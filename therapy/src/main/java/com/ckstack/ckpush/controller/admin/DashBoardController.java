package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.security.CkUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Properties;

/**
 * Created by dhkim94 on 15. 4. 1..
 */
@Controller
@RequestMapping("/admin")
public class DashBoardController {
    private final static Logger LOG = LoggerFactory.getLogger(DashBoardController.class);

    @Autowired
    protected Properties confCommon;

    /**
     * 로그인 완료 후 어드민 페이지에 접근 한다. 웹페이지의 layout 을 설정 한다.
     * @param model Model object
     * @return 어드민 layout 을 가지고 있는 view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String welcom(Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userId", userDetails.getUser_id());

        // 사용자 프로필 이미지 URL을 넣어야 한다.
        String profileImageURL = null;
        if(userDetails.getProfile_thumb_url() == null) {
            if(userDetails.getProfile_url() != null) profileImageURL = userDetails.getProfile_url();
        } else {
            profileImageURL = userDetails.getProfile_thumb_url();
        }
        if(profileImageURL != null) model.addAttribute("userProfileImage", profileImageURL);

        String backOfficeType = "";
        if(confCommon.containsKey("backoffice_by_member_support")) {
            backOfficeType = confCommon.getProperty("backoffice_by_member_support");
        }
        model.addAttribute("backOfficeType", backOfficeType);

        //System.out.println("welcom page");

        return "f_admin/dash/welcom";
    }

    @RequestMapping(value = "/dash", method = RequestMethod.GET)
    public String overallDashBoard() {

        // TODO 로직 완료 되고 난 다음 overall dash board 에서 보여 줄 것을 고민 해 봐야 한다.

        // TODO 테스트 하고 난 다음 삭제 해야 한다.
        System.out.println("overall page");

        return "f_admin/dash/overall";
    }



}
