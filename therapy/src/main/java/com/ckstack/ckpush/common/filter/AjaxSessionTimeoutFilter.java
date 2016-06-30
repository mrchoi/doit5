package com.ckstack.ckpush.common.filter;

import com.ckstack.ckpush.common.MDV;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by dhkim94 on 15. 4. 19..
 */
public class AjaxSessionTimeoutFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(AjaxSessionTimeoutFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession(false);

        if(session == null) {
            String ajaxHeader = httpServletRequest.getHeader("X-Requested-With");

            if(StringUtils.equals(ajaxHeader, "XMLHttpRequest")) {
                LOG.info("current session timeout. request is ajax. response http error code [" +
                        MDV.HTTP_ERR_AJAX_SESSION_TIMEOUT +"]");
                httpServletResponse.sendError(MDV.HTTP_ERR_AJAX_SESSION_TIMEOUT);
            } else {
                // ajax가 아니면 다른곳에서 처리 한다.
                LOG.info("current session timeout. redirect login page");
                chain.doFilter(request, response);
            }
        } else {
            LOG.debug("current session valid");

            // 현재 남아 있는 session 시간을 확인 한다.
            //long sessionEndTime = session.getCreationTime() + session.getMaxInactiveInterval() * 1000;
            //int sessionRestTime = (int)((sessionEndTime - DateTime.now().getMillis()) / 1000);
            //request.setAttribute("sessionRestSec", sessionRestTime);

            request.setAttribute("sessionRestSec", session.getMaxInactiveInterval());

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
