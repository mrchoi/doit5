package com.ckstack.ckpush.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dhkim94 on 15. 4. 14..
 */
public class CrosDomainFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(CrosDomainFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Accept, Accept-Encoding, Accept-Language, Cache-Control, " +
//                "Connection, Content-Length, Content-Type, Cookie, Host, Origin, Pragma, Referer, " +
//                "User-Agent, X-Requested-With");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        LOG.debug("set cross domain header");
        chain.doFilter(request, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
