package com.ckstack.ckpush.common.interceptor;

import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.domain.user.MemberAccessTokenEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.AuthService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 8. 21..
 */
public class MemberAccessTokenInterceptor implements HandlerInterceptor {
    private final static Logger LOG = LoggerFactory.getLogger(MemberAccessTokenInterceptor.class);

    @Autowired
    protected Properties confCommon;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String requestURI = urlPathHelper.getOriginatingRequestUri(request);
        String accessToken = request.getHeader(confCommon.getProperty("access_token_header_name"));

        // http header에 존재하지 않으면 cookie에 존재하는지 체크
        // text/html 으로 request 할때는 header에 access token을 주기 힘들고 cookie에 주기는 쉬우므로
        // 쿠키도 체크 하도록 한다.
        if(accessToken == null || StringUtils.equals(StringUtils.trim(accessToken), "")) {
            // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/util/WebUtils.html
            // 문서 참고
            Cookie cookie = WebUtils.getCookie(request, confCommon.getProperty("access_token_header_name"));

            if(cookie == null) {
                LOG.error("not found access_token in http header. URI [" + requestURI + "]");

                String tid = webUtilService.getTid(request);
                request.setAttribute(confCommon.getProperty("json_tid"), tid);
                request.setAttribute(confCommon.getProperty("json_request_uri"), requestURI);
                request.setAttribute(confCommon.getProperty("json_method"), request.getMethod());
                request.setAttribute("error_code", "process_access_token_error");
                request.getRequestDispatcher("/api/open/show/error").forward(request, response);

                // 상세 원인은 일부러 넣지 않았음. 자세한 설명을 바탕으로 이상하게 요청 할 수 있음

                return false;
            }

            accessToken = StringUtils.trim(cookie.getValue());
        }

        MemberAccessTokenEntity memberAccessTokenEntity = authService.getAccessTokenUsingMember(accessToken);
        if(memberAccessTokenEntity == null) {
            LOG.error("invalid access token [" + accessToken + "]");

            String tid = webUtilService.getTid(request);
            request.setAttribute(confCommon.getProperty("json_tid"), tid);
            request.setAttribute(confCommon.getProperty("json_request_uri"), requestURI);
            request.setAttribute(confCommon.getProperty("json_method"), request.getMethod());
            request.setAttribute("error_code", "process_access_token_error");
            request.getRequestDispatcher("/api/open/show/error").forward(request, response);

            // 상세 원인은 일부러 넣지 않았음. 자세한 설명을 바탕으로 이상하게 요청 할 수 있음

            return false;
        }

        AccessTokenExtra accessTokenExtra = null;
        if(memberAccessTokenEntity.getUser_data() != null &&
                !StringUtils.equals(memberAccessTokenEntity.getUser_data(), "")) {
            try {
                accessTokenExtra = (AccessTokenExtra) webUtilService.convertJsonBytesToObject(
                        "com.ckstack.ckpush.data.cache.AccessTokenExtra",
                        memberAccessTokenEntity.getUser_data().getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                LOG.warn("can't convert user json string to object. e [" +
                        e.getMessage() + "]");
            }
            // 부가 정보가 존재하면 로그키를 설정 한다.
            if(accessTokenExtra != null)    MDC.put("logkey", accessTokenExtra.getUser_id());
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        int renewalSec = authService.renewalAccessTokenExpireUsingMember(accessToken, ltm);

        response.setHeader(confCommon.getProperty("access_token_alive_header_name"), Integer.toString(renewalSec));

        // DB에서 읽은 것을 저장한다. 추후 다시 DB에서 읽지 않게 하기 위함 임.
        if(accessTokenExtra != null) {
            accessTokenExtra.setToken_expire(ltm + renewalSec);
            request.setAttribute(confCommon.getProperty("access_token_info"), accessTokenExtra);
        } else {
            request.setAttribute(confCommon.getProperty("access_token_info"), memberAccessTokenEntity);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
