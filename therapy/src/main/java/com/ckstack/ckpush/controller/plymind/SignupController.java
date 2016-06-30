package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.user.GroupEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.domain.user.MemberExtraEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.plymind.SignupService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.*;

/**
 * Created by kodaji on 2016. 1. 27..
 */
@Controller
@RequestMapping("/signup/member")
public class SignupController {
    private final static Logger LOG = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    protected SignupService signupService;

    /**
     * 회원 가입시 아이디 중복확인한다.
     * @param request Model object
     *        userId 사용자 아이디
     * @return Map
     */
    @RequestMapping(value = "/idCheck", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> idCheck(HttpServletRequest request,
                                       @RequestParam("user_id") String userId) {

        Map<String, String> map = new HashMap<>();
        map.put("result", "0");

        try {
            MemberEntity memberEntity = memberService.getMemberInfo(userId);
            if (memberEntity != null && memberEntity.getUser_id() != null && memberEntity.getUser_id().equals(userId)) {
                map.put("result", "1");
            }
        }catch (Exception e) {
            LOG.info(e.getMessage());
        }

        return map;
    }

    /**
     * 회원 가입시 닉네임 중복확인한다.
     * @param request Model object
     *        userId 사용자 아이디
     * @return Map
     */
    @RequestMapping(value = "/nickNameCheck", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> nickNameCheck(HttpServletRequest request,
                                             @RequestParam("nick_name") String nickName) {

        Map<String, String> map = new HashMap<>();
        map.put("result", "0");

        try {
            long memberEntity = memberService.getMemberInfoNick(nickName);
            if (memberEntity > 0) {
                map.put("result", "2");
            }else{
                map.put("result", "3");
            }
        }catch (Exception e) {
            LOG.info(e.getMessage());
        }

        return map;
    }

    /**
     * 회원가입
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param memberEntity 추가할 사용자 정보
     * @param bindingResult bindingResult object
     * @return 사용자 추가한 결과
     */
    @RequestMapping(value = "/add/t/{tid}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addMemberSubmit(HttpServletRequest request,
                                        @PathVariable("tid") String tid,
                                        @Valid @RequestBody MemberEntity memberEntity,
                                        BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            // ajax 로 hashtag url 로 접근하여 페이지 교체 방식 이므로 에러 메시지를 view 로 전달 할 수 없다.
            // view 로 전달하려면 redirect 하지 않고, view 를 바로 보여야 하는데
            // 이러면 view frame 형태가 깨져 버린다. 로그만 찍자.
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

        // 일반 사용자 일때 app_srl 체크 및 group_srl 확인
        int groupSrl;
        if(memberEntity.getAccount_type() == MDV.USER_NORMAL) {
            if(memberEntity.getApp_srl() == null ||
                    StringUtils.equals(StringUtils.trim(memberEntity.getApp_srl()), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_srl", messageSource.getMessage("valid.app_srl", null,
                        LocaleContextHolder.getLocale()));
                throw new CustomException("add_member_error", reason);
            }

            groupSrl = MDV.APP_USER_ADVISOR;

            if (memberEntity.getApp_account_type() == MDV.APP_USER_NORMAL) {
                GroupEntity groupEntity = memberService.getGroup(confCommon.getProperty("group_role_user"));
                groupSrl = groupEntity.getGroup_srl();
            }

        } else if(memberEntity.getAccount_type() == MDV.USER_ROOT) {
            GroupEntity groupEntity = memberService.getGroup(confCommon.getProperty("group_role_root"));
            groupSrl = groupEntity.getGroup_srl();

        } else {
            Map<String, String> reason = new HashMap<>();
            reason.put("account_type", messageSource.getMessage("valid.account_type", null,
                    LocaleContextHolder.getLocale()));
            throw new CustomException("add_member_error", reason);
        }

        //CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberService.signUp(memberEntity, MDV.NUSE, null, groupSrl, 1,
                webUtilService.getRequestIp(request));

        /*
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("member_srl", memberEntity.getMember_srl());

        return mav;
        */

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", "S000001");
        return map;
    }

    /**
     * 심리평가 기초자료 항목 수정
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param memberExtraEntity 추가할 심리평가 기초자료 항목
     * @param bindingResult bindingResult object
     * @return 사용자 추가한 결과
     */
    @RequestMapping(value = "/modify/extra/t/{tid}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addMemberSubmit(HttpServletRequest request,
                                               @PathVariable("tid") String tid,
                                               @Valid @RequestBody MemberExtraEntity memberExtraEntity,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // ajax 로 hashtag url 로 접근하여 페이지 교체 방식 이므로 에러 메시지를 view 로 전달 할 수 없다.
            // view 로 전달하려면 redirect 하지 않고, view 를 바로 보여야 하는데
            // 이러면 view frame 형태가 깨져 버린다. 로그만 찍자.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> reason = new HashMap<>();

            for (FieldError fieldError : fieldErrors) {
                reason.put(fieldError.getField(), fieldError.getDefaultMessage());
                LOG.warn("invalid field. field name[" + fieldError.getField() +
                        "], message[" + fieldError.getDefaultMessage() + "]");
            }

            // NOTE 400 을 리턴 시키면 jQuery 에서 cors 가 발생 한다.
            throw new CustomException("add_member_error", reason);
        }

        LOG.info("member_srl = " + memberExtraEntity.getMember_srl());
        LOG.info("birthday = " + memberExtraEntity.getBirthday());

        memberService.modifyExtraMember(memberExtraEntity);

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", "S000001");

        return map;
    }


    @RequestMapping(value="/search/userId/{tid}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchUserId(Model model,
                                            @PathVariable("tid") String tid,
                                            @RequestParam(value="user_name") String userName,
                                            @RequestParam(value="email_address") String emailAddress) throws Exception {

        List<String> userIds = signupService.searchUserId(userName, emailAddress);

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", "S000001");
        map.put("count", "" + userIds.size());
        map.put("user_id", userIds);

        return map;
    }

    @RequestMapping(value="/email/userId/{tid}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> emailUserId(Model model,
                                            @PathVariable("tid") String tid,
                                            @RequestParam(value="user_name") String userName,
                                            @RequestParam(value="email_address") String emailAddress) throws Exception {

        List<String> userIds = signupService.searchUserId(userName, emailAddress);
        int result = signupService.emailUserId(userName, emailAddress, userIds);

        String ckError = "S000001";
        if (result != 1) ckError = "E000001";

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", ckError);

        return map;
    }

    @RequestMapping(value="/passwd/{tid}")
    @ResponseBody
    public Map<String, Object> tempPasswordSend(Model model,
                                @PathVariable("tid") String tid,
                                @RequestParam(value="user_id") String userId,
                                @RequestParam(value="user_name") String userName,
                                @RequestParam(value="email_address") String emailAddress) throws Exception {

        long memberSrl = signupService.isCorrectMember(userId, userName, emailAddress);
        LOG.info("member_srl ===> " + memberSrl);
        int result = 0;

        if (memberSrl > 0) {
            String tempPassword = signupService.createTempPassword(memberSrl);
            LOG.info("tempPassword ===> " + tempPassword);

            result = signupService.sendTempPassword(userId, userName, tempPassword, emailAddress);
        }

        String ckError = "S000001";
        if (result != 1) ckError = "E000001";

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", ckError);

        return map;
    }
}
