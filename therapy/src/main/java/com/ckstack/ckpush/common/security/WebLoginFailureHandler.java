package com.ckstack.ckpush.common.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
public class WebLoginFailureHandler implements AuthenticationFailureHandler {
    private final static Logger LOG = LoggerFactory.getLogger(WebLoginFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String userId = request.getParameter("user_id");
        HttpSession httpSession = request.getSession();

        httpSession.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String redirectPage = urlPathHelper.getContextPath(request) + "/admin/open/login?error=1";
        String redirectHash = WebUtils.findParameterValue(request, "redirecthash");

        if(redirectHash != null && !StringUtils.trim(redirectHash).equals("")) redirectPage += "#"+redirectHash;

        LOG.info("failed login. user_id[" + userId + "], reason[" + exception.getMessage() + "], redirect to page["+
                redirectPage);

        response.sendRedirect(redirectPage);
    }
}
