package com.ckstack.ckpush.controller.plymind;

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
@RequestMapping("/user/resource")
public class UserMineralController {
    private final static Logger LOG = LoggerFactory.getLogger(UserMineralController.class);

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


}
