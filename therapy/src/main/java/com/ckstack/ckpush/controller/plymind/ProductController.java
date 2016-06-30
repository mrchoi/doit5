package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.board.DocumentLinkEntity;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import com.ckstack.ckpush.domain.plymind.*;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.domain.user.ZipCodeEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.plymind.AppointmentService;
import com.ckstack.ckpush.service.plymind.ProductService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by wypark on 16. 1. 18.
 */
@Controller
@RequestMapping("/user/product")
public class ProductController {
    private final static Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    protected Properties confCommon;

    @Autowired
    protected Properties confSvc;

    @Autowired
    private WebUtilService webUtilService;

    @Autowired
    private MemberService memberService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected AppointmentService appointmentService;

    /**
     * 심리검사 신청 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/checkupAddForm/{product_srl}", method = RequestMethod.GET)
    public String checkupAddForm(@PathVariable("product_srl") long product_srl,
                                 Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /* 심리검사 정보 리스트 조회 START */
        List<ProductEntity> productEntities = productService.getProductList(Integer.parseInt(confSvc.getProperty("plymind.product.kind.checkup.srl")));
        /* 심리검사 정보 리스트 조회 END */

        boolean userInfoBool = productService.userInfoCheck(userDetails.getMember_srl());

        boolean pretestingBool = productService.pretestingCheck(userDetails.getMember_srl());

        Map<String, Object> checkMap = new HashMap<String, Object>();
        checkMap.put("checkUserInfo", userInfoBool);
        checkMap.put("checkPretesting", pretestingBool);

        model.addAttribute("product_srl", product_srl);
        model.addAttribute("productEntities", productEntities);

        model.addAttribute("checkMap", checkMap);

        return "f_service/plymind/product/checkup_add";
    }

    /**
     * 심리검사 신청 정보를 저장한다.
     * @return model and view
     */
    @RequestMapping(value = "/checkupAdd", method = RequestMethod.POST)
    public ModelAndView checkupAdd(HttpServletRequest request,
                                   @Valid @RequestBody ApplicationEntity applicationEntity) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int resultCode = productService.add(userDetails.getMember_srl(), applicationEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("resultCode", resultCode);

        return mav;
    }

    /**
     * 심리상담 신청 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/adviceAddForm/{contents_srl}/{advice_period}/{advice_type}/{advisor_type}", method = RequestMethod.GET)
    public String adviceAddForm(@PathVariable("contents_srl") long contents_srl,
                                @PathVariable("advice_period") int advice_period,
                                @PathVariable("advice_type") int advice_type,
                                @PathVariable("advisor_type") int advisor_type,
                                Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int kind_srl = Integer.parseInt(confSvc.getProperty("plymind.product.kind.advice.srl"));

        int advice_number = 0;
        if(contents_srl == 5) {
            advice_number = advice_type;
            advice_type = 0;
        }

        /* 컨텐츠 정보 리스트 조회 START */
        List<ProductEntity> contentsEntities = productService.getContentsList(kind_srl);
        /* 컨텐츠 정보 리스트 조회 END */

        /* 심리상담 정보 리스트 조회 START */
        List<ProductEntity> productEntities = productService.getProductList(kind_srl);
        /* 심리상담 정보 리스트 조회 END */

        /* 심리상담 정보 리스트 조회 START */
        List< MemberEntity > memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.advisor.srl")), null, null, MDV.NUSE, MDV.NUSE);
        /* 심리상담 정보 리스트 조회 END */

        List<MemberEntity> advisorEntities = new ArrayList<MemberEntity>();
        if(advisor_type > 0) {
            List<AdvicedomainEntity> advicedomainEntities = productService.getAdvicedomainList(MDV.NUSE, 2, advisor_type);

            for (AdvicedomainEntity advicedomainEntity : advicedomainEntities) {
                for (MemberEntity memberEntity : memberEntities) {
                    if (advicedomainEntity.getMember_srl() == memberEntity.getMember_srl()) {
                        advisorEntities.add(memberEntity);
                        break;
                    }
                }
            }
        } else {
            advisorEntities = memberEntities;
        }


        boolean userInfoBool = productService.userInfoCheck(userDetails.getMember_srl());

        boolean pretestingBool = productService.pretestingCheck(userDetails.getMember_srl());

        Map<String, Object> checkMap = new HashMap<String, Object>();
        checkMap.put("checkUserInfo", userInfoBool);
        checkMap.put("checkPretesting", pretestingBool);

        model.addAttribute("member_srl", userDetails.getMember_srl());

        model.addAttribute("contents_srl", contents_srl);
        model.addAttribute("advice_period", advice_period);
        model.addAttribute("advice_type", advice_type);
        model.addAttribute("advice_number", advice_number);
        model.addAttribute("advisor_type", advisor_type);

        model.addAttribute("contentsEntities", contentsEntities);
        model.addAttribute("productEntities", productEntities);
        model.addAttribute("memberEntities", advisorEntities);

        model.addAttribute("checkMap", checkMap);

        return "f_service/plymind/product/advice_add";
    }

    /**
     * 심리상담 신청 정보를 저장한다.
     * @return model and view
     */
    @RequestMapping(value = "/adviceAdd", method = RequestMethod.POST)
    public ModelAndView adviceAdd(HttpServletRequest request,
                                  @Valid @RequestBody ApplicationEntity applicationEntity) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int resultCode = productService.add(userDetails.getMember_srl(), applicationEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("resultCode", resultCode);

        return mav;
    }

    /**
     * 검사 및 상담을 위한 필수 정보 체크를 한다.
     * @return model and view
     */
    @RequestMapping(value = "/precheck", method = RequestMethod.POST)
    public ModelAndView userInfoCheck(HttpServletRequest request) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean userInfoBool = productService.userInfoCheck(userDetails.getMember_srl());

        boolean pretestingBool = productService.pretestingCheck(userDetails.getMember_srl());

        Map<String, Object> checkMap = new HashMap<String, Object>();
        checkMap.put("checkUserInfo", userInfoBool);
        checkMap.put("checkPretesting", pretestingBool);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        mav.addObject("checkMap", checkMap);

        return mav;
    }

    /**
     * 심리검사 신청 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/precheck_add", method = RequestMethod.GET)
    public String precheckAdd(Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean pretestingBool = productService.pretestingCheck(userDetails.getMember_srl());

        model.addAttribute("pretestingBool", pretestingBool);
        model.addAttribute("member_srl", userDetails.getMember_srl());
        model.addAttribute("user_id", userDetails.getUser_id());

        return "f_service/plymind/product/precheck_add";
    }

    @RequestMapping(value = "/member_search/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listAppForSelect2(@PathVariable("tid") String tid,
                                          @RequestParam("query") String query,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        List<Map<String, Object>> list = new ArrayList<>();

        query = StringUtils.trim(query);
        if(StringUtils.equals(query, "")) {
            mav.addObject(confCommon.getProperty("dtresp_total_row"), 0);
            mav.addObject("list", list);
            return mav;
        }
        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("user_id", query);

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("user_id", "asc");

        List<MemberEntity> memberEntities = memberService.getMemberListByGroup(Long.parseLong(confSvc.getProperty("member.group.user.srl")), searchFilter, null, MDV.NUSE, MDV.NUSE);

        for(MemberEntity memberEntity : memberEntities) {
            Map<String, Object> map = new HashMap<>();

            map.put("member_srl", memberEntity.getMember_srl());
            map.put("user_name", memberEntity.getUser_name());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), memberEntities.size());
        mav.addObject("list", list);

        return mav;
    }

    @RequestMapping(value = "/zipcode_search/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listzipcodeSelect2(@PathVariable("tid") String tid,
                                          @RequestParam("query") String query,
                                          @RequestParam("offset") int offset,
                                          @RequestParam("limit") int limit) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        List<Map<String, Object>> list = new ArrayList<>();

        query = StringUtils.trim(query);
        if(StringUtils.equals(query, "")) {
            mav.addObject(confCommon.getProperty("dtresp_total_row"), 0);
            mav.addObject("list", list);
            return mav;
        }

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("user_id", query);

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("user_id", "asc");

        List<ZipCodeEntity> zipCodeEntities = memberService.getZipcodeList(query);


        for(ZipCodeEntity zipCodeEntity : zipCodeEntities) {
            Map<String, Object> map = new HashMap<>();

            map.put("zipcode_srl", zipCodeEntity.getZipcode_srl());
            map.put("full", zipCodeEntity.getFull());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), zipCodeEntities.size());
        mav.addObject("list", list);

        return mav;
    }

    @RequestMapping(value = "/zipcode_search/jibun/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listzipcodejibunSelect2(@PathVariable("tid") String tid,
                                           @RequestParam("query") String query,
                                           @RequestParam("offset") int offset,
                                           @RequestParam("limit") int limit) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        List<Map<String, Object>> list = new ArrayList<>();

        query = StringUtils.trim(query);
        if(StringUtils.equals(query, "")) {
            mav.addObject(confCommon.getProperty("dtresp_total_row"), 0);
            mav.addObject("list", list);
            return mav;
        }

        Map<String, String> searchFilter = new HashMap<>();
        searchFilter.put("user_id", query);

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("user_id", "asc");

        List<ZipCodeEntity> zipCodeEntities = memberService.getZipcodejibunList(query);


        for(ZipCodeEntity zipCodeEntity : zipCodeEntities) {
            Map<String, Object> map = new HashMap<>();

            map.put("zipcode_srl", zipCodeEntity.getZipcode_srl());
            map.put("full", zipCodeEntity.getFull());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), zipCodeEntities.size());
        mav.addObject("list", list);

        return mav;
    }


    @RequestMapping(value = "/product_list", method = RequestMethod.GET)
    public String productListForm(Model model) {

        long member_srl = MDV.NONE;
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj instanceof  CkUserDetails) { // login
            CkUserDetails userDetails = (CkUserDetails) obj;
            LOG.info("[CkUserDetails] found member_srl" + userDetails.getMember_srl());

            member_srl = userDetails.getMember_srl();

        } else if (obj instanceof String) { // logout
            LOG.info("[CkUserDetails] not found member_srl");
        }

        model.addAttribute("member_srl", member_srl);

        return "f_service/plymind/product/product_list";
    }
}
