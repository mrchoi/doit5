package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.user.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by kodaji on 16. 3. 6.
 */
@Controller
@RequestMapping("/user/open/mobile")
public class MobileController {
    private final static Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;
    @Autowired
    protected Properties confAdmin;
    @Autowired
    protected MemberService memberService;

    @RequestMapping(value = "/company_introduce")
    public String company_introduce(Model model) throws IOException {

        return "f_service/mobile/company_introduce";
    }

    @RequestMapping(value = "/company_counsel", method = RequestMethod.GET)
    public String company_counsel(Model model) throws IOException {

        //MDV.APP_USER_ADVISOR == 상담사
        // long totalRows = memberService.countGroupMemberInfo(null, null, MDV.NUSE, MDV.NUSE, MDV.APP_USER_ADVISOR);

        List<MemberEntity> memberEntities = memberService.getGroupMemberList(null, null, null, MDV.NUSE, MDV.NUSE, MDV.APP_USER_ADVISOR,
                null, MDV.NUSE, MDV.NUSE);

        for(MemberEntity memberEntity : memberEntities) {
            MemberEntity memberInfo = memberService.getMemberInfo(memberEntity.getMember_srl());
            memberEntity.setMemberExtraEntity(memberInfo.getMemberExtraEntity());
        }

        model.addAttribute("mdv_public", MDV.PUBLIC);
        model.addAttribute("normal_category_type", MDV.NORMAL_CATEGORY);
        model.addAttribute("link_category_type", MDV.LINK_CATEGORY);

        model.addAttribute("mdv_nouse", MDV.NUSE);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_deny", MDV.DENY);
        model.addAttribute("category_srl_group", confAdmin.getProperty("plymind.group.category.srl"));

        model.addAttribute("image_server_host", confDym.getProperty("image_server_host"));
        model.addAttribute("document_attach_uri", confCommon.getProperty("document_attach_uri"));
        model.addAttribute("document_attach_download_uri", confCommon.getProperty("document_attach_download_uri"));

        model.addAttribute("memberEntities", memberEntities);

        return "f_service/mobile/company_counsel";
    }
}
