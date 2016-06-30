package com.ckstack.ckpush.controller.admin;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


import com.ckstack.ckpush.common.MDV;
import org.joda.time.DateTime;
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

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 4. 28..
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
public class MemberControllerTest {
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webappContext;
    @Autowired
    protected Properties confCommon;

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
     * jquery select2 를 위한 api 테스트
     */
/*
    @Test
    @Rollback
    public void listUserOfSelect2Test() throws Exception {
        // 인증을 통과 하지 않으면 실패.
        mockMvc.perform(post("/admin/member/list/select2/{enabled}/t/{tid}", MDV.YES, DateTime.now().getMillis())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("query", "run-junit")
                .param("offset", "0")
                .param("limit", "20"))
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

        // 검색값이 empty string 이기 때문에 검색 결과가 없음
        ModelAndView mav1 = mockMvc.perform(post("/admin/member/list/select2/{enabled}/t/{tid}", MDV.YES, DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("query", "")
                .param("offset", "0")
                .param("limit", "20"))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists(confCommon.getProperty("dtresp_total_row")))
                .andExpect(model().attributeExists("list"))
                .andReturn().getModelAndView();

        List<Map<String, Object>> list1 = (List<Map<String, Object>>) mav1.getModel().get("list");
        assertThat(list1.size(), is(0));

        Integer totalRows = (Integer) mav1.getModel().get(confCommon.getProperty("dtresp_total_row"));
        assertThat(totalRows, is(0));

        // 검색값이 dhkim 이면 값이 있음
        ModelAndView mav2 = mockMvc.perform(post("/admin/member/list/select2/{enabled}/t/{tid}", MDV.YES, DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("query", "dhkim")
                .param("offset", "0")
                .param("limit", "20"))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists(confCommon.getProperty("dtresp_total_row")))
                .andExpect(model().attributeExists("list"))
                .andReturn().getModelAndView();

        List<Map<String, Object>> list2 = (List<Map<String, Object>>) mav2.getModel().get("list");
        assertThat(list2.size() > 0, is(true));

        Long totalRows2 = (Long) mav2.getModel().get(confCommon.getProperty("dtresp_total_row"));
        assertThat(totalRows2 > 0, is(true));
    }
*/
}
