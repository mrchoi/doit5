package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.controller.admin.MineralController;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import com.ckstack.ckpush.domain.plymind.*;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.mineral.CkFileService;
import com.ckstack.ckpush.service.plymind.ApplicationService;
import com.ckstack.ckpush.service.plymind.AppointmentService;
import com.ckstack.ckpush.service.plymind.MyadviceService;
import com.ckstack.ckpush.service.plymind.ProductService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/mycheckup")
public class AdminMycheckupController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminMycheckupController.class);

    @Autowired
    protected Properties confDym;

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
    private AppointmentService appointmentService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private MyadviceService myadviceService;

    @Autowired
    private CkFileService ckFileService;

    @Autowired
    private MineralController mineralController;

    /**
     * 심리 검사 목록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String checkupList(Model model) {

        return "f_admin/plymind/mycheckup/checkup_list";
    }

    /**
     * 심리 검사 리스트를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView applicationGroupList(HttpServletRequest request,
                                             @PathVariable("tid") String tid) {

        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_payment_error", reason);
        }

        //sortValue.put("application_group", "DESC");

        List<MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<ApplicationEntity> applicationMemberEntities = applicationService.getApplicationMember(MDV.NUSE);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        List<Long> contents_srls = new ArrayList<Long>();
        contents_srls.add(Long.parseLong("6"));
        contents_srls.add(Long.parseLong("7"));
        contents_srls.add(Long.parseLong("8"));
        contents_srls.add(Long.parseLong("9"));

        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(MDV.APPLICATION_STATUS_READY);
        application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);
        //application_statues.add(MDV.APPLICATION_STATUS_COMPLETE);
        //application_statues.add(MDV.APPLICATION_STATUS_CANCLE);

        //상담사 로그인 시 처리부분
        Map<String, String> advisor = memberService.getAdvisorAuthority();
        long advisor_srl = MDV.NUSE;
        if(advisor.get("advisor").equals("true")) {
            advisor_srl = Long.valueOf(advisor.get("member_srl"));
        }

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = applicationService.countApplicationGroup(MDV.NUSE, advisor_srl, application_statues, contents_srls);
        long filterRows = applicationService.countSearchApplicationGroup(MDV.NUSE, advisor_srl, application_statues, contents_srls, searchFilter);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationGroupList(MDV.NUSE, advisor_srl, application_statues, contents_srls, searchFilter, sortValue, offset, limit);

        for (ApplicationEntity applicationEntity : applicationEntities) {
            Map<String, Object> row = new HashMap<>();

            ApplicationEntity tempapplicationEntity = applicationService.getApplication(applicationEntity.getApplication_srl());

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + applicationEntity.getApplication_srl());
            row.put("application_srl", applicationEntity.getApplication_srl());
            row.put("application_group", applicationEntity.getApplication_group());
            row.put("member_srl", applicationEntity.getMember_srl());
            row.put("advisor_srl", applicationEntity.getAdvisor_srl());
            row.put("product_srl", applicationEntity.getProduct_srl());
            row.put("advisor_name", "");
            row.put("application_status", tempapplicationEntity.getApplication_status());

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    row.put("user_name", memberEntity.getUser_name());
                    break;
                }
            }

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    row.put("advisor_name", memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (applicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                    row.put("kind", productEntity.getKind());
                    row.put("title", productEntity.getTitle());
                    row.put("advice_type", productEntity.getAdvice_type());
                    row.put("advice_period", productEntity.getAdvice_period());
                    row.put("advice_number", productEntity.getAdvice_number());
                    row.put("advice_price", productEntity.getAdvice_price());
                    break;
                }
            }

            List<Map<String, Object>> applicationMember = new ArrayList<Map<String, Object>>();
            for(ApplicationEntity applicationMemberentity : applicationMemberEntities) {
                if(applicationEntity.getApplication_group() == applicationMemberentity.getApplication_group()) {
                    for (MemberEntity memberEntity : memberEntities) {
                        if (applicationMemberentity.getMember_srl() == memberEntity.getMember_srl()) {
                            Map<String, Object> applicationMemberMap = new HashMap<String, Object>();
                            applicationMemberMap.put("member_srl", memberEntity.getMember_srl());
                            applicationMemberMap.put("user_name", memberEntity.getUser_name());

                            applicationMember.add(applicationMemberMap);
                            break;
                        }
                    }
                }
            }

            row.put("application_member", applicationMember);

            table.add(row);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject(confCommon.getProperty("dtresp_data"), table);
        mav.addObject(confCommon.getProperty("dtresp_draw"),
                Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_draw")), 10));
        mav.addObject(confCommon.getProperty("dtresp_total_row"), totalRows);
        mav.addObject(confCommon.getProperty("dtresp_filter_row"), filterRows);

        return mav;
    }

    /**
     * 심리 검사 상세보기 페이지
     * @return model and view
     */
    @RequestMapping(value = "/{member_srl}/{application_group}", method = RequestMethod.GET)
    public String checkupDetail(HttpServletRequest request,
                                @PathVariable("member_srl") long member_srl,
                                 @PathVariable("application_group") int application_group,
                                 Model model) {

        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(MDV.NUSE);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationListByGroup(member_srl, application_group, null, MDV.NUSE, MDV.NUSE);

        ApplicationEntity applicationEntity = new ApplicationEntity();

        boolean isCheck = false;
        boolean isAppointment = false;
        for (int i=0; i<applicationEntities.size(); i++) {
            ApplicationEntity tempApplicationEntity = applicationEntities.get(i);

            if(i != 0 && tempApplicationEntity.getApplication_status() == 1) {
                break;
            }

            applicationEntity = tempApplicationEntity;

            for (MemberEntity memberEntity : memberEntities) {
                if (tempApplicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    applicationEntity.setUser_name(memberEntity.getUser_name());
                    break;
                }
            }

            for (MemberEntity memberEntity : memberEntities) {
                if (tempApplicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    applicationEntity.setAdvisor_name(memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (tempApplicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
                    applicationEntity.setKind(productEntity.getKind());
                    applicationEntity.setContents_srl(productEntity.getContents_srl());
                    applicationEntity.setTitle(productEntity.getTitle());
                    applicationEntity.setAdvice_type(productEntity.getAdvice_type());
                    applicationEntity.setAdvice_period(productEntity.getAdvice_period());
                    applicationEntity.setAdvice_number(productEntity.getAdvice_number());
                    applicationEntity.setAdvice_price(productEntity.getAdvice_price());
                    break;
                }
            }

            for (AppointmentEntity appointmentEntity : appointmentEntities) {
                if (tempApplicationEntity.getApplication_srl() == appointmentEntity.getApplication_srl()) {
                    applicationEntity.setAppointment_date(appointmentEntity.getAppointment_date());
                    applicationEntity.setAppointment_time(appointmentEntity.getAppointment_time());
                }
            }

            if(applicationEntity.getApplication_status() == 1 || applicationEntity.getApplication_status() == 2) {
                break;
            }
        }

        List<PlymindDocumentEntity> plymindDocumentEntities = myadviceService.getPlymindDocument(applicationEntity.getApplication_srl(), application_group);
        List<PlymindDocumentEntity> tempplymindDocumentEntities = new ArrayList<>();

        for (PlymindDocumentEntity plymindDocumentEntity : plymindDocumentEntities)
        {
            PlymindDocumentEntity temp = new PlymindDocumentEntity();

            temp.setDocument_srl(plymindDocumentEntity.getDocument_srl());
            temp.setApplication_srl(plymindDocumentEntity.getApplication_srl());
            temp.setApplication_group(plymindDocumentEntity.getApplication_group());
            temp.setCheckup_file_srl(plymindDocumentEntity.getCheckup_file_srl());
            temp.setReply_file_srl(plymindDocumentEntity.getReply_file_srl());
            temp.setResult_file_srl(plymindDocumentEntity.getResult_file_srl());
            temp.setCheckup_date(plymindDocumentEntity.getCheckup_date());
            temp.setReply_date(plymindDocumentEntity.getReply_date());
            temp.setResult_date(plymindDocumentEntity.getResult_date());
            temp.setAdvisor_comment(plymindDocumentEntity.getAdvisor_comment());
            temp.setC_date(plymindDocumentEntity.getC_date());

            temp.setCheckup_file_name(this.getFileRepositoryEntity(plymindDocumentEntity.getCheckup_file_srl()));
            temp.setReply_file_name(this.getFileRepositoryEntity(plymindDocumentEntity.getReply_file_srl()));
            temp.setResult_file_name(this.getFileRepositoryEntity(plymindDocumentEntity.getResult_file_srl()));

            String checkupfileUrl = "";
            if(plymindDocumentEntity.getCheckup_file_srl() > 0){
                checkupfileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getCheckup_file_srl(), "repository_file_download_uri");

            }

            String replyfileUrl = "";
            if(plymindDocumentEntity.getReply_file_srl() > 0){
                replyfileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getReply_file_srl(), "repository_file_download_uri");

            }


            String resultfileUrl = "";
            if(plymindDocumentEntity.getResult_file_srl() > 0){
                resultfileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getResult_file_srl(), "repository_file_download_uri");

            }


            temp.setCheckup_file_url(checkupfileUrl);
            temp.setReply_file_url(replyfileUrl);
            temp.setResult_file_url(resultfileUrl);


            tempplymindDocumentEntities.add(temp);
        }


        model.addAttribute("applicationEntity", applicationEntity);
        model.addAttribute("plymindDocumentEntities", tempplymindDocumentEntities);

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);


        return "f_admin/plymind/mycheckup/checkup_detail";
    }

    private String getFileRepositoryEntity(long file_srl){

        if(file_srl <= 0) {
            return "";
        }

        FileRepositoryEntity fileRepositoryEntity = ckFileService.getFile(file_srl);

        return fileRepositoryEntity.getOrig_name();
    }

    /**
     * 심리 검사지 검색
     * @return model and view
     */
    @RequestMapping(value = "/select2/t/{tid}", method = RequestMethod.POST)
    public ModelAndView listCheckupForSelect2(@PathVariable("tid") String tid,
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
        searchFilter.put("orig_name", query);

        Map<String, String> sortValue = new LinkedHashMap<>();
        sortValue.put("orig_name", "asc");

        List<Integer> file_types = new ArrayList<Integer>();
        file_types.add(Integer.parseInt("1")); //  심리검사지
        file_types.add(Integer.parseInt("4")); // 사전 검사지
        file_types.add(Integer.parseInt("5")); // 사후 검사지

        //List<FileRepositoryEntity> fileRepositoryEntities = ckFileService.getFile(searchFilter, sortValue, MDV.NUSE, MDV.NUSE);
        List<FileRepositoryEntity> fileRepositoryEntities = ckFileService.getFilePlymind(searchFilter, file_types, sortValue, offset, limit);

        for(FileRepositoryEntity fileRepositoryEntity : fileRepositoryEntities) {
            Map<String, Object> map = new HashMap<>();

            map.put("file_srl", fileRepositoryEntity.getFile_srl());
            map.put("orig_name", fileRepositoryEntity.getOrig_name());

            list.add(map);
        }

        mav.addObject(confCommon.getProperty("dtresp_total_row"), fileRepositoryEntities.size());
        mav.addObject("list", list);

        return mav;
    }

    /**
     * 상담사 코멘트 등록 페이지로 이동
     * @return model and view
     */
    @RequestMapping(value = "/advisor_comment_add/{member_srl}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String advisorCommentAdd(@PathVariable("member_srl") long member_srl,
                                    @PathVariable("application_srl") long application_srl,
                                    @PathVariable("application_group") int application_group,
                                    Model model) {
        List< MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ApplicationEntity> applicationMemberEntities = applicationService.getApplicationMember(application_group);

        for (ApplicationEntity applicationMemberEntity : applicationMemberEntities) {
            for (MemberEntity memberEntity : memberEntities) {
                if (applicationMemberEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    applicationMemberEntity.setUser_name(memberEntity.getUser_name());
                    break;
                }
            }
        }

        model.addAttribute("member_srl", member_srl);
        model.addAttribute("application_srl", application_srl);
        model.addAttribute("application_group", application_group);
        model.addAttribute("applicationMemberEntities", applicationMemberEntities);

        return "f_admin/plymind/mycheckup/advisor_comment_add";
    }

    /**
     * 상담사 코멘트 등록 처리.
     * @return model and view
     */
    @RequestMapping(value = "/advisor_comment_add/t/{tid}", method = RequestMethod.POST)
    public ModelAndView commentAdd(HttpServletRequest request,
                                   @Valid @RequestBody PlymindDocumentEntity plymindDocumentEntity,
                                   @PathVariable("tid") String tid) {


        myadviceService.addPlymindDocument(plymindDocumentEntity);

        // 상태값 변경 처리
        applicationService.statusModify(plymindDocumentEntity.getApplication_srl(), plymindDocumentEntity.getApplication_group(), MDV.APPLICATION_STATUS_PROGRESS);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 심리 검사 수정 처리.
     * @return model and view
     */
    @RequestMapping(value = "/{document_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView checkupModify(HttpServletRequest request,
                                    //@Valid @RequestBody PlymindDocumentEntity plymindDocumentEntity,
                                      @Valid @RequestBody Map<String, Object> requestBody,
                                    @PathVariable("document_srl") long document_srl,
                                    @PathVariable("tid") String tid) {


        long application_srl = Long.parseLong(requestBody.get("application_srl").toString());
        int application_group = Integer.parseInt(requestBody.get("application_group").toString());
        long file_srl1 = Long.parseLong(requestBody.get("file_srl1").toString());
        String file_url1 = requestBody.get("file_url1").toString();
        String file_type = requestBody.get("file_type").toString();
        long checkup_file_srl = Long.parseLong(requestBody.get("checkup_file_srl").toString());
        //long reply_file_srl = Long.parseLong(requestBody.get("reply_file_srl").toString());
        long result_file_srl = Long.parseLong(requestBody.get("result_file_srl").toString());

        //repository 파일등록 ( 결과 파일 등록 )
        if(file_srl1 > 0 ) {
            mineralController.addFileSubmit(request, tid, requestBody);
            result_file_srl = file_srl1;

            // 상태값 변경 처리
            applicationService.statusModify(application_srl, application_group, MDV.APPLICATION_STATUS_COMPLETE);

        }

        //결과지 파일 등록 및 맵핑 변경
        PlymindDocumentEntity plymindDocumentEntity = new PlymindDocumentEntity();
        plymindDocumentEntity.setCheckup_file_srl(checkup_file_srl);
        //plymindDocumentEntity.setReply_file_srl(reply_file_srl);
        plymindDocumentEntity.setResult_file_srl(result_file_srl);

        myadviceService.modifyPlymindDocument(plymindDocumentEntity, document_srl);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }
}
