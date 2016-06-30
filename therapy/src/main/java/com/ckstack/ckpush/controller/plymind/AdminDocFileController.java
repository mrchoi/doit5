package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.app.AppService;
import com.ckstack.ckpush.service.board.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by 0610 on 16. 2. 16.
 */
@Controller
@RequestMapping("/admin/docFile")
public class AdminDocFileController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminDocFileController.class);

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

    /**
     * 검사 문서 파일 리스트를 보여주는 폼을 출력 한다.
     *
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listForm(Model model) {


        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("mdv_public", MDV.PUBLIC);

        return "f_admin/plymind/docFile/file_list";
    }


    @RequestMapping(value = "/add/file", method = RequestMethod.GET)
    public String addForm(Model model) {

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);

        return "f_admin/plymind/docFile/file_add";
    }
}