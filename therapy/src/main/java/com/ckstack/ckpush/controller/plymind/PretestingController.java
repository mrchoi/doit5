package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.plymind.PretestingEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.plymind.PretestingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wypark on 16. 1. 18.
 */
@Controller
@RequestMapping("/user/pretesting")
public class PretestingController {
    private final static Logger LOG = LoggerFactory.getLogger(PretestingController.class);

    @Autowired
    protected Properties confCommon;

    @Autowired
    private WebUtilService webUtilService;

    @Autowired
    protected PretestingService pretestingService;

    /**
     * 사전검사 등록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/addForm", method = RequestMethod.GET)
    public String addForm(Model model) {
        List<PretestingEntity> pretestingEntities = pretestingService.getPretestingList();

        List<PretestingEntity> questionEntities = new ArrayList<PretestingEntity>();
        List<PretestingEntity> kindEntities = new ArrayList<PretestingEntity>();

        long question_srl = 0;
        long kind_srl = 0;

        for(int i=0; i<pretestingEntities.size(); i++) {
            PretestingEntity pretestingEntity = (PretestingEntity) pretestingEntities.get(i);

            if (i == 0) {
                questionEntities.add(pretestingEntity);
                kindEntities.add(pretestingEntity);

                question_srl = pretestingEntity.getQuestion_srl();
                kind_srl = pretestingEntity.getKind_srl();
            } else {
                if(question_srl != pretestingEntity.getQuestion_srl()) {
                    questionEntities.add(pretestingEntity);

                    question_srl = pretestingEntity.getQuestion_srl();
                }

                if(kind_srl != pretestingEntity.getKind_srl()) {
                    kindEntities.add(pretestingEntity);

                    kind_srl = pretestingEntity.getKind_srl();
                }
            }
        }

        model.addAttribute("questionEntities", questionEntities);
        model.addAttribute("kindEntities", kindEntities);
        model.addAttribute("pretestingEntities", pretestingEntities);

        return "f_service/plymind/pretesting/add";
    }

    /**
     * 사전검사 정보를 저장한다.
     * @return model and view
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView add(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int c_date = pretestingService.add(userDetails.getMember_srl(), requestBody);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("c_date", c_date);

        return mav;
    }

    /**
     * 사전검사 결과를 통한 심리검사 추천 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/checkup/{c_date}", method = RequestMethod.GET)
    public String testing(@PathVariable("c_date") int c_date, Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> infoMap = pretestingService.getPretestingResult(userDetails.getMember_srl());

        model.addAttribute("nickName", userDetails.getNick_name());
        model.addAttribute("infoMap", infoMap);

        return "f_service/plymind/pretesting/checkup";
    }

    /**
     * 사전검사 결과를 통한 심리상담 추천 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/advice", method = RequestMethod.GET)
    public String advice(Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> infoMap = pretestingService.getPretestingResult(userDetails.getMember_srl());

        model.addAttribute("nickName", userDetails.getNick_name());
        model.addAttribute("infoMap", infoMap);

        return "f_service/plymind/pretesting/advice";
    }
}
