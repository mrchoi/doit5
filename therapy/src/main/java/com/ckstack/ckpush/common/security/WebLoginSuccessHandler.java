package com.ckstack.ckpush.common.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dhkim94 on 15. 3. 26..
 */
public class WebLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final static Logger LOG = LoggerFactory.getLogger(WebLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CkUserDetails userDetails = (CkUserDetails) authentication.getPrincipal();

        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String redirectPage = urlPathHelper.getContextPath(request) + "/admin";
        String redirectHash = WebUtils.findParameterValue(request, "redirecthash");

        if(redirectHash != null && !StringUtils.trim(redirectHash).equals("")) redirectPage += "#"+redirectHash;

        LOG.info("success login. user_id["+userDetails.getUser_id()+"], member_srl["+
                userDetails.getMember_srl()+"] redirect to page["+redirectPage+"]");

        response.sendRedirect(redirectPage);
    }
}
