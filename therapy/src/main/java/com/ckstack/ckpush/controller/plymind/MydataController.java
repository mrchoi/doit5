package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.data.request.KeyBean;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.user.GroupEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.domain.user.MemberExtraEntity;
import com.ckstack.ckpush.service.board.DocumentService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * Created by je7646 on 16. 2. 16.
 */
@Controller
@RequestMapping("/user/mydata")
public class MydataController {
    private final static Logger LOG = LoggerFactory.getLogger(MydataController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private DocumentService documentService;

    /**
     * 회원 정보
     *
     * @return 회원 정보
     */
    @RequestMapping(value = "/modify")
    public String register(Model model) throws IOException {

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        MemberEntity memberEntity = memberService.getMemberInfo(userDetails.getUser_id());

        String user_group_name = "";
        if(memberEntity.getMemberExtraEntity().getUser_group_srl() > 0){
            DocumentEntity documentEntities = documentService.getDocument(memberEntity.getMemberExtraEntity().getUser_group_srl());
            user_group_name = documentEntities.getDocument_title();
        }

        if(memberEntity == null) {
            LOG.info("failed login. not found user_id["+userDetails.getUser_id()+"]");
            throw new UsernameNotFoundException("not found user_id["+userDetails.getUser_id()+"]");
        }
        String allow_mailing = "";
        String allow_message = "";
        if(memberEntity.getAllow_mailing() == 1){
            allow_mailing = "checked";
        }
        if(memberEntity.getAllow_message() == 1){
            allow_message = "checked";
        }


        model.addAttribute("member_srl", userDetails.getMember_srl());
        model.addAttribute("user_id", memberEntity.getUser_id());
        model.addAttribute("email_address", memberEntity.getEmail_address());
        model.addAttribute("user_password", memberEntity.getUser_password());
        model.addAttribute("user_name", memberEntity.getUser_name());
        model.addAttribute("nick_name", memberEntity.getNick_name());
        model.addAttribute("group_gubun", memberEntity.getMemberExtraEntity().getGroup_gubun());
        model.addAttribute("user_group_name", user_group_name);
        model.addAttribute("user_group_srl", memberEntity.getMemberExtraEntity().getUser_group_srl());

        model.addAttribute("mobile_phone_number", memberEntity.getMobile_phone_number());
        model.addAttribute("allow_mailing", allow_mailing);
        model.addAttribute("allow_message", allow_message);


        return "f_service/open/modify";
    }

    /**
     * 회원 정보 수정
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param memberEntity 수정될 회원정보 항목
     * @param bindingResult bindingResult object
     * @return 수정 결과
     */
    @RequestMapping(value = "/modify/t/{tid}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addMemberSubmit(HttpServletRequest request,
                                               @PathVariable("tid") String tid,
                                               @Valid @RequestBody MemberEntity memberEntity,
                                               BindingResult bindingResult) {


        LOG.info("member_srl = " + memberEntity.getMember_srl());
        LOG.info("group_srl["+memberEntity.getUser_group_srl()+"]");
        LOG.info("group_gubun["+memberEntity.getGroup_gubun()+"]");

        Map<String, String> memberData = new HashMap<>();
        memberData.put("nick_name", memberEntity.getNick_name());
        memberData.put("email_address", memberEntity.getEmail_address());
        memberData.put("allow_mailing", String.valueOf(memberEntity.getAllow_mailing()));
        memberData.put("mobile_phone_number", memberEntity.getMobile_phone_number());
        memberData.put("allow_message", String.valueOf(memberEntity.getAllow_message()));

        memberService.modifyMember((int) memberEntity.getMember_srl(), memberData);

        String group_gubun = String.valueOf(memberEntity.getGroup_gubun());
        String group_srl = String.valueOf(memberEntity.getUser_group_srl());

        Map<String, String> memberExtraData = new HashMap<>();
        memberExtraData.put("group_gubun", group_gubun);
        memberExtraData.put("user_group_srl", group_srl);

        memberService.modifyMemberExtra((int) memberEntity.getMember_srl(), memberExtraData);

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", "S000001");

        return map;
    }

    /**
     * 심리평가 기초 자료 항목
     *
     * @param memberSrl memberSrl
     * @return memberSrl의 심리평가 기초자료 정보
     */
    @RequestMapping(value = "/modify/register_optional")
    public String registerOptional(Model model, @RequestParam(value="num") long memberSrl) throws IOException {

        LOG.info("try to login using user_id[" + memberSrl + "]");
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        MemberExtraEntity memberextraEntity = memberService.getMemberextraInfo(memberSrl);
        if(memberextraEntity == null) {
            LOG.info("failed login. not found memberSrl["+memberSrl+"]");
            throw new UsernameNotFoundException("not found memberSrl["+memberSrl+"]");
        }

        int year = 0;
        int month = 0;
        int date = 0;
        String job = "";
        String children = "";
        String religion_name = "";
        String disability_type = "";
        String disability_rate = "";

        if(!(memberextraEntity.getBirthday() == null || memberextraEntity.getBirthday().equals("")) && memberextraEntity.getBirthday().length() == 8) {
            year = Integer.parseInt(memberextraEntity.getBirthday().substring(0, 4));
            month = Integer.parseInt(memberextraEntity.getBirthday().substring(4, 6));
            date = Integer.parseInt(memberextraEntity.getBirthday().substring(6, 8));
        }
        if(!(memberextraEntity.getJob() == null || memberextraEntity.getJob().equals(""))) {
            job = memberextraEntity.getJob();
        }
        if(!(memberextraEntity.getChildren() == null || memberextraEntity.getChildren().equals(""))) {
            children = memberextraEntity.getChildren();
        }
        if(!(memberextraEntity.getReligion_name() == null || memberextraEntity.getReligion_name().equals(""))) {
            religion_name = memberextraEntity.getReligion_name();
        }
        if(!(memberextraEntity.getDisability_type() == null || memberextraEntity.getDisability_type().equals(""))) {
            disability_type = memberextraEntity.getDisability_type();
        }
        if(!(memberextraEntity.getDisability_rate() == null || memberextraEntity.getDisability_rate().equals(""))) {
            disability_rate = memberextraEntity.getDisability_rate();
        }

        LOG.info("["+year+"]"+month+","+date+",ac="+memberextraEntity.getAcademic_srl());

        model.addAttribute("user_id", userDetails.getUser_id());
        model.addAttribute("member_srl", memberextraEntity.getMember_srl());
        model.addAttribute("birthday", memberextraEntity.getBirthday());
        model.addAttribute("job", job);
        model.addAttribute("marriage", memberextraEntity.getMarriage());
        model.addAttribute("children", children);
        model.addAttribute("religion", memberextraEntity.getReligion());
        model.addAttribute("religion_name", religion_name);
        model.addAttribute("disability", memberextraEntity.getDisability());
        model.addAttribute("disability_type", disability_type);
        model.addAttribute("disability_rate", disability_rate);
        model.addAttribute("academic_srl", memberextraEntity.getAcademic_srl());
        model.addAttribute("gender", memberextraEntity.getGender());
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("date", date);

        return "f_service/open/modify_optional";
    }

    /**
     * 패스워드 수정 화면
     *
     * @return 수정 결과
     */
    @RequestMapping(value = "/modify/pass")
    public String password(Model model) throws IOException {

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("member_srl", userDetails.getMember_srl());
        model.addAttribute("user_id", userDetails.getUser_id());
        model.addAttribute("user_password", userDetails.getUser_password());

        return "f_service/open/password";
    }

    /**
     * 패스워드 수정
     *
     * @param request HttpServletRequest object
     * @param memberEntity 수정될 회원정보 항목
     * @param bindingResult bindingResult object
     * @return 수정 결과
     */
    @RequestMapping(value = "/modify/passmodify", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyPassword(HttpServletRequest request,
                                           @Valid @RequestBody MemberEntity memberEntity,
                                           BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        LOG.info("====================new_password = ["+memberEntity.getUser_password()+"]");
        LOG.info("====================old_password = ["+memberEntity.getOld_password()+"]");
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean m = memberService.getmatches(memberEntity.getOld_password(), userDetails.getUser_password());
        LOG.info("====================password_matches = [" + m + "]");

        Map<String, String> memberData = new HashMap<>();
        memberData.put("user_password", memberEntity.getUser_password());

        if(m){
            memberService.modifyMember((int) memberEntity.getMember_srl(), memberData);
            map.put("ck_error", "S000001");
            return map;
        }else{
            map.put("ck_error", "S000088");
        }

        return map;
    }


    /**
     * 회원 탈퇴페이
     *
     * @return 회원 정보
     */
    @RequestMapping(value = "/drop")
    public String userDrop(Model model) throws IOException {

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        MemberEntity memberEntity = memberService.getMemberInfo(userDetails.getUser_id());
        if(memberEntity == null) {
            LOG.info("failed login. not found user_id["+userDetails.getUser_id()+"]");
            throw new UsernameNotFoundException("not found user_id["+userDetails.getUser_id()+"]");
        }


        model.addAttribute("member_srl", userDetails.getMember_srl());
        model.addAttribute("user_id", userDetails.getUser_id());
        model.addAttribute("user_password", userDetails.getUser_password());
        model.addAttribute("user_name", userDetails.getUser_name());
        model.addAttribute("nick_name", userDetails.getNick_name());

        return "f_service/open/drop";
    }

    /**
     * 회원 탈퇴
     *
     * @param request HttpServletRequest object
     * @param memberEntity 탈퇴될 회원정보 항목
     * @param bindingResult bindingResult object
     * @return 수정 결과
     */
    @RequestMapping(value = "/drop/signOut", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> userSignOut(HttpServletRequest request,
                                               @Valid @RequestBody MemberEntity memberEntity,
                                               BindingResult bindingResult) {

        LOG.info("failed login. not found memberSrl["+memberEntity.getUser_id()+"]");

        memberService.signOutMember(memberEntity.getUser_id());

        Map<String, Object> map = new HashMap<>();
        map.put("ck_error", "S000001");

        return map;
    }

}
