package com.ckstack.ckpush.controller.open;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.common.view.DownloadView;
import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import com.ckstack.ckpush.service.mineral.CkFileService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 5. 7..
 */
@Controller
@RequestMapping("/resource")
public class FileController {
    private final static Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private CkFileService ckFileService;
    @Autowired
    private DownloadView downloadView;

    /**
     * repository 에 저장된 파일을 다운로드 시킨다.
     *
     * @param fileSrl 다운로드 할 파일 시리얼 넘버
     * @return download view 를 포함하는 model and view
     */
    @RequestMapping(value = "/repo/download/{file_srl}", method = RequestMethod.GET,
            produces = {"image/jpeg", "image/png", "image/gif", "application/json"})
    @ResponseBody
    public ModelAndView downloadRepositoryFile(@PathVariable("file_srl") long fileSrl) {
        FileRepositoryEntity fileRepositoryEntity = ckFileService.getFile(fileSrl);
        if(fileRepositoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "failed read file. file_srl [" + fileSrl + "]");
            LOG.error(reason.get("no_file"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        ModelAndView mav = new ModelAndView();
        mav.setView(downloadView);

        mav.addObject("downloadFile", new File(fileRepositoryEntity.getFile_path()));
        mav.addObject("fileName", fileRepositoryEntity.getOrig_name());

        return mav;
    }

    /**
     * document attach 에 저장된 파일을 다운로드 시킨다.
     *
     * @param fileSrl 다운로드 할 파일 시리얼 넘버
     * @return download view 를 포함하는 model and view
     */
    @RequestMapping(value = "/document/download/{file_srl}", method = RequestMethod.GET,
            produces = {"image/jpeg", "image/png", "image/gif", "application/json"})
    @ResponseBody
    public ModelAndView downloadDocumentAttachFile(@PathVariable("file_srl") long fileSrl) {
        DocumentAttachEntity documentAttachEntity = ckFileService.getDocumentAttachFile(fileSrl);
        if(documentAttachEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "failed read file. file_srl [" + fileSrl + "]");
            LOG.error(reason.get("no_file"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        ModelAndView mav = new ModelAndView();
        mav.setView(downloadView);

        mav.addObject("downloadFile", new File(documentAttachEntity.getFile_path()));
        mav.addObject("fileName", documentAttachEntity.getOrig_name());

        return mav;
    }

    /**
     * 저장용/메시지 푸쉬용/게시물 첨부용 이미지 파일을 보여 준다. resize, crop 을 지원 한다.
     * 게시물 첨부용 이미지는 게시물 작성시에 미리보기가 지원 되어야 히기 때문에 DB flag 가 delete 라도 출력 한다.
     * (저장용/메시지 푸쉬용은 flag가 delete 라면 출력 하지 않는다)
     * 다음 3 가지 모드를 지원 한다.
     *
     * 1. r 값이 0보다 크면 : r 은 0 에서 1 까지 의 값이며 원본 이미지를 비율로 resize 한다.
     * 2. x, y 값이 0 보다 크면 : x, y, w, h 값에 따라 이미지를 crop 한다. 만일 w, h 가 zero 이면 원본 이미지 그대로 출력
     * 3. r 이 0, x, y 가 0 보다 작다면 : w, h 값이 따라 resize 한다.
     * 4. 1, 2, 3 조건이 만족하지 않으면 원본 사이즈 그대로 출력 한다.
     *
     * @param fileType 보여줄 파일의 종류. repo: 리포지토리 파일, push: 메시지 푸쉬용 파일
     * @param fileSrl 출력할 이미지 파일의 시리얼 넘버
     * @param r resize ratio
     * @param x crop position x
     * @param y crop position y
     * @param w crop size width 또는 resize width
     * @param h crop size height 또는 resize height
     * @return 이미지 자체를 출력 한다.
     */
    @RequestMapping(value = "/show/image/{file_type}/{file_srl}", method = RequestMethod.GET,
            produces = {"image/jpeg", "image/png", "image/gif", "application/json"})
    @ResponseBody
    public byte[] showImageFile(@PathVariable("file_type") String fileType,
                                @PathVariable("file_srl") long fileSrl,
                                @RequestParam(value = "r", required = false, defaultValue = "0") double r,
                                @RequestParam(value = "x", required = false, defaultValue = "-1") int x,
                                @RequestParam(value = "y", required = false, defaultValue = "-1") int y,
                                @RequestParam(value = "w", required = false, defaultValue = "0") int w,
                                @RequestParam(value = "h", required = false, defaultValue = "0") int h) {
        int iFileType;

        if(StringUtils.equals(fileType, "repo"))            iFileType = MDV.FILE_REPOSITORY;
        else if(StringUtils.equals(fileType, "push"))       iFileType = MDV.FILE_MSG_PUSH;
        else if(StringUtils.equals(fileType, "document"))   iFileType = MDV.FILE_DOC_ATTACH;
        else {
            Map<String, String> reason = new HashMap<>();
            reason.put("image_type", "not supported image_type [" + fileType + "]");
            LOG.error(reason.get("image_type"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        // 이미지 비율로 resize 처리
        if(r > 0) {
            if(r == 1) {
                LOG.warn("ignore ratio image. ratio value is larger then 1. show original image. r [" + r + "]");
                return ckFileService.getImageFile(fileSrl, iFileType);
            }
            return ckFileService.getImageFileRatio(fileSrl, r, iFileType);
        }

        // 이미지 crop 처리
        if(x >= 0 && y >= 0) {
            if(w <= 0 || h <= 0) {
                LOG.warn("ignore crop image. width, height is less then zero. show original image. x [" +
                        x + "], y [" + y + "], w [" + w + "], h [" + h + "]");
                return ckFileService.getImageFile(fileSrl, iFileType);
            }
            return ckFileService.getImageFileCrop(fileSrl, x, y, w, h, iFileType);
        }

        // 이미지 resize 처리
        if(w > 0 && h > 0) return ckFileService.getImageFileResize(fileSrl, w, h, iFileType);

        LOG.info("show original image. file_srl [" + fileSrl + "]");

        return ckFileService.getImageFile(fileSrl, iFileType);
    }

}
