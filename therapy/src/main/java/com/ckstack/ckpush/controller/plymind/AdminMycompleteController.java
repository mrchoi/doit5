package com.ckstack.ckpush.controller.plymind;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.controller.admin.MineralController;
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
import javax.validation.Valid;
import java.util.*;

/**
 * Created by kas0610 on 16. 2. 19.
 */
@Controller
@RequestMapping("/admin/mycomplete")
public class AdminMycompleteController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminMycompleteController.class);

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
    private AdminMyadviceController adminMyadviceController;
    @Autowired
    private MineralController mineralController;

    /**
     * 완료 내역 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String checkupList(Model model) {

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);

        return "f_admin/plymind/mycomplete/complete_list";
    }

    /**
     * 완료 내역 리스트를 조회한다.
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

        List<MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<ApplicationEntity> applicationMemberEntities = applicationService.getApplicationMember(MDV.NUSE);

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("application_listType", "completeList");

        List<Long> contents_srls = new ArrayList<Long>();

        List<Integer> application_statues = new ArrayList<Integer>();
        //application_statues.add(MDV.APPLICATION_STATUS_INSERT);
        //application_statues.add(MDV.APPLICATION_STATUS_READY);
        //application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);
        application_statues.add(MDV.APPLICATION_STATUS_COMPLETE);
        //application_statues.add(MDV.APPLICATION_STATUS_CANCLE);

        //상담사 로그인 시 처리부분
        Map<String, String> advisor = memberService.getAdvisorAuthority();
        long advisor_srl = MDV.NUSE;
        if(advisor.get("advisor").equals("true")) {
            advisor_srl = Long.valueOf(advisor.get("member_srl"));
        }

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = applicationService.countApplicationGroupComplete(MDV.NUSE, advisor_srl, application_statues, contents_srls);
        long filterRows = applicationService.countSearchApplicationGroupComplete(MDV.NUSE, advisor_srl, application_statues, contents_srls, searchFilter);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationGroupList(MDV.NUSE, advisor_srl, application_statues, contents_srls, searchFilter, sortValue, offset, limit);

        for (ApplicationEntity applicationEntity : applicationEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + applicationEntity.getApplication_group());
            row.put("application_srl", applicationEntity.getApplication_srl());
            row.put("application_group", applicationEntity.getApplication_group());
            row.put("member_srl", applicationEntity.getMember_srl());
            row.put("advisor_srl", applicationEntity.getAdvisor_srl());
            row.put("product_srl", applicationEntity.getProduct_srl());
            row.put("advisor_name", "");

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

            // 회차별로 상세보기 로직 추가
            List<ApplicationEntity> eachApplicationEntities = applicationService.getApplicationListByGroup(applicationEntity.getMember_srl(),
                    applicationEntity.getApplication_group(), null, MDV.NUSE, MDV.NUSE);
            row.put("eachApplicationEntities", eachApplicationEntities);

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

            ApplicationEntity tempapplicationEntity = applicationService.getApplication(applicationEntity.getApplication_srl());
            row.put("application_number", tempapplicationEntity.getApplication_number());
            row.put("application_status", tempapplicationEntity.getApplication_status());

            List<PlymindDocumentEntity> tempplymindDocumentEntities = myadviceService.getPlymindDocument(0, applicationEntity.getApplication_group());
            String fileUrl = "size";
            if(tempplymindDocumentEntities.size() > 1 || tempplymindDocumentEntities == null){
                LOG.info("many result_file_srl");
            }else{
                for (PlymindDocumentEntity plymindDocumentEntity : tempplymindDocumentEntities)
                {
                    if(plymindDocumentEntity != null && plymindDocumentEntity.getResult_file_srl() > 0){
                        LOG.info("found result_file_srl : " + plymindDocumentEntity.getResult_file_srl());
                        fileUrl = confDym.getProperty("image_server_host") +
                                webUtilService.getFileURI(request, plymindDocumentEntity.getResult_file_srl(), "repository_file_download_uri");
                    } else {
                        LOG.info("found not result_file_srl");
                    }
                }
            }

            row.put("result_file_url", fileUrl);

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
     * 심리상담 상세보기 페이지 ( 완료 내역에서 회차별로 보기 위해 추가 됨 )
     * @param member_srl :
     * @param application_srl :
     * @return model and view
     */
    @RequestMapping(value = "/complete_detail/{member_srl}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String progressDetail(HttpServletRequest request,
                                 @PathVariable("member_srl") long member_srl,
                                 @PathVariable("application_srl") long application_srl,
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
            applicationEntity = applicationEntities.get(i);

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    applicationEntity.setUser_name(memberEntity.getUser_name());
                    break;
                }
            }

            for (MemberEntity memberEntity : memberEntities) {
                if (applicationEntity.getAdvisor_srl() == memberEntity.getMember_srl()) {
                    applicationEntity.setAdvisor_name(memberEntity.getNick_name());
                    break;
                }
            }

            for (ProductEntity productEntity : productEntities) {
                if (applicationEntity.getProduct_srl() == productEntity.getProduct_srl()) {
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
                if (applicationEntity.getApplication_srl() == appointmentEntity.getApplication_srl()) {
                    applicationEntity.setAppointment_date(appointmentEntity.getAppointment_date());
                    applicationEntity.setAppointment_time(appointmentEntity.getAppointment_time());
                    break;
                }
            }

            //완료 내역에서 해당 회차 하나만 보여질 때
            if(application_srl == applicationEntity.getApplication_srl() ) {
                break;
            }
        }

        List<ApplicationEntity> applicationMemberEntities = applicationService.getApplicationMember(application_group);

        for (ApplicationEntity applicationMemberEntity : applicationMemberEntities) {
            for (MemberEntity memberEntity : memberEntities) {
                if (applicationMemberEntity.getMember_srl() == memberEntity.getMember_srl()) {
                    applicationMemberEntity.setUser_name(memberEntity.getUser_name());
                    break;
                }
            }
        }

        //마음일기
        List<DiaryEntity> diaryEntities = myadviceService.getDiary(member_srl, application_srl, application_group);
        List<Integer> application_statues = new ArrayList<Integer>();
        //application_statues.add(Integer.parseInt("3"));

        //push 메시지
        List<PushEntity> pushEntities = myadviceService.getPushList(application_srl, application_group, application_statues, null, MDV.NUSE, MDV.NUSE);
        List<PushEntity> messageEntities = myadviceService.getMessageList(application_srl, application_group, MDV.NUSE);

        List<PlymindDocumentEntity> plymindDocumentEntities = myadviceService.getPlymindDocument(application_srl, application_group);
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
        model.addAttribute("diaryEntities", diaryEntities);
        model.addAttribute("pushEntities", pushEntities);
        model.addAttribute("messageEntities", messageEntities);
        model.addAttribute("plymindDocumentEntities", tempplymindDocumentEntities);
        model.addAttribute("applicationMemberEntities", applicationMemberEntities);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);

        return "f_admin/plymind/mycomplete/complete_detail";
    }

    private String getFileRepositoryEntity(long file_srl){

        if(file_srl <= 0) {
            return "";
        }

        FileRepositoryEntity fileRepositoryEntity = ckFileService.getFile(file_srl);

        return fileRepositoryEntity.getOrig_name();
    }

    /**
     * 결과보고서 등록 페이지로 이동
     * @return model and view
     */
    @RequestMapping(value = "/complete_file_add/{kind}/{member_srl}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String advisorCommentAdd(HttpServletRequest request,
                                    @PathVariable("kind") int kind,
                                    @PathVariable("member_srl") long member_srl,
                                    @PathVariable("application_srl") long application_srl,
                                    @PathVariable("application_group") int application_group,
                                    Model model) {

        //심리검사일 경우 application_srl 0 처리
        if(kind == 1) application_srl = 0;

        List<PlymindDocumentEntity> plymindDocumentEntities = myadviceService.getPlymindDocument(application_srl, application_group);
        List<PlymindDocumentEntity> tempplymindDocumentEntities = new ArrayList<>();

        for (PlymindDocumentEntity plymindDocumentEntity : plymindDocumentEntities)
        {
            PlymindDocumentEntity temp = new PlymindDocumentEntity();

            temp.setDocument_srl(plymindDocumentEntity.getDocument_srl());
            temp.setApplication_srl(plymindDocumentEntity.getApplication_srl());
            temp.setApplication_group(plymindDocumentEntity.getApplication_group());
            temp.setResult_file_srl(plymindDocumentEntity.getResult_file_srl());

            temp.setResult_file_name(this.getFileRepositoryEntity(plymindDocumentEntity.getResult_file_srl()));

            String resultfileUrl = "";
            if(plymindDocumentEntity.getResult_file_srl() > 0){
                resultfileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getResult_file_srl(), "repository_file_download_uri");
            }
            temp.setResult_file_url(resultfileUrl);


            tempplymindDocumentEntities.add(temp);
        }

        LOG.debug("결과파일존재확인" + plymindDocumentEntities.size());

        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("member_srl", member_srl);
        model.addAttribute("application_srl", application_srl);
        model.addAttribute("application_group", application_group);

        model.addAttribute("plymindDocumentEntities", tempplymindDocumentEntities);

        return "f_admin/plymind/mycomplete/complete_file_add";
    }

    /**
     * 결과보고서 등록 처리.
     * @return model and view
     */
    @RequestMapping(value = "/complete_file_add/add/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView checkupModify(HttpServletRequest request,
                                      @Valid @RequestBody Map<String, Object> requestBody,
                                      @PathVariable("tid") String tid) {


        long member_srl = Long.parseLong(requestBody.get("member_srl").toString());
        long application_srl = Long.parseLong(requestBody.get("application_srl").toString());
        int application_group = Integer.parseInt(requestBody.get("application_group").toString());

        long file_srl1 = Long.parseLong(requestBody.get("file_srl1").toString());
        String file_url1 = requestBody.get("file_url1").toString();
        String file_type = requestBody.get("file_type").toString();

        //repository 파일등록 ( 결과 파일 등록 )
        if(file_srl1 > 0 ) {
            mineralController.addFileSubmit(request, tid, requestBody);
        }

        //결과지 파일 등록 및 맵핑 변경
        PlymindDocumentEntity plymindDocumentEntity = new PlymindDocumentEntity();
        plymindDocumentEntity.setMember_srl(member_srl);
        plymindDocumentEntity.setApplication_srl(0); // 심리검사 결과파일은 0으로 처리.
        plymindDocumentEntity.setApplication_group(application_group);
        plymindDocumentEntity.setCheckup_file_srl(MDV.NONE);
        plymindDocumentEntity.setReply_file_srl(MDV.NONE);
        plymindDocumentEntity.setResult_file_srl(file_srl1);
        plymindDocumentEntity.setAdvisor_comment("결과보고서파일");

        myadviceService.addPlymindDocument(plymindDocumentEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 상담사 코멘트 보기 페이지로 이동
     * @return model and view
     */
    @RequestMapping(value = "/advisor_comment_view/{member_srl}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String advisorCommentView(HttpServletRequest request, @PathVariable("member_srl") long member_srl,
                                     @PathVariable("application_srl") long application_srl,
                                     @PathVariable("application_group") int application_group,
                                     Model model) {

        List<PlymindDocumentEntity> tempplymindDocumentEntities = adminMyadviceController.getCommentPlymindDocumentEntities(request, member_srl, application_srl, application_group);

        model.addAttribute("plymindDocumentEntities", tempplymindDocumentEntities);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("member_srl", member_srl);
        model.addAttribute("application_srl", application_srl);
        model.addAttribute("application_group", application_group);

        return "f_admin/plymind/mycomplete/advisor_comment_view";
    }

}
