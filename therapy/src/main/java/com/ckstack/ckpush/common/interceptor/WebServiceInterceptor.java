package com.ckstack.ckpush.common.interceptor;

import com.ckstack.ckpush.common.security.CkUserDetails;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 3. 30..
 */
public class WebServiceInterceptor implements HandlerInterceptor {
    private final static Logger LOG = LoggerFactory.getLogger(WebServiceInterceptor.class);

    @Autowired
    protected Properties confDym;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // freemarker를 위한 context path 를 request attribute 에 넣는다.
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String contextPath = urlPathHelper.getContextPath(request);
        request.setAttribute("contextPath", contextPath);

        // resource path를 넣는다.
        String resPath;
        if(StringUtils.equals(confDym.getProperty("resource_location"), "1"))
            resPath = contextPath + confDym.getProperty("resource_path");
        else
            resPath = confDym.getProperty("resource_path");
        request.setAttribute("resPath", resPath);

        String svcResPath;
        if(StringUtils.equals(confDym.getProperty("svc_resource_path"), "1"))
            svcResPath = contextPath + confDym.getProperty("svc_resource_path");
        else
            svcResPath = confDym.getProperty("svc_resource_path");
        request.setAttribute("svcResPath", svcResPath);

        boolean loginSession = false;
        String loginId = "";
        String loginAuthority = "";
        try{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CkUserDetails) {
                loginSession = true;
                CkUserDetails userDetails = (CkUserDetails) principal;

                loginId = userDetails.getUser_name() + " (" +userDetails.getUser_id() + ") ";

                String path = request.getServletPath();

                List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
                int num = 0;
                for(GrantedAuthority grantedAuthority : authorities){
                    if(num == 0) loginAuthority = grantedAuthority.getAuthority();
                    //사용자 페이지에서 잡속 했으나 사용자가 아닐경우
                    if(path.indexOf("/user") != -1 && !grantedAuthority.getAuthority().equals("ROLE_USER")) loginSession = false;
                    else if(path.indexOf("/admin") != -1 && grantedAuthority.getAuthority().equals("ROLE_USER")) loginSession = false;

                    num++;
                }
            }
        }catch (Exception e){
            LOG.info(e.getMessage());
        }
        request.setAttribute("loginSession", loginSession);
        request.setAttribute("loginId", loginId);
        request.setAttribute("loginAuthority", loginAuthority);

        LOG.debug("set request attribute contextPath["+contextPath+"]");
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
