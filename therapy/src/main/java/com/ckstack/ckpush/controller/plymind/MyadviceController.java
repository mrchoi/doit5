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
import com.ckstack.ckpush.service.plymind.*;
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
 * Created by wypark on 16. 1. 18.
 */
@Controller
@RequestMapping("/user/myadvice")
public class MyadviceController {
    private final static Logger LOG = LoggerFactory.getLogger(MyadviceController.class);

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
    protected Properties confDym;

    @Autowired
    private MineralController mineralController;

    /**
     * 심리상담 목록 페이지로 이동한다.
     * @return model and view
     */
    @RequestMapping(value = "/progress_list", method = RequestMethod.GET)
    public String adviceList(Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> presentMap = myadviceService.getPresent(userDetails.getMember_srl());

        model.addAttribute("presentMap", presentMap);

        return "f_service/plymind/myadvice/progress_list";
    }

    /**
     * 심리상담 리스트를 조회한다.
     * @return model and view
     */
    @RequestMapping(value = "/progress_list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView applicationGroupList(HttpServletRequest request, @PathVariable("tid") String tid) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);

        List<Long> contents_srls = new ArrayList<Long>();
        contents_srls.add(Long.parseLong("1"));
        contents_srls.add(Long.parseLong("2"));
        contents_srls.add(Long.parseLong("3"));
        contents_srls.add(Long.parseLong("4"));
        contents_srls.add(Long.parseLong("5"));

        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(MDV.APPLICATION_STATUS_READY);// 준비중
        application_statues.add(MDV.APPLICATION_STATUS_PROGRESS);// 진행중

        List<Map<String, Object>> table = new ArrayList<>();
        long totalRows = applicationService.countApplicationGroup(userDetails.getMember_srl(), MDV.NUSE, application_statues, contents_srls);
        long filterRows = applicationService.countSearchApplicationGroup(userDetails.getMember_srl(), MDV.NUSE, application_statues, contents_srls, searchFilter);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationGroupList(userDetails.getMember_srl(), MDV.NUSE, application_statues, contents_srls, searchFilter, sortValue, offset, limit);

        for (ApplicationEntity applicationEntity : applicationEntities) {
            Map<String, Object> row = new HashMap<>();

            // data table 이 여러개 들어 가기 때문에 dt-row-<id>를 사용하지 않고 dt-document-row-<id>를 사용한다.
            row.put(confCommon.getProperty("dtresp_row_id"), "dt-document-row-" + applicationEntity.getApplication_group());
            row.put("application_srl", applicationEntity.getApplication_srl());
            row.put("application_group", applicationEntity.getApplication_group());
            row.put("member_srl", applicationEntity.getMember_srl());
            row.put("advisor_srl", applicationEntity.getAdvisor_srl());
            row.put("product_srl", applicationEntity.getProduct_srl());
            row.put("c_date", applicationEntity.getC_date());
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
     * 심리상담 상세보기 페이지
     * @param menu_type : progress or complete
     * @param application_group :
     * @return model and view
     */
    @RequestMapping(value = "/progress_detail/{menu_type}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String progressDetail(HttpServletRequest request,
                                 @PathVariable("menu_type") String menu_type,
                                 @PathVariable("application_srl") long application_srl,
                                 @PathVariable("application_group") int application_group,
                         Model model) {

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<MemberEntity> memberEntities = memberService.getMemberList(null, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        List<ProductEntity> productEntities = productService.getProductList(MDV.NUSE);

        List<AppointmentEntity> appointmentEntities = appointmentService.getAppointmentList(MDV.NUSE);

        List<ApplicationEntity> applicationEntities = applicationService.getApplicationListByGroup(userDetails.getMember_srl(), application_group, null, MDV.NUSE, MDV.NUSE);

        ApplicationEntity applicationEntity = new ApplicationEntity();
        MemberEntity advisorEntity = new MemberEntity();

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
                    advisorEntity = memberEntity;
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

            if(application_srl > 0 && application_srl == applicationEntity.getApplication_srl()) {
                break;
            }

            if(application_srl == 0 && (applicationEntity.getApplication_status() == 1 || applicationEntity.getApplication_status() == 2)) {
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

        List<DiaryEntity> diaryEntities = myadviceService.getDiary(userDetails.getMember_srl(), application_srl, application_group);

        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(Integer.parseInt("3"));

        List<PushEntity> pushEntities = myadviceService.getPushList(application_srl, application_group, application_statues, null, MDV.NUSE, MDV.NUSE);

        List<PushEntity> messageEntities = myadviceService.getMessageList(application_srl, application_group, 2);

        List<PlymindDocumentEntity> plymindDocumentEntities = myadviceService.getPlymindDocument(application_srl, application_group);
        List<PlymindDocumentEntity> tempplymindDocumentEntities = new ArrayList<>();

        for (PlymindDocumentEntity plymindDocumentEntity : plymindDocumentEntities) {
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

            String checkup_fileUrl = "";
            String reply_fileUrl = "";
            String result_fileUrl = "";

            if(plymindDocumentEntity.getCheckup_file_srl() > 0){
                checkup_fileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getCheckup_file_srl(), "repository_file_download_uri");
            }
            temp.setCheckup_file_url(checkup_fileUrl);

            if(plymindDocumentEntity.getReply_file_srl() > 0){
                reply_fileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getReply_file_srl(), "repository_file_download_uri");
            }
            temp.setReply_file_url(reply_fileUrl);

            if(plymindDocumentEntity.getResult_file_srl() > 0){
                result_fileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getResult_file_srl(), "repository_file_download_uri");
            }
            temp.setResult_file_url(result_fileUrl);


            tempplymindDocumentEntities.add(temp);
        }

        model.addAttribute("applicationEntity", applicationEntity);
        model.addAttribute("diaryEntities", diaryEntities);
        model.addAttribute("messageEntities", messageEntities);
        model.addAttribute("plymindDocumentEntities", tempplymindDocumentEntities);
        model.addAttribute("pushEntities", pushEntities);
        model.addAttribute("advisorEntity", advisorEntity);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("menu_type", menu_type);

        return "f_service/plymind/myadvice/progress_detail";
    }

    /**
     * 심리 상담 수정 처리.
     * @return model and view
     */
    @RequestMapping(value = "/{document_srl}/t/{tid}", method = RequestMethod.PUT)
    public ModelAndView checkupModify(HttpServletRequest request,
                                      @Valid @RequestBody Map<String, Object> requestBody,
                                      @PathVariable("document_srl") long document_srl,
                                      @PathVariable("tid") String tid) {

        if(document_srl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "not found document_srl. document_srl [" + document_srl + "]");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("read_plymind_document_error", reason);
        }

        long application_srl = Long.parseLong(requestBody.get("application_srl").toString());
        long application_group = Long.parseLong(requestBody.get("application_group").toString());
        long file_srl1 = Long.parseLong(requestBody.get("file_srl1").toString());
        String file_url1 = requestBody.get("file_url1").toString();
        String file_type = requestBody.get("file_type").toString();
        long checkup_file_srl = Long.parseLong(requestBody.get("checkup_file_srl").toString());
        long reply_file_srl = Long.parseLong(requestBody.get("reply_file_srl").toString());
        //long result_file_srl = Long.parseLong(requestBody.get("result_file_srl").toString());

        //repository 파일등록 ( 결과 파일 등록 )
        if(file_srl1 > 0 ) {
            mineralController.addFileSubmit(request, tid, requestBody);
            reply_file_srl = file_srl1;

        }

        //결과지 파일 등록 및 맵핑 변경
        PlymindDocumentEntity plymindDocumentEntity = new PlymindDocumentEntity();
        plymindDocumentEntity.setCheckup_file_srl(checkup_file_srl);
        plymindDocumentEntity.setReply_file_srl(reply_file_srl);

        myadviceService.modifyPlymindDocument(plymindDocumentEntity, document_srl);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");

        return mav;
    }

    /**
     * 스마트기기 수령 확인 처리
     * @return model and view
     */
    @RequestMapping(value = "/receive_check", method = RequestMethod.POST)
    public ModelAndView receiveCheck(@Valid @RequestBody Map<String, Object> requestBody) {
        String application_group = requestBody.get("application_group").toString();

        int resultCode = applicationService.receiveCheck(Integer.parseInt(application_group));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 마음일기 등록 페이지
     * @return model and view
     */
    @RequestMapping(value = "/mind_diary_add/{menu_type}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String mindDiaryAdd(@PathVariable("menu_type") String menu_type,
                               @PathVariable("application_group") String application_group,
                               @PathVariable("application_srl") String application_srl,
                               Model model) {
        model.addAttribute("menu_type", menu_type);
        model.addAttribute("application_group", application_group);
        model.addAttribute("application_srl", application_srl);

        return "f_service/plymind/myadvice/mind_diary_add";
    }

    /**
     * 마음일기 등록 처리.
     * @return model and view
     */
    @RequestMapping(value = "/mind_diary_add", method = RequestMethod.POST)
    public ModelAndView addDiary(HttpServletRequest request,
                                 @Valid @RequestBody DiaryEntity diaryEntity) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        diaryEntity.setMember_srl(userDetails.getMember_srl());

        int resultCode = myadviceService.addDiary(diaryEntity);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, "", "success");

        return mav;
    }

    /**
     * 마음일기 더 보기 페이지
     * @return model and view
     */
    @RequestMapping(value = "/mind_diary_view/{menu_type}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String mindDiaryList(@PathVariable("menu_type") String menu_type,
                                @PathVariable("application_srl") long application_srl,
                                @PathVariable("application_group") int application_group,
                                Model model) {
        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<DiaryEntity> diaryEntities = myadviceService.getDiary(userDetails.getMember_srl(), application_srl, application_group);

        model.addAttribute("application_group", application_group);
        model.addAttribute("diaryEntities", diaryEntities);
        model.addAttribute("menu_type", menu_type);

        return "f_service/plymind/myadvice/mind_diary_view";
    }

    /**
     * 그룹테라피 더 보기 페이지
     * @return model and view
     */
    @RequestMapping(value = "/message_view/{menu_type}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String messageList(@PathVariable("menu_type") String menu_type,
                              @PathVariable("application_srl") long application_srl,
                              @PathVariable("application_group") int application_group,
                              Model model) {
        List<PushEntity> messageEntities = myadviceService.getMessageList(application_srl, application_group, 2);

        model.addAttribute("application_group", application_group);
        model.addAttribute("messageEntities", messageEntities);
        model.addAttribute("menu_type", menu_type);

        return "f_service/plymind/myadvice/message_view";
    }

    /**
     * 컨텐츠케어세라피 인문컨텐츠 페이지
     * @return model and view
     */
    @RequestMapping(value = "/push_view/{menu_type}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String smsPushList(@PathVariable("menu_type") String menu_type,
                              @PathVariable("application_srl") long application_srl,
                              @PathVariable("application_group") int application_group,
                              Model model) {
        List<Integer> application_statues = new ArrayList<Integer>();
        application_statues.add(Integer.parseInt("3"));

        List<PushEntity> pushEntities = myadviceService.getPushList(application_srl, application_group, application_statues, null, MDV.NUSE, MDV.NUSE);

        model.addAttribute("application_group", application_group);
        model.addAttribute("pushEntities", pushEntities);
        model.addAttribute("menu_type", menu_type);

        return "f_service/plymind/myadvice/push_view";
    }

    public List<PlymindDocumentEntity> getCommentPlymindDocumentEntities(HttpServletRequest request,
                                                                         long application_srl,
                                                                         int application_group)
    {
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

            String checkup_fileUrl = "";
            String reply_fileUrl = "";
            if(plymindDocumentEntity.getCheckup_file_srl() > 0){
                checkup_fileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getCheckup_file_srl(), "repository_file_download_uri");

            }
            temp.setCheckup_file_url(checkup_fileUrl);

            if(plymindDocumentEntity.getReply_file_srl() > 0){
                reply_fileUrl = confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, plymindDocumentEntity.getReply_file_srl(), "repository_file_download_uri");
            }
            temp.setReply_file_url(reply_fileUrl);

            tempplymindDocumentEntities.add(temp);
        }

        return tempplymindDocumentEntities;
    }

    /**
     * 상담사 코멘트 및 검사지 페이지
     * @return model and view
     */
    @RequestMapping(value = "/advisor_comment_view/{menu_type}/{application_srl}/{application_group}", method = RequestMethod.GET)
    public String advisorCommentList(HttpServletRequest request,
                                     @PathVariable("menu_type") String menu_type,
                                     @PathVariable("application_srl") long application_srl,
                                     @PathVariable("application_group") int application_group,
                                     Model model) {

        List<PlymindDocumentEntity> tempplymindDocumentEntities = this.getCommentPlymindDocumentEntities(request, application_srl, application_group);


        model.addAttribute("application_group", application_group);
        model.addAttribute("plymindDocumentEntities", tempplymindDocumentEntities);
        model.addAttribute("mdv_no", MDV.NO);
        model.addAttribute("mdv_yes", MDV.YES);
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("menu_type", menu_type);

        return "f_service/plymind/myadvice/advisor_comment_view";
    }

    private String getFileRepositoryEntity(long file_srl){

        if(file_srl <= 0) {
            return "";
        }

        FileRepositoryEntity fileRepositoryEntity = ckFileService.getFile(file_srl);

        return fileRepositoryEntity.getOrig_name();
    }
}