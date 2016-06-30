package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.security.CkUserDetails;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.mineral.CkFileService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by dhkim94 on 15. 4. 30..
 */
@Controller
@RequestMapping("/admin/resource")
public class MineralController {
    private final static Logger LOG = LoggerFactory.getLogger(MineralController.class);

    @Autowired
    private CkFileService ckFileService;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;


    /**
     * 게시물 첨부 파일 업로드를 받아 준다.
     *
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @param multipartFile 업로드 시킬 파일
     * @param fileComment 파일의 코멘트
     * @return model and view object
     */
    @RequestMapping(value = "/document/add/file", method = RequestMethod.POST,
            headers = "content-type=multipart/form-data")
    public ModelAndView addFileToDocument(HttpServletRequest request,
                                          @RequestHeader("X-Tid") String tid,
                                          @RequestParam(value = "attach_file", required = false) MultipartFile multipartFile,
                                          @RequestParam(value = "file_comment", required = false) String fileComment) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DocumentAttachEntity documentAttachEntity = ckFileService.saveFile(multipartFile, fileComment,
                userDetails.getMember_srl(), webUtilService.getRequestIp(request));

        // uri, domain, file_srl, file_size, orig_name, mime_type 은 필수로 들어가야 한다.
        mav.addObject("uri", webUtilService.getFileURI(request, documentAttachEntity.getFile_srl(),
                "document_attach_uri"));
        mav.addObject("domain", confDym.getProperty("image_server_host"));
        mav.addObject("file_srl", documentAttachEntity.getFile_srl());
        mav.addObject("file_size", documentAttachEntity.getFile_size());
        mav.addObject("orig_name", documentAttachEntity.getOrig_name());
        mav.addObject("mime_type", documentAttachEntity.getMime_type());

        return mav;
    }

    /**
     * 파일 업로드 view 를 보여 준다.
     * 일반 파일, push 용 파일 두 가지 업로드를 지원 한다.
     *
     * @param model Model object
     * @return view name
     */
    @RequestMapping(value = "/repository/add/file")
    public String addFileForm(Model model) {
        model.addAttribute("mdv_none", MDV.NONE);
        model.addAttribute("mdv_yes", MDV.YES);

        return "f_admin/mineral/add_file";
    }

    /**
     * 임의 파일을 등록 한다. fileUpload 을 통해서 등록된 물리 이미지 파일을 최종으로 DB 정보로 등록하는 역할을 한다.
     *
     * @param tid transaction id
     * @param requestBody 파일 등록 정보
     * @return model and view object
     */
    @RequestMapping(value = "/repository/add/file/t/{tid}", method = RequestMethod.POST)
    public ModelAndView addFileSubmit(HttpServletRequest request,
                                      @PathVariable("tid") String tid,
                                      @RequestBody Map<String, Object> requestBody) {
        if(requestBody == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "no parameter. parameter is null");
            LOG.error(reason.get("parameter_parsing"));
            throw new CustomException("add_file_error", reason);
        }

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fileURL;

        if(!requestBody.containsKey("file_url1")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_url1", "invalid file_url1. file_url1 [" + requestBody.get("file_srl1") + "]");
            LOG.error(reason.get("file_url1"));
            throw new CustomException("add_file_error", reason);
        }

        fileURL = StringUtils.trim(requestBody.get("file_url1").toString());

        if(StringUtils.equals(fileURL, "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_url1", "invalid file_url1. fileURL [" + fileURL + "]");
            LOG.error(reason.get("file_url1"));
            throw new CustomException("add_file_error", reason);
        }

        if(!requestBody.containsKey("file_srl1") ||
                !NumberUtils.isNumber(requestBody.get("file_srl1").toString())) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl1", "invalid file_srl1. file_srl1 [" + requestBody.get("file_srl1") + "]");
            LOG.error(reason.get("file_srl1"));
            throw new CustomException("add_file_error", reason);
        }

        long fileSrl = Long.parseLong(requestBody.get("file_srl1").toString(), 10);

        if(!requestBody.containsKey("file_type")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_type", "invalid file_type. file_type [" + requestBody.get("file_srl1") + "]");
            LOG.error(reason.get("file_type"));
            throw new CustomException("add_file_error", reason);
        }

        int fileType = Integer.parseInt(requestBody.get("file_type").toString());

        // 외부 링크 파일 다운로드 후 저장
        if(fileSrl <= 0) {
            // 파일 다운로드
            byte[] fileData = webUtilService.serverToServerDownload(fileURL);
            if(fileData == null) {
                Map<String, String> reason = new HashMap<>();
                reason.put("file_url1", messageSource.getMessage("valid.file_url1", null,
                        LocaleContextHolder.getLocale()));
                throw new CustomException("add_file_error", reason);
            }

            // 파일 저장
            FileRepositoryEntity fileRepositoryEntity = ckFileService.saveFile(fileType, fileURL, fileData,
                    userDetails.getUser_id(), webUtilService.getRequestIp(request), true);
            fileSrl = fileRepositoryEntity.getFile_srl();
        }

        // 파일 메타 정보 수정
        FileRepositoryEntity modifyVo = new FileRepositoryEntity();
        modifyVo.init();
        modifyVo.setFile_srl(fileSrl);
        modifyVo.setDeleted(MDV.NO);
        modifyVo.setWidth(MDV.NUSE);
        modifyVo.setHeight(MDV.NUSE);
        modifyVo.setThumb_width(MDV.NUSE);
        modifyVo.setThumb_height(MDV.NUSE);
        modifyVo.setFile_type(fileType);

        if(requestBody.containsKey("file_comment"))
            modifyVo.setFile_comment(requestBody.get("file_comment").toString());

        ckFileService.modifyFile(modifyVo);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");
        mav.addObject("file_srl", fileSrl);

        return mav;
    }

    /**
     * 임의 파일 업로드를 받아 준다.
     *t
     * @param request HttpServletRequest object
     * @param attachFile 업로드 되는 파일
     * @param tid transaction id
     * @return ModelAndView object
     */
    @RequestMapping(value = "/repository/upload/file/t/{tid}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ModelAndView fileUpload(HttpServletRequest request,
                                   @RequestParam("attach_file") MultipartFile attachFile,
                                   @PathVariable("tid") String tid) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        CkUserDetails userDetails = (CkUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FileRepositoryEntity fileRepositoryEntity = ckFileService.saveFile(attachFile,
                userDetails.getUser_id(), webUtilService.getRequestIp(request), true);

        mav.addObject("uri", webUtilService.getFileURI(request, fileRepositoryEntity.getFile_srl(),
                "repository_file_type_uri"));
        mav.addObject("domain", confDym.getProperty("image_server_host"));
        mav.addObject("file_srl", fileRepositoryEntity.getFile_srl());

        return mav;
    }

    /**
     * 임의 파일 리스트를 보여주는 폼을 출력 한다.
     * @return view name
     */
    @RequestMapping(value = "/repository/file/list", method = RequestMethod.GET)
    public String fileList() {
        return "f_admin/mineral/list_file";
    }

    /**
     * 임의 파일 목록 데이터를 구한다.
     *0
     * @param request HttpServletRequest object
     * @param tid transaction id
     * @return list data
     */
    @RequestMapping(value = "/repository/file/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView fileList(HttpServletRequest request,
                                 @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_image_for_gcm_apns_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("deleted", Integer.toString(MDV.NO));

        List<Integer> file_types = new ArrayList<Integer>();
        file_types.add(Integer.parseInt("1")); //  심리검사지
        file_types.add(Integer.parseInt("4")); // 사전 검사지
        file_types.add(Integer.parseInt("5")); // 사후 검사지

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = ckFileService.getFileCountPlymind(null, MDV.NO, MDV.NUSE, file_types);
        long filterRows = ckFileService.getFileCountPlymind(searchFilter, file_types);

        //List<FileRepositoryEntity> fileList = ckFileService.getFile(searchFilter, sortValue, offset, limit);
        List<FileRepositoryEntity> fileList = ckFileService.getFilePlymind(searchFilter, file_types, sortValue, offset, limit);

        List<Map<String, Object>> table = new ArrayList<>();

        for(FileRepositoryEntity fileRepositoryEntity : fileList) {
            Map<String, Object> row = new HashMap<>();

            row.put("file_srl", fileRepositoryEntity.getFile_srl());
            row.put("file_comment", fileRepositoryEntity.getFile_comment());
            row.put("file_type", fileRepositoryEntity.getFile_type());
            row.put("orig_name", fileRepositoryEntity.getOrig_name());
            row.put("file_size", fileRepositoryEntity.getFile_size());

            String fileUrl = fileRepositoryEntity.getFile_url();
            if(fileUrl == null || StringUtils.equals(fileUrl = StringUtils.trim(fileUrl), "")) {
                if (StringUtils.indexOf(fileRepositoryEntity.getMime_type(), "image/") == -1)
                    row.put("file_url", confDym.getProperty("image_server_host") +
                            webUtilService.getFileURI(request, fileRepositoryEntity.getFile_srl(), "repository_file_download_uri"));
                else
                    row.put("file_url", confDym.getProperty("image_server_host") +
                            webUtilService.getFileURI(request, fileRepositoryEntity.getFile_srl(), "repository_file_type_uri"));
            } else
                row.put("file_url", fileUrl);

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
     * push message(gcm, apns) 용 이미지 파일 업로드를 받아 준다.
     *
     * @param request HttpServletRequest object
     * @param attachFile 업로드 되는 파일
     * @param tid transaction id
     * @return ModelAndView object
     */
    @RequestMapping(value = "/gcm/apns/file/upload/t/{tid}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ModelAndView gcmApnsFileUpload(HttpServletRequest request,
                                          @RequestParam("attach_file")MultipartFile attachFile,
                                          @PathVariable("tid") String tid) {
        FileEntity fileEntity = ckFileService.savePushImage(attachFile, webUtilService.getRequestIp(request), true);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("f_api/not_support");

        webUtilService.setCommonApiResponse(mav, tid, "success");

        mav.addObject("uri", webUtilService.getFileURI(request, fileEntity.getFile_srl(),
                "push_file_type_uri"));
        mav.addObject("domain", confDym.getProperty("image_server_host"));
        mav.addObject("file_srl", fileEntity.getFile_srl());

        return mav;
    }

    /**
     * Push 메시지용 이미지 리스트를 보여주는 폼을 출력 한다.
     *
     * @return view name
     */
    @RequestMapping(value = "/gcm/apns/file/list", method = RequestMethod.GET)
    public String gcmApnsFileListForm() {
        return "f_admin/mineral/list_push_file";
    }

    /**
     * Push 메시지용 이미지 리스트를 구한다.
     *
     * @param request HttpServletRequest Object
     * @param tid transaction id
     * @return push 메시지용 이미지 리스트
     */
    @RequestMapping(value = "/gcm/apns/file/list/t/{tid}", method = RequestMethod.POST)
    public ModelAndView gcmApnsFileList(HttpServletRequest request,
                                        @PathVariable("tid") String tid) {
        // DataTable 파라미터 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> sortValue = new LinkedHashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        if(!webUtilService.parsingDataTableParameter(columnValue, sortValue, extraValue, request)) {
            LOG.error("failed parsing jquery data-table parameter parsing");
            Map<String, String> reason = new HashMap<>();
            reason.put("parameter_parsing", "failed parsing jquery data-table parameter parsing");
            throw new CustomException("list_image_for_gcm_apns_error", reason);
        }

        // 검색 칼럼과 검색값을 구한다.
        Map<String, String> searchFilter = webUtilService.getSearchFilter(columnValue, extraValue);
        searchFilter.put("deleted", Integer.toString(MDV.NO));

        int offset = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_start")), 10);
        int limit = Integer.parseInt(extraValue.get(confCommon.getProperty("dtreq_length")), 10);

        long totalRows = ckFileService.getPushMessagePicCount(null, MDV.NO);
        long filterRows = ckFileService.getPushMessagePicCount(searchFilter);

        List<Map<String, Object>> imageInfos = ckFileService.getPushMessagePic(searchFilter,
                sortValue, offset, limit);
        List<Map<String, Object>> table = new ArrayList<>();

        for(Map<String, Object> imageInfo : imageInfos) {
            FileEntity fileEntity = (FileEntity) imageInfo.get("image");

            Map<String, Object> row = new HashMap<>();
            row.put("file_srl", fileEntity.getFile_srl());
            row.put("orig_name", fileEntity.getOrig_name());

            String fileUrl = fileEntity.getFile_url();
            if(fileUrl == null || StringUtils.equals(fileUrl = StringUtils.trim(fileUrl), ""))
                row.put("file_url", confDym.getProperty("image_server_host") +
                        webUtilService.getFileURI(request, fileEntity.getFile_srl(), "push_file_type_uri"));
            else
                row.put("file_url", fileUrl);

            if(imageInfo.containsKey("app")) {
                @SuppressWarnings("unchecked")
                List<AppEntity> appEntities = (List<AppEntity>) imageInfo.get("app");
                if(appEntities.size() > 1) row.put("app_name", StringUtils.trim(appEntities.get(0).getApp_name())+", ...");
                else if(appEntities.size() == 1) row.put("app_name", StringUtils.trim(appEntities.get(0).getApp_name()));
                else row.put("app_name", "");
            } else {
                row.put("app_name", "");
            }

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

}
