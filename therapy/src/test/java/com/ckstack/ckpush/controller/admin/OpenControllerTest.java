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

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.user.MemberService;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 3. 30..
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
public class OpenControllerTest {
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webappContext;
    @Autowired
    private MemberService memberService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private MessageSource messageSource;

    private MockMvc mockMvc;

    @Test
    @Rollback
    public void sample() { assertThat(1, is(1)); }

/*
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext)
                .addFilter(springSecurityFilterChain)
                .build();
    }
*/

    /**
     * 로그인 Form 페이지를 테스트 한다.
     * @throws Exception 테스트 예외
     */
/*
    @Test
    @Rollback
    public void loginFormPageTest() throws Exception {
        mockMvc.perform(get("/admin/open/login").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/open/login"));
    }
*/

    /**
     * root 사용자 로그인 request 를 테스트 한다.
     * @throws Exception 테스트 예외
     */
/*
    @Test
    @Rollback
    public void rootMemberLoginProcessTest() throws Exception {
        // member 존재 유무 확인
        MemberEntity memberEntity = memberService.getMemberInfo("dhkim@ckstack.com");
        assertThat(memberEntity, is(notNullValue()));
        assertThat(memberEntity.getEnabled(), is(MDV.YES));
        assertThat(memberEntity.getSign_out(), is(MDV.NO));

        // dhkim@ckstack.com 으로 정상 로그인 테스트
        HttpSession session = mockMvc.perform(post("/admin/open/authentication")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .param("user_id", "dhkim@ckstack.com")
                .param("user_password", "e19d5cd5af0378da05f63f891c7467af"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin"))
                .andReturn().getRequest().getSession();

        // 성공이기 때문에 로그인 실패 메시지가 없어야 한다.
        Object loginFailedObj = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        assertThat(loginFailedObj, is(nullValue()));

        // 패스워드가 잘못 됐을때 로그인 실패 테스트
        // org.springframework.security.authentication.BadCredentialsException: Bad credentials
        session = mockMvc.perform(post("/admin/open/authentication")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .param("user_id", "dhkim@ckstack.com")
                .param("user_password", "e19d5cd5af0378da05f63f891c7467af1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/open/login?error=1"))
                .andReturn().getRequest().getSession();

        // 로그인 실패 메시지가 세션에 들어 있는지 본다
        loginFailedObj = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        assertThat(loginFailedObj, is(notNullValue()));
        System.out.println("login failed exception["+loginFailedObj.toString()+"]");

        // 사용자를 disabled 시킨다.
        memberService.enabledMember(memberEntity.getUser_id(), MDV.NO);
        memberEntity = memberService.getMemberInfo("dhkim@ckstack.com");
        assertThat(memberEntity, is(notNullValue()));
        assertThat(memberEntity.getEnabled(), is(MDV.NO));

        // 사용자가 disabled 되었기 때문에 로그인 실패 하는지 테스트
        // org.springframework.security.authentication.DisabledException: User is disabled
        session = mockMvc.perform(post("/admin/open/authentication")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .param("user_id", "dhkim@ckstack.com")
                .param("user_password", "e19d5cd5af0378da05f63f891c7467af"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/open/login?error=1"))
                .andReturn().getRequest().getSession();

        // 로그인 실패 메시지가 세션에 들어 있는지 본다
        loginFailedObj = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        assertThat(loginFailedObj, is(notNullValue()));
        System.out.println("login failed exception["+loginFailedObj.toString()+"]");

        // 사용자를 deny 시킨다.
        memberService.enabledMember(memberEntity.getUser_id(), MDV.DENY);
        memberEntity = memberService.getMemberInfo("dhkim@ckstack.com");
        assertThat(memberEntity, is(notNullValue()));
        assertThat(memberEntity.getEnabled(), is(MDV.DENY));

        // 사용자가 deny 되었기 때문에 로그인 실패 하는지 테스트
        // org.springframework.security.authentication.DisabledException: User is disabled
        session = mockMvc.perform(post("/admin/open/authentication")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .param("user_id", "dhkim@ckstack.com")
                .param("user_password", "e19d5cd5af0378da05f63f891c7467af"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/open/login?error=1"))
                .andReturn().getRequest().getSession();

        // 로그인 실패 메시지가 세션에 들어 있는지 본다
        loginFailedObj = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        assertThat(loginFailedObj, is(notNullValue()));
        System.out.println("login failed exception[" + loginFailedObj.toString() + "]");

        // 사용자를 enabled 시킨다.
        memberService.enabledMember(memberEntity.getUser_id(), MDV.YES);
        memberEntity = memberService.getMemberInfo("dhkim@ckstack.com");
        assertThat(memberEntity, is(notNullValue()));
        assertThat(memberEntity.getEnabled(), is(MDV.YES));

        // 사용자를 sign out 시킨다.
        memberService.signOutMember(memberEntity.getUser_id());
        memberEntity = memberService.getMemberInfo("dhkim@ckstack.com");
        assertThat(memberEntity, is(notNullValue()));
        assertThat(memberEntity.getSign_out(), is(MDV.YES));

        // 사용자가 sign out 되었기 때문에 로그인 실패 하는지 테스트
        // org.springframework.security.authentication.AccountExpiredException: User account has expired
        session = mockMvc.perform(post("/admin/open/authentication")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .param("user_id", "dhkim@ckstack.com")
                .param("user_password", "e19d5cd5af0378da05f63f891c7467af"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/admin/open/login?error=1"))
                .andReturn().getRequest().getSession();

        // 로그인 실패 메시지가 세션에 들어 있는지 본다
        loginFailedObj = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        assertThat(loginFailedObj, is(notNullValue()));
        System.out.println("login failed exception["+loginFailedObj.toString()+"]");

        //Enumeration<String> enumeration = session.getAttributeNames();
        //while(enumeration.hasMoreElements()) {
        //    System.out.println(enumeration.nextElement());
        //}
    }
*/

/*
    @Test
    @Rollback
    public void makeUUIDTest() throws Exception {
        String tid = "1";

        Locale locale = LocaleContextHolder.getLocale();

        // request content type 이 text/html;charset=UTF-8 일때
        mockMvc.perform(get("/admin/open/uuid/t/" + tid).accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists("uuid"))
                .andExpect(model().attribute(confCommon.getProperty("json_tid"), is(tid)))
                .andExpect(model().attribute(confCommon.getProperty("json_error"), is(messageSource.getMessage("ecd.success", null, locale))))
                .andExpect(model().attribute(confCommon.getProperty("json_message"), is(messageSource.getMessage("emg.success", null, locale))));

        // request content type 이 application/json;charset=UTF-8 일때
        mockMvc.perform(get("/admin/open/uuid/t/"+tid).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists("uuid"))
                .andExpect(model().attribute(confCommon.getProperty("json_tid"), is(tid)))
                .andExpect(model().attribute(confCommon.getProperty("json_error"), is(messageSource.getMessage("ecd.success", null, locale))))
                .andExpect(model().attribute(confCommon.getProperty("json_message"), is(messageSource.getMessage("emg.success", null, locale))));
    }
*/
}
