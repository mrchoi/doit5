package com.ckstack.ckpush.common.interceptor;

import com.ckstack.ckpush.domain.accessory.DeviceAccessTokenEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.AuthService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 7. 13..
 * 회원 모듈이 포함되지 않고 단말기 아이디로 돌아가는 api 에서 access_token을 체크 한다.
 */
public class DeviceAccessTokenInterceptor implements HandlerInterceptor {
    private final static Logger LOG = LoggerFactory.getLogger(DeviceAccessTokenInterceptor.class);

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

        if(accessToken == null || StringUtils.equals(StringUtils.trim(accessToken), "")) {
            LOG.error("not found access_token in http header. URI [" + requestURI + "]");

            String tid = webUtilService.getTid(request);
            request.setAttribute(confCommon.getProperty("json_tid"), tid);
            request.setAttribute(confCommon.getProperty("json_request_uri"), requestURI);
            request.setAttribute(confCommon.getProperty("json_method"), request.getMethod());
            request.setAttribute("error_code", "process_access_token_error");
            request.getRequestDispatcher("/dapi/open/show/error").forward(request, response);

            // 상세 원인은 일부러 넣지 않았음. 자세한 설명을 바탕으로 이상하게 요청 할 수 있음

            return false;
        }

        DeviceAccessTokenEntity deviceAccessTokenEntity = authService.getAccessTokenUsingDevice(accessToken);
        if(deviceAccessTokenEntity == null) {
            LOG.error("invalid access token [" + accessToken + "]");

            String tid = webUtilService.getTid(request);
            request.setAttribute(confCommon.getProperty("json_tid"), tid);
            request.setAttribute(confCommon.getProperty("json_request_uri"), requestURI);
            request.setAttribute(confCommon.getProperty("json_method"), request.getMethod());
            request.setAttribute("error_code", "process_access_token_error");
            request.getRequestDispatcher("/dapi/open/show/error").forward(request, response);

            // 상세 원인은 일부러 넣지 않았음. 자세한 설명을 바탕으로 이상하게 요청 할 수 있음

            return false;
        }

        int renewalSec = authService.renewalAccessTokenExpireUsingDevice(deviceAccessTokenEntity.getToken_srl());
        response.setHeader(confCommon.getProperty("access_token_alive_header_name"), Integer.toString(renewalSec));

        // DB에서 읽은 것을 저장한다. 추후 다시 DB에서 읽지 않게 하기 위함 임.
        request.setAttribute(confCommon.getProperty("access_token_info"), deviceAccessTokenEntity);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }
}
