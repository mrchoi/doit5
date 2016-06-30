package com.ckstack.ckpush.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dhkim94 on 15. 4. 9..
 * 현재 사용하지 않는다. 추후 참고용으로 그대로 둔다.
 */
public class CkUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final static Logger LOG = LoggerFactory.getLogger(CkUsernamePasswordAuthenticationFilter.class);

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        LOG.debug("-----> here here here successfulAuthentication");
        String prev_hash = request.getParameter("prev_hash");
        LOG.debug("-----> additional parameter[" + prev_hash + "]");
        super.successfulAuthentication(request, response, chain, authResult);
    }


}
