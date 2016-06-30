package com.ckstack.ckpush.controller.api.subscribe;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.data.request.SignInBean;
import com.ckstack.ckpush.data.request.SignUpBean;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.GroupEntity;
import com.ckstack.ckpush.domain.user.MemberAccessTokenEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.AuthService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by dhkim94 on 15. 7. 9..
 */
@Controller
@RequestMapping("/api/open")
public class ApiOpenController {
    private final static Logger LOG = LoggerFactory.getLogger(ApiOpenController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private AppService appService;
    @Autowired
    private AuthService authService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Resource(name = "passwordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * exception 방식으로 처리 하지 못하는 에러에 대해 에러 결과를 강제로 보여 준다.
     *
     * @param request HttpServletRequest object
     * @return Model and View object
     */
    @RequestMapping(value = "/show/error")
    public ModelAndView showError(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        String tid = (String) request.getAttribute(confCommon.getProperty("json_tid"));
        String errCode = (String) request.getAttribute("error_code");

        webUtilService.setCommonApiResponse(mav, tid, errCode);

        mav.addObject(confCommon.getProperty("json_method"),
                request.getAttribute(confCommon.getProperty("json_method")));
        mav.addObject(confCommon.getProperty("json_request_uri"),
                request.getAttribute(confCommon.getProperty("json_request_uri")));

        Object reason = request.getAttribute(confCommon.getProperty("json_reason"));

        if(reason == null)
            mav.addObject(confCommon.getProperty("json_reason"), new HashMap<String, String>());
        else
            mav.addObject(confCommon.getProperty("json_reason"), reason);

        return mav;
    }

    /**
     * 신규 회원을 가입 시킨다. 총 4가지 기능을 한번에 제공 한다.
     *
     * 1. 가입
     * 2. 단말기 등록
     * 3. Push ID 등록
     * 4. 접속 토큰 발급
     *
     * @param response HttpServletResponse object
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param apiKey 가입 하려는 앱의 api_key
     * @param apiSecret 가입 하려는 앱의 api_secret
     * @param socialType 계정의 종류
     * @param signUpBean 가입 하는 사용자 정보
     * @param bindingResult BindingResult object
     * @return model and view object
     */
    @RequestMapping(value = "/signup/t/{tid}", method = RequestMethod.POST)
    public ModelAndView signUp(HttpServletResponse response,
                               HttpServletRequest request,
                               @PathVariable("tid") String tid,
                               @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
                               @RequestHeader(value = "X-Api-Secret", required = false) String apiSecret,
                               @RequestHeader(value = "X-Social-Type", required = false) String socialType,
                               @Valid @RequestBody SignUpBean signUpBean,
                               BindingResult bindingResult) {
        if(apiKey == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " not found X-Api-Key header. apiKey is null.");
            throw new CustomException("add_member_error", reason);
        }

        apiKey = StringUtils.trim(apiKey);
        if(StringUtils.equals(apiKey, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " invalid X-Api-Key header. apiKey is empty string.");
            throw new CustomException("add_member_error", reason);
        }

        if(apiSecret == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H3)");
            LOG.error(reason.get("invalid") + " not found X-Api-Secret header. apiSecret is null.");
            throw new CustomException("add_member_error", reason);
        }

        apiSecret = StringUtils.trim(apiSecret);
        if(StringUtils.equals(apiSecret, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H4)");
            LOG.error(reason.get("invalid") + " invalid X-Api-Secret header. apiSecret is empty string.");
            throw new CustomException("add_member_error", reason);
        }

        if(socialType == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H7)");
            LOG.error(reason.get("invalid") + " not found X-Social-Type header. socialType is null.");
            throw new CustomException("add_member_error", reason);
        }

        socialType = StringUtils.trim(socialType);
        if(StringUtils.equals(socialType, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H8)");
            LOG.error(reason.get("invalid") + " invalid X-Social-Type header. socialType is empty string.");
            throw new CustomException("add_member_error", reason);
        }

        String socialHeader = webUtilService.getSocialHeader(socialType);
        if(socialHeader == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H9)");
            LOG.error(reason.get("invalid") + " invalid X-Social-Type header. socialType [" +
                    socialType + "]");
            throw new CustomException("add_member_error", reason);
        }

        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for(FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name["+fieldError.getField()+
                        "], message["+fieldError.getDefaultMessage()+"]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("add_member_error", reason);
        }

        // 만일 소셜을 통한 후 가입 하는 것이라면 아이디를 변경 한다.
        signUpBean.setUser_id(socialHeader + signUpBean.getUser_id());

        // 사용자 가입 시키려는 앱 정보를 찾는다.
        AppEntity appEntity = appService.getAppInfo(apiKey);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H5)");
            LOG.error(reason.get("invalid") + " not found app by api_key [" + apiKey + "]");
            throw new CustomException("add_member_error", reason);
        }

        if(!StringUtils.equals(apiSecret, appEntity.getApi_secret())) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H6)");
            LOG.error(reason.get("invalid") + " invalid api_secret [" + apiSecret + "] by api_key [" + apiKey + "]");
            throw new CustomException("add_member_error", reason);
        }

        // 활성된 앱 인지 체크 한다.
        if(appEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H11)");
            LOG.error(reason.get("invalid") + " disabled app. enabled [" + appEntity.getEnabled() + "]");
            throw new CustomException("add_member_error", reason);
        }

        if(signUpBean.getMobile_phone_number() != null)
            signUpBean.setMobile_phone_number(StringUtils.trim(signUpBean.getMobile_phone_number()));
        else
            signUpBean.setMobile_phone_number("");

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUser_id(signUpBean.getUser_id());
        memberEntity.setEmail_address(signUpBean.getEmail_address() == null ? "" : signUpBean.getEmail_address());
        memberEntity.setUser_password(signUpBean.getUser_password());
        memberEntity.setUser_name(signUpBean.getUser_name());
        memberEntity.setNick_name(signUpBean.getNick_name() == null ? signUpBean.getUser_name() : signUpBean.getNick_name());
        memberEntity.setMobile_phone_number(signUpBean.getMobile_phone_number());
        memberEntity.setAccount_type(MDV.USER_NORMAL);
        memberEntity.setApp_srl(Integer.toString(appEntity.getApp_srl()));
        // 앱에 일반 사용자로 가입 시킨다.
        memberEntity.setApp_account_type(MDV.APP_USER_NORMAL);

        GroupEntity groupEntity = memberService.getGroup(confCommon.getProperty("group_role_user"));
        int groupSrl = groupEntity.getGroup_srl();

        long fileSrl = signUpBean.getProfile_file_srl();
        String fileURL = signUpBean.getProfile_file_url();
        if(fileSrl <= 0) fileURL = null;

        // 1. 사용자를 가입 시킨다.
        memberService.signUp(memberEntity, fileSrl, fileURL, groupSrl, MDV.NONE,
                webUtilService.getRequestIp(request));

        // 2. 사용자의 단말을 등록 한다.
        if(signUpBean.getDevice_id() != null)
            signUpBean.setDevice_id(StringUtils.trim(signUpBean.getDevice_id()));
        else
            signUpBean.setDevice_id("");

        // 지원하는 단말기 타입을 확인
        String[] arrSupportTerminal = StringUtils.split(confCommon.getProperty("user_terminal_support"), ",");
        List<Integer> supportTerminal = new ArrayList<>();

        assert arrSupportTerminal != null;
        for (String element : arrSupportTerminal) {
            String[] values = StringUtils.split(element, ":");

            if (values.length < 2) {
                LOG.warn("invalid support terminal define. value[" + element + "]");
                continue;
            }

            supportTerminal.add(Integer.parseInt(StringUtils.trim(values[0]), 10));
        }

        if(!StringUtils.equals(signUpBean.getDevice_id(), "") && supportTerminal.contains(signUpBean.getType())) {
            DeviceEntity deviceEntity = new DeviceEntity();
            deviceEntity.setDevice_id(signUpBean.getDevice_id());
            deviceEntity.setDevice_type(signUpBean.getModel());
            deviceEntity.setDevice_class(signUpBean.getType());
            deviceEntity.setOs_version(signUpBean.getOs_version());
            deviceEntity.setMobile_phone_number(signUpBean.getMobile_phone_number());

            appService.joinDeviceInApp(deviceEntity, appEntity.getApp_srl(), signUpBean.getPush_id());
        }

        // 3. access_token을 발급 한다.
        MemberAccessTokenEntity memberAccessTokenEntity = authService.createAccessTokenUsingMember(null,
                memberEntity.getMember_srl(), appEntity.getApp_srl());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("member_srl", memberEntity.getMember_srl());
        mav.addObject("access_token", memberAccessTokenEntity.getAccess_token());
        mav.addObject("access_token_expire", Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10));

        response.setHeader(confCommon.getProperty("access_token_header_name"),
                memberAccessTokenEntity.getAccess_token());
        response.setHeader(confCommon.getProperty("access_token_alive_header_name"),
                confCommon.getProperty("access_token_valid_sec"));

        return mav;
    }

    /**
     * 사용자의 접속 토큰을 발급 한다. 로그인 기능이라고 생각 하면 된다.
     *
     * @param response HttpServletResponse object
     * @param tid transaction id
     * @param apiKey 접속 토큰을 발급 하려는 사용자가 가입한 앱의 api key
     * @param apiSecret 접속 토큰을 발급 하려는 사용자가 가입한 앱의 api secret
     * @param socialType 접속 토큰 발급 하려는 사용자의 소셜 연동 가입 형태
     * @param signInBean 접속 토큰 발급 하는 사용자 정보
     * @param bindingResult BindingResult object
     * @return model and view object
     */
    @RequestMapping(value = "/signin/t/{tid}", method = RequestMethod.POST)
    public ModelAndView signIn(HttpServletResponse response,
                               @PathVariable("tid") String tid,
                               @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
                               @RequestHeader(value = "X-Api-Secret", required = false) String apiSecret,
                               @RequestHeader(value = "X-Social-Type", required = false) String socialType,
                               @Valid @RequestBody SignInBean signInBean,
                               BindingResult bindingResult) {
        if(apiKey == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H1)");
            LOG.error(reason.get("invalid") + " not found X-Api-Key header. apiKey is null.");
            throw new CustomException("create_access_token_error", reason);
        }

        apiKey = StringUtils.trim(apiKey);
        if(StringUtils.equals(apiKey, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " invalid X-Api-Key header. apiKey is empty string.");
            throw new CustomException("create_access_token_error", reason);
        }

        if(apiSecret == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H3)");
            LOG.error(reason.get("invalid") + " not found X-Api-Secret header. apiSecret is null.");
            throw new CustomException("create_access_token_error", reason);
        }

        apiSecret = StringUtils.trim(apiSecret);
        if(StringUtils.equals(apiSecret, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H4)");
            LOG.error(reason.get("invalid") + " invalid X-Api-Secret header. apiSecret is empty string.");
            throw new CustomException("create_access_token_error", reason);
        }

        if(socialType == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H7)");
            LOG.error(reason.get("invalid") + " not found X-Social-Type header. socialType is null.");
            throw new CustomException("create_access_token_error", reason);
        }

        socialType = StringUtils.trim(socialType);
        if(StringUtils.equals(socialType, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H8)");
            LOG.error(reason.get("invalid") + " invalid X-Social-Type header. socialType is empty string.");
            throw new CustomException("create_access_token_error", reason);
        }

        String socialHeader = webUtilService.getSocialHeader(socialType);
        if(socialHeader == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H9)");
            LOG.error(reason.get("invalid") + " invalid X-Social-Type header. socialType [" +
                    socialType + "]");
            throw new CustomException("create_access_token_error", reason);
        }

        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for(FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name["+fieldError.getField()+
                        "], message["+fieldError.getDefaultMessage()+"]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("create_access_token_error", reason);
        }

        // 만일 소셜을 통한 후 로그인 하는 것이라면 아이디를 변경 한다.
        signInBean.setUser_id(socialHeader + signInBean.getUser_id());

        // 로그인 하려는 사용자 앱 정보를 찾는다.
        AppEntity appEntity = appService.getAppInfo(apiKey);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H5)");
            LOG.error(reason.get("invalid") + " not found app by api_key [" + apiKey + "]");
            throw new CustomException("create_access_token_error", reason);
        }

        if(!StringUtils.equals(apiSecret, appEntity.getApi_secret())) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H6)");
            LOG.error(reason.get("invalid") + " invalid api_secret [" + apiSecret + "] by api_key [" + apiKey + "]");
            throw new CustomException("create_access_token_error", reason);
        }

        // 활성된 앱 인지 체크 한다.
        if(appEntity.getEnabled() != MDV.YES) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H11)");
            LOG.error(reason.get("invalid") + " disabled app. enabled [" + appEntity.getEnabled() + "]");
            throw new CustomException("create_access_token_error", reason);
        }

        // 사용자 확인
        MemberEntity memberEntity = memberService.getMemberInfo(signInBean.getUser_id());
        if(memberEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H10)");
            LOG.error(reason.get("invalid") + " not found user. user_id [" + signInBean.getUser_id() + "]");
            throw new CustomException("create_access_token_error", reason);
        }

        if(!passwordEncoder.matches(signInBean.getUser_password(), memberEntity.getUser_password())) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H12)");
            LOG.error(reason.get("invalid") + " wrong password. user_id [" + signInBean.getUser_id() + "]");
            throw new CustomException("create_access_token_error", reason);
        }

        // 접속 토큰 발급
        MemberAccessTokenEntity memberAccessTokenEntity = authService.createAccessTokenUsingMember(memberEntity,
                MDV.NUSE, appEntity.getApp_srl());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("member_srl", memberEntity.getMember_srl());
        mav.addObject("access_token", memberAccessTokenEntity.getAccess_token());
        mav.addObject("access_token_expire", Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10));

        // plymind 전용. 사용자 정보를 추가 한다.
        Map<String, Boolean> notiSetup = new HashMap<>();
        notiSetup.put("call", memberEntity.getMemberExtraEntity().getAllow_call() == MDV.YES);
        mav.addObject("noti", notiSetup);

        response.setHeader(confCommon.getProperty("access_token_header_name"),
                memberAccessTokenEntity.getAccess_token());
        response.setHeader(confCommon.getProperty("access_token_alive_header_name"),
                confCommon.getProperty("access_token_valid_sec"));

        return mav;
    }

    /**
     * 접속 토큰 시간을 연장 한다.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @param tid transaction id
     * @param accessToken 시간 연장 할 access_token
     * @return model and view
     */
    @RequestMapping(value = "/token/expire/up/t/{tid}", method = RequestMethod.GET)
    public ModelAndView continueAccessToken(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable("tid") String tid,
                                            @RequestHeader(value = "X-Access-Token", required = false) String accessToken) {
        if(accessToken == null || StringUtils.equals(StringUtils.trim(accessToken), "")) {
            // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/util/WebUtils.html
            // 문서 참고
            Cookie cookie = WebUtils.getCookie(request, confCommon.getProperty("access_token_header_name"));

            if(cookie == null) {
                Map<String, String> reason = new HashMap<>();
                reason.put("invalid", "invalid request(H1)");
                LOG.error(reason.get("invalid") + " not found X-Access-Token header or cookie. accessToken is null.");
                throw new CustomException("continuation_access_token_error", reason);
            }

            accessToken = StringUtils.trim(cookie.getValue());
        }

        MemberAccessTokenEntity memberAccessTokenEntity = authService.getAccessTokenUsingMember(accessToken);
        if(memberAccessTokenEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("invalid", "invalid request(H2)");
            LOG.error(reason.get("invalid") + " invalid access_token. accessToken [" + accessToken + "]");
            throw new CustomException("continuation_access_token_error", reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        int renewalSec = authService.renewalAccessTokenExpireUsingMember(accessToken, ltm);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("access_token_expire", Integer.parseInt(confCommon.getProperty("access_token_valid_sec"), 10));

        response.setHeader(confCommon.getProperty("access_token_alive_header_name"), Integer.toString(renewalSec));

        return mav;
    }

}
