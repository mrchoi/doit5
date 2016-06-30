package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.board.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Properties;

/**
 * Created by kas0610 on 16. 1. 15.
 */
@Controller
@RequestMapping("/user/mypage")
public class MypageController {
    private final static Logger LOG = LoggerFactory.getLogger(MypageController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confAdmin;


    @RequestMapping(value = "/progress/group_list", method = RequestMethod.GET)
    public String progressListForm(Model model) {


        return "f_service/plymind/mypage/progress_group_list";
    }

    /**
     * 현재 진행 중인 상담
     *
     * @param groupSrl 상담 신청 그룹 srl
     * @param model model object
     * @return view name
     */
    @RequestMapping(value = "/progress/group/{group_srl}", method = RequestMethod.GET)
    public String progressListForm2(@PathVariable("group_srl") long groupSrl,
                                 Model model) {

        return "f_service/plymind/mypage/progress_list";
    }

    @RequestMapping(value = "/complete_list", method = RequestMethod.GET)
    public String completeListForm(Model model) {


        return "f_service/plymind/mypage/complete_list";
    }

    @RequestMapping(value = "/complete/{srl}", method = RequestMethod.GET)
    public String detailComplete(@PathVariable("srl") long srl,
                                    Model model) {

        return "f_service/plymind/mypage/complete_detail";
    }



}