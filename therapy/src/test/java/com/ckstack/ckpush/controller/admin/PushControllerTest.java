package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.push.PushMessageEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.user.MemberService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by dhkim94 on 15. 4. 28..
 *
 * 터미널에서 클래스 하나만 테스트 돌릴때는 다음과 같이 하면 된다.(함수 하나 돌리는 건 나중에 찾도록 하자...)
 * $ ./gradlew cleanTest test --tests com.ckstack.ckpush.controller.admin.PushControllerTest
 *
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
public class PushControllerTest {
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webappContext;
    @Autowired
    protected Properties confCommon;

    Md5PasswordEncoder md5PasswordEncoder;

    private MockMvc mockMvc;

    private String loginURL;
    private String loginForwardURL;

    private String rootUserId;          // 관리자 user_id
    private String rootUserPassword;    // 관리자 user_password
    private String memberUserId;        // 정회원 user_id
    private String memberUserPassword;  // 정회원 user_password
    private String visitorUserId;       // 준회원 user_id
    private String visitorUserPassword; // 준회원 user_password

    private byte[] convertObjectToJsonBytes(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper.writeValueAsBytes(obj);
    }

    @Before
    public void setUp() {
        md5PasswordEncoder = new Md5PasswordEncoder();

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

    /**
     * 어드민에서 단말이 등록되지 않은 앱에 push 메시지를 발송 할때 에러 발생 하는 지 테스트.
     * 20150818 -   테스트 서버에 반영 할때 에러 발생 했음.
     *              원인은 redis.properties 의 redis 접속 주소가 잘못 되어서 발생 한 것임.
     */
    @Test
    @Rollback
    public void noDeviceSendPushMessage() throws Exception {
        // spring security 통과 처리
        Authentication authentication = new UsernamePasswordAuthenticationToken(rootUserId, rootUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // 1. 테스트를 위한 신규 앱을 추가 한다.
        AppEntity appEntity = new AppEntity();
        appEntity.setApp_id("junit.test.app");
        appEntity.setApp_name("junit test app99");
        appEntity.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity.setApp_version("1.0");
        appEntity.setEnabled(MDV.YES);

        long tid2 = DateTime.now().getMillis();
        ModelAndView mav2 = mockMvc.perform(post("/admin/app/add/t/{tid}", tid2)
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(appEntity)))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists("app_srl"))
                .andExpect(model().attributeExists("api_key"))
                .andExpect(model().attributeExists("api_secret"))
                .andExpect(model().attributeExists("app_version"))
                .andExpect(model().attributeExists("app_name"))
                .andReturn().getModelAndView();

        assertThat(Long.parseLong(mav2.getModel().get(confCommon.getProperty("json_tid")).toString(), 10), is(tid2));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_error")).toString().equals("S000001"), is(true));
        assertThat(mav2.getModel().get("api_key").toString().equals(appEntity.getApi_key()), is(true));
        assertThat(mav2.getModel().get("api_secret").toString().equals(appEntity.getApi_secret()), is(true));
        assertThat(mav2.getModel().get("app_version").toString().equals(appEntity.getApp_version()), is(true));
        assertThat(mav2.getModel().get("app_name").toString().equals(appEntity.getApp_name()), is(true));

        int appSrl = Integer.parseInt(mav2.getModel().get("app_srl").toString(), 10);

        // 2. 아무런 단말이 등록되지 않은 앱에 메시지 발송
        PushMessageEntity pushMessageEntity = new PushMessageEntity();
        pushMessageEntity.setPush_title("no target send message");
        pushMessageEntity.setPush_text("no target send body");
        pushMessageEntity.setApp_srl(appSrl);
        pushMessageEntity.setPush_target(confCommon.getProperty("push_target_all_in_app"));


        long tid3 = DateTime.now().getMillis();
        ModelAndView mav3 = mockMvc.perform(post("/admin/message/add/t/{tid}", tid3)
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(pushMessageEntity)))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andReturn().getModelAndView();

        assertThat(Long.parseLong(mav3.getModel().get(confCommon.getProperty("json_tid")).toString(), 10), is(tid3));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_error")).toString().equals("S000001"), is(true));
    }

    /**
     * push 를 하기 위한 앱 리스트 출력 폼 테스트
     *
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void listAppForGcmApnsFormTest() throws Exception {
        // 인증을 통과 하지 않으면 실패.
        mockMvc.perform(get("/admin/message/gcm/apns/app/list")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
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

        ModelAndView mav1 = mockMvc.perform(get("/admin/message/gcm/apns/app/list")
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/message/list_app_for_gcm_apns"))
                .andReturn().getModelAndView();
    }
*/

    /**
     * push 메시지를 보여주기 위한 app list data api 테스트.
     * 활성화된 앱만 보여 준다.
     *
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void listAppForGcmApnsTest() throws Exception {
        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(post("/admin/message/gcm/apns/app/list/t/{tid}", DateTime.now().getMillis())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8"))
                .param("draw", "1")
                .param("columns[0][data]", "app_srl")
                .param("columns[0][name]", "app_srl")
                .param("columns[0][searchable]", "true")
                .param("columns[0][orderable]", "false")
                .param("columns[0][search][value]", "")
                .param("columns[0][search][regex]", "false")
                .param("columns[1][data]", "app_name")
                .param("columns[1][name]", "app_name")
                .param("columns[1][searchable]", "true")
                .param("columns[1][orderable]", "true")
                .param("columns[1][search][value]", "")
                .param("columns[1][search][regex]", "false")
                .param("columns[2][data]", "device_class")
                .param("columns[2][name]", "device_class")
                .param("columns[2][searchable]", "false")
                .param("columns[2][orderable]", "false")
                .param("columns[2][search][value]", "")
                .param("columns[2][search][regex]", "false")
                .param("columns[3][data]", "reg_terminal")
                .param("columns[3][name]", "reg_terminal")
                .param("columns[3][searchable]", "false")
                .param("columns[3][orderable]", "false")
                .param("columns[3][search][value]", "")
                .param("columns[3][search][regex]", "false")
                .param("columns[4][data]", "reg_push_terminal")
                .param("columns[4][name]", "reg_push_terminal")
                .param("columns[4][searchable]", "false")
                .param("columns[4][orderable]", "false")
                .param("columns[4][search][value]", "")
                .param("columns[4][search][regex]", "false")
                .param("columns[5][data]", "push_count")
                .param("columns[5][name]", "push_count")
                .param("columns[5][searchable]", "false")
                .param("columns[5][orderable]", "false")
                .param("columns[5][search][value]", "")
                .param("columns[5][search][regex]", "false")
                .param("order[0][column]", "1")
                .param("order[0][dir]", "desc")
                .param("start", "0")
                .param("length", "10")
                .param("search[value]", "")
                .param("search[regex]", "false"))
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

        mockMvc.perform(post("/admin/message/gcm/apns/app/list/t/{tid}", DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8"))
                .param("draw", "1")
                .param("columns[0][data]", "app_srl")
                .param("columns[0][name]", "app_srl")
                .param("columns[0][searchable]", "true")
                .param("columns[0][orderable]", "false")
                .param("columns[0][search][value]", "")
                .param("columns[0][search][regex]", "false")
                .param("columns[1][data]", "app_name")
                .param("columns[1][name]", "app_name")
                .param("columns[1][searchable]", "true")
                .param("columns[1][orderable]", "true")
                .param("columns[1][search][value]", "")
                .param("columns[1][search][regex]", "false")
                .param("columns[2][data]", "device_class")
                .param("columns[2][name]", "device_class")
                .param("columns[2][searchable]", "false")
                .param("columns[2][orderable]", "false")
                .param("columns[2][search][value]", "")
                .param("columns[2][search][regex]", "false")
                .param("columns[3][data]", "reg_terminal")
                .param("columns[3][name]", "reg_terminal")
                .param("columns[3][searchable]", "false")
                .param("columns[3][orderable]", "false")
                .param("columns[3][search][value]", "")
                .param("columns[3][search][regex]", "false")
                .param("columns[4][data]", "reg_push_terminal")
                .param("columns[4][name]", "reg_push_terminal")
                .param("columns[4][searchable]", "false")
                .param("columns[4][orderable]", "false")
                .param("columns[4][search][value]", "")
                .param("columns[4][search][regex]", "false")
                .param("columns[5][data]", "push_count")
                .param("columns[5][name]", "push_count")
                .param("columns[5][searchable]", "false")
                .param("columns[5][orderable]", "false")
                .param("columns[5][search][value]", "")
                .param("columns[5][search][regex]", "false")
                .param("order[0][column]", "1")
                .param("order[0][dir]", "desc")
                .param("start", "0")
                .param("length", "10")
                .param("search[value]", "")
                .param("search[regex]", "false"))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists(confCommon.getProperty("dtresp_data")))
                .andExpect(model().attributeExists(confCommon.getProperty("dtresp_draw")))
                .andExpect(model().attributeExists(confCommon.getProperty("dtresp_total_row")))
                .andExpect(model().attributeExists(confCommon.getProperty("dtresp_filter_row")))
                .andReturn().getModelAndView();
    }
*/
}
