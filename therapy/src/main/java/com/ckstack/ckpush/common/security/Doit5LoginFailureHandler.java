package com.ckstack.ckpush.common.security;

import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by kodaji on 2016. 1. 22..
 */
public class Doit5LoginFailureHandler implements AuthenticationFailureHandler {
    private final static Logger LOG = LoggerFactory.getLogger(Doit5LoginFailureHandler.class);

    @Autowired
    private MemberService memberService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String userId = request.getParameter("user_id");
        String userPasswd = request.getParameter("user_password");
        HttpSession httpSession = request.getSession();

        httpSession.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception);

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String redirectPage = urlPathHelper.getContextPath(request) + "/user/open/login";

        MemberEntity memberEntity = memberService.getMemberInfo(userId);
        if(memberEntity == null) {
            LOG.info("failed login. not found user_id["+userId+"]");
            redirectPage += "?error=1";

            response.sendRedirect(redirectPage);

            return;
        } else {
            LOG.info("failed login. password wrong ["+userId+"]");
            redirectPage += "?error=2";
        }

        response.sendRedirect(redirectPage);
    }
}
