package com.ckstack.ckpush.controller.admin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Created by dhkim94 on 15. 4. 9..
 */
@WebAppConfiguration(value = "src/main/webapp")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/webapp/WEB-INF/mvc-config.xml",
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class DashBoardControllerTest {
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webappContext;

    private MockMvc mockMvc;

    private String loginURL;
    private String loginForwardURL;

    private String rootUserId;          // 관리자 user_id
    private String rootUserPassword;    // 관리자 user_password
    private String memberUserId;        // 정회원 user_id
    private String memberUserPassword;  // 정회원 user_password
    private String visitorUserId;       // 준회원 user_id
    private String visitorUserPassword; // 준회원 user_password

    @Test
    @Rollback
    public void sample() { assertThat(1, is(1)); }

/*
    @Before
    public void setUp() {
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();

        // 관리자 정보를 설정
        rootUserId          = "dhkim@ckstack.com";
        rootUserPassword    = md5PasswordEncoder.encodePassword("abcd1234", "");

        // 정회원 정보를 설정
        memberUserId        = "member@ckstack.com";
        memberUserPassword  = md5PasswordEncoder.encodePassword("member", "");

        // 준회원 정보를 설정
        visitorUserId       = "guest@ckstack.com";
        visitorUserPassword = md5PasswordEncoder.encodePassword("guest", "");

        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext)
                .addFilter(springSecurityFilterChain)
                .build();

        // set login URL
        loginURL = "http://localhost"+webappContext.getServletContext().getContextPath()+"/admin/open/login";
        loginForwardURL = webappContext.getServletContext().getContextPath()+"/admin/open/login?error=3";
    }
*/

    /**
     * welcom 페이지 접속 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void welcomTest() throws Exception {
        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(get("/admin").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", loginURL));

        // spring security 통과 처리
        Authentication authentication = new UsernamePasswordAuthenticationToken(rootUserId, rootUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // TODO userProfileImage 가지고 있는 사람과 없는 사람 두 번 체크 해야 함. 현재는 없는 사람만 체크 하고 있음.

        // welcom 페이지에 정상 접속 테스트
        ModelAndView mav = mockMvc.perform(get("/admin")
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/dash/welcom"))
                .andExpect(model().attributeExists("userId"))
                //.andExpect(model().attributeExists("userProfileImage"))
                .andReturn().getModelAndView();

        assertThat(mav.getModel().get("userId").toString(), is(rootUserId));
    }
*/

    /**
     * 준회원이 어드민 웹사이트 welcom 페이지에 접근 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void visitorConnectWelcomTest() throws Exception {
        // spring security
        Authentication authentication = new UsernamePasswordAuthenticationToken(visitorUserId, visitorUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // welcom 페이지에 접근 시도
        // 접근 권한이 없기 때문에 로그인 페이지로 redirect 되어야 함. 접근 권한 없음
        mockMvc.perform(get("/admin")
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl(loginForwardURL));
    }
*/

    /**
     * 정회원이 어드민 웹사이트 welcom 페이지에 접근 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void memberConnectWelcomTest() throws Exception {
        // spring security
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberUserId, memberUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // welcom 페이지에 접근 시도
        // 접근 권한이 없기 때문에 로그인 페이지로 redirect 되어야 함. 접근 권한 없음
        mockMvc.perform(get("/admin")
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl(loginForwardURL));
    }
*/

/*
    @Test
    @Rollback
    public void overallDashBoardTest() throws Exception {
        // TODO controller 가 완료 되지 않아서 완료 되면 다시 만들어야 한다.

        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(get("/admin/dash").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", loginURL));

        // spring security 통과 처리
        Authentication authentication = new UsernamePasswordAuthenticationToken(rootUserId, rootUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // overall 페이지에 정상 접속 테스트
        ModelAndView mav = mockMvc.perform(get("/admin/dash")
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/dash/overall"))
                .andReturn().getModelAndView();
    }
*/

    /**
     * 준회원이 어드민 웹사이트 overall dashboard 페이지에 접근 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void visitorConnectOverallDashBoardTest() throws Exception {
        // spring security
        Authentication authentication = new UsernamePasswordAuthenticationToken(visitorUserId, visitorUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // welcom 페이지에 접근 시도
        // 접근 권한이 없기 때문에 로그인 페이지로 redirect 되어야 함. 접근 권한 없음
        mockMvc.perform(get("/admin/dash")
                    .session(session)
                    .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl(loginForwardURL));
    }
*/

    /**
     * 정회원이 어드민 웹사이트 overall dashboard 페이지에 접근 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void memberConnectOverallDashBoardTest() throws Exception {
        // spring security
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberUserId, memberUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // welcom 페이지에 접근 시도
        // 접근 권한이 없기 때문에 로그인 페이지로 redirect 되어야 함. 접근 권한 없음
        mockMvc.perform(get("/admin/dash")
                    .session(session)
                    .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl(loginForwardURL));
    }
*/
}
