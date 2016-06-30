package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by dhkim94 on 15. 4. 8..
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
public class AppControllerTest {
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webappContext;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    protected Properties confCommon;

    @Autowired
    private AppController appController;

    private MockMvc mockMvc;
    private MockMvc standaloneMvc;

    private String loginURL;
    private String loginForwardURL;

    private String rootUserId;          // 관리자 user_id
    private String rootUserPassword;    // 관리자 user_password
    private String memberUserId;        // 정회원 user_id
    private String memberUserPassword;  // 정회원 user_password
    private String visitorUserId;       // 준회원 user_id
    private String visitorUserPassword; // 준회원 user_password

    private AppEntity appEntity1;
    private AppEntity appEntity2;
    private AppEntity appEntity3;

    private byte[] convertObjectToJsonBytes(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper.writeValueAsBytes(obj);
    }

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

        standaloneMvc = MockMvcBuilders.standaloneSetup(appController)
                .setValidator(new LocalValidatorFactoryBean()).build();

        // set login URL
        loginURL = "http://localhost"+webappContext.getServletContext().getContextPath()+"/admin/open/login";
        loginForwardURL = webappContext.getServletContext().getContextPath()+"/admin/open/login?error=3";

        appEntity1 = new AppEntity();
        appEntity1.setApp_name("junit test app1");
        appEntity1.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApp_version("1.0");
        appEntity1.setEnabled(MDV.YES);

        appEntity2 = new AppEntity();
        appEntity2.setApp_name("junit test app2");
        appEntity2.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApp_version("1.0");
        appEntity2.setEnabled(MDV.YES);

        appEntity3 = new AppEntity();
        appEntity3.setApp_name("junit test app3");
        appEntity3.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity3.setApp_version("1.0");
        appEntity3.setEnabled(MDV.YES);
    }
*/

    /**
     * admin 웹페이지의 app 추가하는 form 페이지를 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void addAppFormTest() throws Exception {
        assertThat(confAccessory.containsKey("user_terminal_support"), is(true));

        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(get("/admin/app/add").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
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

        // 앱 추가하는 form 페이지에 정상 접속 테스트
        ModelAndView mav = mockMvc.perform(get("/admin/app/add")
                    .session(session)
                    .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/app/add_app"))
                .andExpect(model().attributeExists("appEntity"))
                .andReturn().getModelAndView();

        AppEntity appEntity = (AppEntity) mav.getModel().get("appEntity");
        assertThat(appEntity, is(notNullValue()));
        assertThat(appEntity.getApp_srl(), is(0));
        assertThat(appEntity.getApp_name(), is(nullValue()));
        assertThat(appEntity.getApi_key(), is(nullValue()));
        assertThat(appEntity.getApp_version(), is(nullValue()));
        assertThat(appEntity.getApi_secret(), is(nullValue()));
        assertThat(appEntity.getC_date(), is(0));
        assertThat(appEntity.getU_date(), is(0));
    }
*/

    /**
     * 준회원이 어드민 웹사이트 app 추가하는 form 페이지에 접근 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void visitorConnectAddAppFormTest() throws Exception {
        // spring security
        Authentication authentication = new UsernamePasswordAuthenticationToken(visitorUserId, visitorUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // app 추가하는 form 페이지에 접근 시도
        // 접근 권한이 없기 때문에 로그인 페이지로 redirect 되어야 함. 접근 권한 없음
        mockMvc.perform(get("/admin/app/add")
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
     * 정회원이 어드민 웹사이트 app 추가하는 form 페이지에 접근 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void memberConnectAddAppFormTest() throws Exception {
        // spring security
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberUserId, memberUserPassword);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        // app 추가하는 form 페이지에 접근 시도
        // 접근 권한이 없기 때문에 로그인 페이지로 redirect 되어야 함. 접근 권한 없음
        mockMvc.perform(get("/admin/app/add")
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
     * add app 파라미터 바인딩 실패 값이 없음.(CustomException 이 아니고 NestedServletException 발생함)
     * 바인딩 실패는 아래 내용을 참고 해서 테스트 필요하면 추가 하도록 한다.
     * 완전한 자동 테스트는 아님.
     * @throws Exception
     */
/*
    @Test(expected = NestedServletException.class)
    @Rollback
    public void addAppSubmitRequestBindingTest() throws Exception {
        // standalone 이기 때문에 spring security 줄 수 없음

        AppEntity appEntity = new AppEntity();
        // NOTE @ByteSize 가 있어서 junit 실패남. 이건 테스트 하지 말자.
        appEntity.setApp_name("junit test app1");
        appEntity.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity.setApp_version(".1.0");

        standaloneMvc.perform(post("/admin/app/add/t/{tid}", DateTime.now().getMillis() / 1000)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(appEntity)));
    }
*/

    /**
     * 앱 추가 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void addAppSubmitTest() throws Exception {
        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(post("/admin/app/add/t/{tid}", DateTime.now().getMillis())
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

        // 앱 추가하는 submit 페이지에 정상 접속 테스트(return json)
        ModelAndView mav = mockMvc.perform(post("/admin/app/add/t/{tid}", DateTime.now().getMillis())
                    .session(session)
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                    .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                    .content(this.convertObjectToJsonBytes(appEntity1)))
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
                .andExpect(model().attributeExists("api_version"))
                .andExpect(model().attributeExists("api_name"))
                .andReturn().getModelAndView();

        assertThat((int) mav.getModel().get("app_srl") > 0, is(true));
        assertThat((String) mav.getModel().get("api_key"), is(appEntity1.getApi_key()));
        assertThat((String) mav.getModel().get("api_secret"), is(appEntity1.getApi_secret()));
        assertThat((String) mav.getModel().get("api_version"), is(appEntity1.getApp_version()));
        assertThat((String) mav.getModel().get("api_name"), is(appEntity1.getApp_name()));

        int appSrl = (int) mav.getModel().get("app_srl");

        // 앱 정보를 가져온다.
        // 인증이 안되면 접속 불가
        mockMvc.perform(get("/admin/app/{app_srl}", appSrl)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", loginURL));

        ModelAndView mav1_1 =mockMvc.perform(get("/admin/app/{app_srl}", appSrl)
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/app/detail_app"))
                .andExpect(model().attributeExists("appEntity"))
                .andExpect(model().attributeExists("appManager"))
                .andReturn().getModelAndView();

        // 리턴되는 값들을 테스트 해 본다.
        assertThat(mav1_1.getModel().get("appEntity"), is(notNullValue()));
        assertThat(mav1_1.getModel().get("appManager"), is(notNullValue()));

        AppEntity resultEntity1 = (AppEntity) mav1_1.getModel().get("appEntity");
        assertThat(resultEntity1.getApp_srl(), is(appSrl));
        assertThat(resultEntity1.getApp_name(), is(appEntity1.getApp_name()));

        List<MemberEntity> appManager = (List<MemberEntity>) mav1_1.getModel().get("appManager");
        assertThat(appManager.size(), is(1));
        assertThat(appManager.get(0).getUser_id(), is(rootUserId));

        // 앱 추가하는 submit 페이지에 정상 접속 테스트(return html)
        mockMvc.perform(post("/admin/app/add/t/{tid}", DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(appEntity2)))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists("app_srl"))
                .andExpect(model().attributeExists("api_key"))
                .andExpect(model().attributeExists("api_secret"))
                .andExpect(model().attributeExists("api_version"))
                .andExpect(model().attributeExists("api_name"))
                .andReturn().getModelAndView();

        // 중복 이면 실패를 내린다.
        mockMvc.perform(post("/admin/app/add/t/{tid}", DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(appEntity1)))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_comm/error"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_reason")))
                .andReturn().getModelAndView();

        // 등록된 앱을 수정 한다.
        // 인증이 안되면 수정 접속 불가
        Map<String, Object> modifyMap = new HashMap<>();
        String nextAppName = "app name modifyed hello world";
        modifyMap.put("app_name", nextAppName);
        KeyBean modifyBean = new KeyBean();
        modifyBean.setM_key(modifyMap);

        mockMvc.perform(put("/admin/app/{app_srl}/t/{tid}", appSrl, DateTime.now().getMillis())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(modifyBean)))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", loginURL));

        // 등록된 앱을 수정 한다.(앱 정보 변경)
        ModelAndView mav2 = mockMvc.perform(put("/admin/app/{app_srl}/t/{tid}", appSrl, DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(modifyBean)))
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

        assertThat(mav2.getModel().get(confCommon.getProperty("json_error")).toString(), is("S000001"));

        // 등록된 앱을 수정 한다.(앱 관리자 변경) - 앱 정보와 앱 관리자는 동시에 같이 변경 할 수 없음.
        List<String> nextManager = new ArrayList<>();
        nextManager.add(memberUserId);
        nextManager.add(rootUserId);
        modifyMap.put("app_manager", nextManager);
        modifyMap.remove("app_name");
        modifyBean.setM_key(modifyMap);

        ModelAndView mav2_2 = mockMvc.perform(put("/admin/app/{app_srl}/t/{tid}", appSrl, DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(modifyBean)))
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

        assertThat(mav2_2.getModel().get(confCommon.getProperty("json_error")).toString(), is("S000001"));


        // 등록된 앱을 삭제 한다.
        // 인증이 안되면 삭제에 접속 불가
        mockMvc.perform(delete("/admin/app/t/{tid}", DateTime.now().getMillis())
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", loginURL));

        List<Integer> appSrls = new ArrayList<>();
        appSrls.add(appSrl);
        KeyBean keyBean = new KeyBean();
        keyBean.setI_keys(appSrls);

        // 등록된 앱을 삭제 한다.
        ModelAndView mav3 = mockMvc.perform(delete("/admin/app/t/{tid}", DateTime.now().getMillis())
                .session(session)
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .content(this.convertObjectToJsonBytes(keyBean)))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(view().name("f_api/not_support"))
                .andExpect(model().attributeExists(confCommon.getProperty("json_tid")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_error")))
                .andExpect(model().attributeExists(confCommon.getProperty("json_message")))
                .andExpect(model().attributeExists("apps"))
                .andReturn().getModelAndView();

        // 앱 하나를 지웠기 때문에 apps 의 요소는 하나만 존재함(app_srl, app_name 페어)
        Map<Integer, String> deleteElement = (Map<Integer, String>) mav3.getModel().get("apps");
        assertThat(deleteElement.size(), is(1));
        assertThat(deleteElement.containsKey(appSrl), is(true));
        assertThat(StringUtils.equals(deleteElement.get(appSrl), nextAppName), is(true));
    }
*/

    /**
     * 앱 리스트 폼 테스트
     */
/*
    @Test
    @Rollback
    public void listAppFormTest() throws Exception {
        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(get("/admin/app/list/{enabled}", MDV.YES)
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

        // 앱 추가하는 submit 페이지에 정상 접속 테스트(return json)
        ModelAndView mav = mockMvc.perform(get("/admin/app/list/{enabled}", MDV.YES)
                .session(session))
                // print 가 제대로 작동 하려면 compile 시에 servlet-api version 3.x 대를 사용해야 한다.
                // testCompile 이 아니라 정상적인 compile 시에 3.x 를 사용해야 함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("f_admin/app/list_app"))
                .andExpect(model().attributeExists("enabled"))
                .andExpect(model().attributeExists("defEnabled"))
                .andExpect(model().attributeExists("nouse"))
                .andReturn().getModelAndView();

        assertThat(Integer.parseInt(mav.getModel().get("enabled").toString(), 10), is(MDV.YES));
        assertThat(Integer.parseInt(mav.getModel().get("defEnabled").toString(), 10), is(MDV.YES));
        assertThat(Integer.parseInt(mav.getModel().get("nouse").toString(), 10), is(MDV.NUSE));
    }
*/

    /**
     * jquery datatable 용 json 을 넘기는 앱 리스트 api 테스트
     * @throws Exception
     */
/*
    @Test
    @Rollback
    public void listAppTest() throws Exception {
        // spring security 를 통과 하지 않으면 페이지가 보이지 않고 로그인 페이지로 이동 한다.
        mockMvc.perform(post("/admin/app/list/{enabled}/t/{tid}", MDV.YES, DateTime.now().getMillis())
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                .contentType(MediaType.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8"))
                .param("draw", "1")
                .param("columns[0][data]", "")
                .param("columns[0][name]", "checked")
                .param("columns[0][searchable]", "false")
                .param("columns[0][orderable]", "false")
                .param("columns[0][search][value]", "")
                .param("columns[0][search][regex]", "false")
                .param("columns[1][data]", "app_srl")
                .param("columns[1][name]", "app_srl")
                .param("columns[1][searchable]", "false")
                .param("columns[1][orderable]", "true")
                .param("columns[1][search][value]", "")
                .param("columns[1][search][regex]", "false")
                .param("columns[2][data]", "app_name")
                .param("columns[2][name]", "app_name")
                .param("columns[2][searchable]", "true")
                .param("columns[2][orderable]", "true")
                .param("columns[2][search][value]", "")
                .param("columns[2][search][regex]", "false")
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

        // 앱 리스트 정상 접속 테스트(return json)
        mockMvc.perform(post("/admin/app/list/{enabled}/t/{tid}", MDV.YES, DateTime.now().getMillis())
                    .session(session)
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
                    .contentType(MediaType.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8"))
                    .param("draw", "1")
                    .param("columns[0][data]", "")
                    .param("columns[0][name]", "checked")
                    .param("columns[0][searchable]", "false")
                    .param("columns[0][orderable]", "false")
                    .param("columns[0][search][value]", "")
                    .param("columns[0][search][regex]", "false")
                    .param("columns[1][data]", "app_srl")
                    .param("columns[1][name]", "app_srl")
                    .param("columns[1][searchable]", "false")
                    .param("columns[1][orderable]", "true")
                    .param("columns[1][search][value]", "")
                    .param("columns[1][search][regex]", "false")
                    .param("columns[2][data]", "app_name")
                    .param("columns[2][name]", "app_name")
                    .param("columns[2][searchable]", "true")
                    .param("columns[2][orderable]", "true")
                    .param("columns[2][search][value]", "")
                    .param("columns[2][search][regex]", "false")
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
