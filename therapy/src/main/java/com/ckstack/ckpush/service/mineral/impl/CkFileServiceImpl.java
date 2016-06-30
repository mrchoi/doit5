package com.ckstack.ckpush.service.mineral.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.mineral.*;
import com.ckstack.ckpush.dao.push.PushMessageDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.mineral.*;
import com.ckstack.ckpush.domain.push.PushMessageEntity;
import com.ckstack.ckpush.service.mineral.CkFileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tika.Tika;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by dhkim94 on 15. 4. 30..
 */
@Service
@Transactional(value = "transactionManager")
public class CkFileServiceImpl implements CkFileService {
    private final static Logger LOG = LoggerFactory.getLogger(CkFileServiceImpl.class);

    private DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
    private DateTimeFormatter fmtDD = DateTimeFormat.forPattern("dd");

    @Autowired
    private DocumentFileDao documentFileDao;
    @Autowired
    private DocumentAttachDao documentAttachDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private FileRepositoryDao fileRepositoryDao;
    @Autowired
    private PushMessagePicDao pushMessagePicDao;
    @Autowired
    private PushMessageDao pushMessageDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confExtension;
    @Autowired
    protected Properties confTblFile;
    @Autowired
    protected Properties confDym;

    @Transactional(readOnly = true)
    @Override
    public Map<Long, List<DocumentAttachEntity>> getDocumentAttachFile(List<Long> documentSrls) {
        Map<Long, List<DocumentAttachEntity>> ret = new HashMap<>();

        List<DocumentFileEntity> documentFileEntities = documentFileDao.get(MDV.NUSE, documentSrls,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        if(documentFileEntities.size() <= 0) return ret;

        List<Long> fileSrls = new ArrayList<>();
        for(DocumentFileEntity documentFileEntity : documentFileEntities) {
            long fileSrl = documentFileEntity.getFile_srl();
            if(!fileSrls.contains(fileSrl)) fileSrls.add(fileSrl);
        }

        List<DocumentAttachEntity> documentAttachEntities = documentAttachDao.get(fileSrls, MDV.NO, null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        if(documentAttachEntities.size() <= 0) return ret;

        Map<Long, DocumentAttachEntity> fileMap = new HashMap<>();
        for(DocumentAttachEntity documentAttachEntity : documentAttachEntities) {
            long fileSrl = documentAttachEntity.getFile_srl();
            if(!fileMap.containsKey(fileSrl)) fileMap.put(fileSrl, documentAttachEntity);
        }

        for(DocumentFileEntity documentFileEntity : documentFileEntities) {
            long documentSrl = documentFileEntity.getDocument_srl();
            long fileSrl = documentFileEntity.getFile_srl();

            if(!ret.containsKey(documentSrl)) ret.put(documentSrl, new ArrayList<DocumentAttachEntity>());

            List<DocumentAttachEntity> tmpList = ret.get(documentSrl);
            tmpList.add(fileMap.get(fileSrl));
        }

        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentAttachEntity getDocumentAttachFile(long fileSrl) {
        return documentAttachDao.get(fileSrl, MDV.NO);
    }

    @Transactional(readOnly = true)
    @Override
    public long getFileCount(String origName, int deleted) {
        if(origName != null) {
            origName = StringUtils.trim(origName);
            if(StringUtils.equals(origName, "")) origName = null;
        }

        return fileRepositoryDao.count(null, origName, null, deleted, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public long getFileCountPlymind(String origName, int deleted, int file_type, List<Integer> file_types) {
        if(origName != null) {
            origName = StringUtils.trim(origName);
            if(StringUtils.equals(origName, "")) origName = null;
        }

        return fileRepositoryDao.countPlymind(null, file_type, file_types, origName, null, deleted, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public long getFileCount(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.getFileCount(null, MDV.NUSE);

        String origName = null;
        int deleted = MDV.NUSE;

        if(searchFilter.containsKey("orig_name")) origName = searchFilter.get("orig_name");
        if(searchFilter.containsKey("deleted")) {
            String num = searchFilter.get("deleted");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deleted = Integer.parseInt(num, 10);
        }

        return this.getFileCount(origName, deleted);
    }

    @Override
    public long getFileCountPlymind(Map<String, String> searchFilter, List<Integer> file_types) {

        String origName = null;
        int deleted = MDV.NUSE;
        int file_type = MDV.NUSE;

        if(searchFilter == null) return this.getFileCountPlymind(null, MDV.NUSE, file_type, file_types);

        if(searchFilter.containsKey("orig_name")) origName = searchFilter.get("orig_name");
        if(searchFilter.containsKey("deleted")) {
            String num = searchFilter.get("deleted");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deleted = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("file_type")) {
            String num = searchFilter.get("file_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) file_type = Integer.parseInt(num, 10);
        }

        return this.getFileCountPlymind(origName, deleted, file_type, file_types);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FileRepositoryEntity> getFile(String origName, int deleted,
                                              Map<String, String> sort, int offset, int limit) {
        if(origName != null) {
            origName = StringUtils.trim(origName);
            if(StringUtils.equals(origName, "")) origName = null;
        }

        // sort 의 direction 체크
        if(sort == null || sort.size() <= 0) sort = null;
        else {
            Set<String> keys = sort.keySet();
            for(String key : keys) {
                if(!StringUtils.equals(sort.get(key), "desc") && !StringUtils.equals(sort.get(key), "asc")) {
                    LOG.warn("invalid column sort direction. column [" + key +"], direction [" +
                            sort.get(key) + "]. set default desc direction");
                    sort.put(key, "desc");
                }
            }
        }

        return fileRepositoryDao.get(null, deleted, origName, null, MDV.NUSE, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FileRepositoryEntity> getFile(Map<String, String> searchFilter,
                                              Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null) return this.getFile(null, MDV.NUSE, sort, offset, limit);

        String origName = null;
        int deleted = MDV.NUSE;

        if(searchFilter.containsKey("orig_name")) origName = searchFilter.get("orig_name");
        if(searchFilter.containsKey("deleted")) {
            String num = searchFilter.get("deleted");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deleted = Integer.parseInt(num, 10);
        }

        return this.getFile(origName, deleted, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FileRepositoryEntity> getFilePlymind(String origName, int deleted, int file_type, List<Integer> file_types,
                                                          Map<String, String> sort, int offset, int limit) {
        if(origName != null) {
            origName = StringUtils.trim(origName);
            if(StringUtils.equals(origName, "")) origName = null;
        }

        // sort 의 direction 체크ls
        if(sort == null || sort.size() <= 0) sort = null;
        else {
            Set<String> keys = sort.keySet();
            for(String key : keys) {
                if(!StringUtils.equals(sort.get(key), "desc") && !StringUtils.equals(sort.get(key), "asc")) {
                    LOG.warn("invalid column sort direction. column [" + key +"], direction [" +
                            sort.get(key) + "]. set default desc direction");
                    sort.put(key, "desc");
                }
            }
        }

        return fileRepositoryDao.getPlymind(null, deleted, origName, file_type, file_types, null, MDV.NUSE, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FileRepositoryEntity> getFilePlymind(Map<String, String> searchFilter, List<Integer> file_types,
                                              Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null) return this.getFilePlymind(null, MDV.NUSE, MDV.NUSE, null, sort, offset, limit);

        String origName = null;
        int file_type = MDV.NUSE;
        int deleted = MDV.NUSE;

        if(searchFilter.containsKey("orig_name")) origName = searchFilter.get("orig_name");
        if(searchFilter.containsKey("deleted")) {
            String num = searchFilter.get("deleted");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deleted = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("file_type")) {
            String num = searchFilter.get("file_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) file_type = Integer.parseInt(num, 10);
        }


        return this.getFilePlymind(origName, deleted, file_type, file_types, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public FileRepositoryEntity getFile(long fileSrl) {
        return fileRepositoryDao.get(fileSrl, MDV.NO);
    }

    @Override
    public void modifyFile(FileRepositoryEntity fileRepositoryEntity) {
        if(fileRepositoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_info", "failed modify file repository. fileRepositoryEntity is null");
            LOG.error(reason.get("no_info"));
            throw new CustomException("modify_file_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(fileRepositoryEntity.getFile_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "failed modify file repository. invalid file_srl [" + fileRepositoryEntity.getFile_srl() + "]");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("modify_file_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        FileRepositoryEntity prevVo = fileRepositoryDao.get(fileRepositoryEntity.getFile_srl(), MDV.NUSE);
        if(prevVo == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "failed modify file repository. no file file_srl [" + fileRepositoryEntity.getFile_srl() + "]");
            LOG.error(reason.get("no_file"));
            throw new CustomException("modify_file_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        boolean willModify = false;
        FileRepositoryEntity modifyVo = new FileRepositoryEntity();
        modifyVo.init();

        if(fileRepositoryEntity.getOrig_name() != null &&
                !StringUtils.equals(prevVo.getOrig_name(), fileRepositoryEntity.getOrig_name())) {
            modifyVo.setOrig_name(fileRepositoryEntity.getOrig_name());
            willModify = true;
        }

        if(fileRepositoryEntity.getMime_type() != null &&
                !StringUtils.equals(prevVo.getMime_type(), fileRepositoryEntity.getMime_type())) {
            modifyVo.setMime_type(fileRepositoryEntity.getMime_type());
            willModify = true;
        }

        if(fileRepositoryEntity.getFile_size() >= 0 && fileRepositoryEntity.getFile_size() != prevVo.getFile_size()) {
            modifyVo.setFile_size(fileRepositoryEntity.getFile_size());
            willModify = true;
        }

        if(fileRepositoryEntity.getFile_path() != null &&
                !StringUtils.equals(prevVo.getFile_path(), fileRepositoryEntity.getFile_path())) {
            modifyVo.setFile_path(fileRepositoryEntity.getFile_path());
            willModify = true;
        }

        if(fileRepositoryEntity.getFile_url() != null &&
                !StringUtils.equals(prevVo.getFile_url(), fileRepositoryEntity.getFile_url())) {
            modifyVo.setFile_url(fileRepositoryEntity.getFile_url());
            willModify = true;
        }

        if(fileRepositoryEntity.getWidth() != MDV.NUSE && prevVo.getWidth() != fileRepositoryEntity.getWidth()) {
            modifyVo.setWidth(fileRepositoryEntity.getWidth());
            willModify = true;
        }

        if(fileRepositoryEntity.getHeight() != MDV.NUSE && prevVo.getHeight() != fileRepositoryEntity.getHeight()) {
            modifyVo.setHeight(fileRepositoryEntity.getHeight());
            willModify = true;
        }

        if(fileRepositoryEntity.getThumb_url() != null &&
                !StringUtils.equals(prevVo.getThumb_url(), fileRepositoryEntity.getThumb_url())) {
            modifyVo.setThumb_url(fileRepositoryEntity.getThumb_url());
            willModify = true;
        }

        if(fileRepositoryEntity.getThumb_width() != MDV.NUSE && prevVo.getThumb_width() != fileRepositoryEntity.getThumb_width()) {
            modifyVo.setThumb_width(fileRepositoryEntity.getThumb_width());
            willModify = true;
        }

        if(fileRepositoryEntity.getThumb_height() != MDV.NUSE && prevVo.getThumb_height() != fileRepositoryEntity.getThumb_height()) {
            modifyVo.setThumb_height(fileRepositoryEntity.getThumb_height());
            willModify = true;
        }

        if(fileRepositoryEntity.getFile_comment() != null &&
                !StringUtils.equals(prevVo.getFile_comment(), fileRepositoryEntity.getFile_comment())) {
            modifyVo.setFile_comment(fileRepositoryEntity.getFile_comment());
            willModify = true;
        }

        if(fileRepositoryEntity.getUser_id() != null &&
                !StringUtils.equals(prevVo.getUser_id(), fileRepositoryEntity.getUser_id())) {
            modifyVo.setUser_id(fileRepositoryEntity.getUser_id());
            willModify = true;
        }

        if(fileRepositoryEntity.getIpaddress() != null &&
                !StringUtils.equals(prevVo.getIpaddress(), fileRepositoryEntity.getIpaddress())) {
            modifyVo.setIpaddress(fileRepositoryEntity.getIpaddress());
            willModify = true;
        }

        if(fileRepositoryEntity.getDeleted() > 0 && prevVo.getDeleted() != fileRepositoryEntity.getDeleted()) {
            modifyVo.setDeleted(fileRepositoryEntity.getDeleted());
            willModify = true;
        }

        if(fileRepositoryEntity.getFile_type() > 0 && prevVo.getFile_type() != fileRepositoryEntity.getFile_type()) {
            modifyVo.setFile_type(fileRepositoryEntity.getFile_type());
            willModify = true;
        }

        if(!willModify) {
            LOG.info("ignore modify file repository. same data. updateData [" + modifyVo.toString() + "]");
            return;
        }

        modifyVo.setU_date((int)(DateTime.now().getMillis() / 1000));

        fileRepositoryDao.modify(modifyVo, fileRepositoryEntity.getFile_srl());
        LOG.info("modify file repository. fileRepositoryVo [" + modifyVo + "]");
    }

    @Override
    public DocumentAttachEntity saveFile(MultipartFile multipartFile, String fileComment,
                                         long memberSrl, String requestIp) {
        if(multipartFile == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "file data is null");
            LOG.error(reason.get("no_file"));
            throw new CustomException("save_file_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentAttachEntity documentAttachEntity = new DocumentAttachEntity();
        documentAttachEntity.setOrig_name(multipartFile.getOriginalFilename());
        documentAttachEntity.setFile_size(multipartFile.getSize());

        // mime type 을 체크 한다.
        String fileExtension = null, mimeType = "";
        try {
            Tika tika = new Tika();
            mimeType = tika.detect(multipartFile.getInputStream());
        } catch (IOException e) {
            LOG.warn("check file mime type exception. e [" + e.getMessage() + "]");
        }

        documentAttachEntity.setMime_type(mimeType);

        // png 인지 체크 한다.
        String[] mimePng = StringUtils.split(confCommon.getProperty("mime_type_png"), ",");
        for(String mime : mimePng) {
            if(mimeType.equals(StringUtils.trim(mime))) {
                fileExtension = "png";
                break;
            }
        }

        // jpg 인지 체크 한다.
        if(fileExtension == null) {
            String[] mimeJpg = StringUtils.split(confCommon.getProperty("mime_type_jpg"), ",");
            for(String mime : mimeJpg) {
                if(mimeType.equals(StringUtils.trim(mime))) {
                    fileExtension = "jpg";
                    break;
                }
            }
        }

        // mime type 에서 파일 확장자 구하기가 실패 하면 원본 파일의 확장자를 본다.
        if(fileExtension == null) {
            LOG.debug("can't find file extension by mime-type. use origin file extension");
            fileExtension = StringUtils.lowerCase(org.springframework.util.StringUtils.getFilenameExtension(documentAttachEntity.getOrig_name()));
        }

        if(fileExtension == null) {
            LOG.warn("not found file extension. set file extension is empty string");
        }

        DateTime now = DateTime.now();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileDirectory = confDym.getProperty("document_file_path") +
                now.toString(fmtYYYYMM) + "/" + now.toString(fmtDD);
        documentAttachEntity.setFile_path(fileDirectory + "/" + fileName +
                (fileExtension == null ? "" : "." + fileExtension));

        // 이미지 파일인지 조사
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            bufferedImage = null;
        }

        documentAttachEntity.setFile_url("");

        if(bufferedImage != null) {
            documentAttachEntity.setWidth(bufferedImage.getWidth());
            documentAttachEntity.setHeight(bufferedImage.getHeight());
        } else {
            documentAttachEntity.setWidth(MDV.NONE);
            documentAttachEntity.setHeight(MDV.NONE);
        }

        documentAttachEntity.setIpaddress(requestIp);
        documentAttachEntity.setDeleted(MDV.YES);
        documentAttachEntity.setC_date((int) (now.getMillis() / 1000));
        documentAttachEntity.setU_date((int) (now.getMillis() / 1000));

        documentAttachEntity.setThumb_path("");
        documentAttachEntity.setThumb_url("");
        documentAttachEntity.setThumb_width(MDV.NONE);
        documentAttachEntity.setThumb_height(MDV.NONE);

        if(fileComment == null) fileComment = "";
        fileComment = StringUtils.trim(fileComment);

        documentAttachEntity.setFile_comment(fileComment);
        documentAttachEntity.setMember_srl(memberSrl);

        // 파일 저장할 디렉토리 생성
        File fDir = new File(fileDirectory);
        if(!fDir.exists()) {
            try {
                FileUtils.forceMkdir(fDir);
                LOG.info("make directory for file save. mkdir [" + fileDirectory + "]");
            } catch (IOException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mkdir", "failed mkdir [" + fileDirectory + "]. exeption [" + e.getMessage() + "]");
                LOG.error(reason.get("mkdir"));
                throw new CustomException("save_file_error", reason);
            }
        }

        // 파일 저장
        File fFile = new File(documentAttachEntity.getFile_path());
        try {
            multipartFile.transferTo(fFile);
            LOG.info("saved file [" + documentAttachEntity.getFile_path() + "]");
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("save_file", "failed save file [" + documentAttachEntity.getFile_path() + "]. exeption [" + e.getMessage() + "]");
            LOG.error(reason.get("save_file"));
            throw new CustomException("save_file_error", reason);
        }

        documentAttachDao.add(documentAttachEntity);
        LOG.info("add file. documentAttachEntity [" + documentAttachEntity.toString() + "]");

        return documentAttachEntity;
    }

    @Override
    public FileRepositoryEntity saveFile(int fileType, String fileURL, byte[] fileData, String userId,
                                         String requestIp, boolean isDelete) {
        if(fileData == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "file data is null");
            LOG.error(reason.get("no_file"));
            throw new CustomException("save_file_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        String mimeType = new Tika().detect(fileData);
        String fileExtension = "";
        int lastIndex = StringUtils.lastIndexOf(fileURL, ".");
        if(lastIndex >= 0) {
            fileExtension = StringUtils.substring(fileURL, lastIndex);
        }

        if(confExtension.containsKey(mimeType)) {
            String tmpExtension = confExtension.getProperty(mimeType);
            if(StringUtils.indexOf(tmpExtension, "ambiguous-") >= 0) {
                if(StringUtils.equals(fileExtension, "") || fileExtension.length() > 4)
                    fileExtension = "." + StringUtils.replace(tmpExtension, "ambiguous-", "");
            } else {
                fileExtension = "." + tmpExtension;
            }
        }

        DateTime now = DateTime.now();

        FileRepositoryEntity fileRepositoryEntity = new FileRepositoryEntity();
        fileRepositoryEntity.setOrig_name(Long.toString(now.getMillis())+fileExtension);
        fileRepositoryEntity.setFile_size(fileData.length);
        fileRepositoryEntity.setMime_type(mimeType);

        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileDirectory = confDym.getProperty("file_repository_path") +
                now.toString(fmtYYYYMM) + "/" + now.toString(fmtDD);
        fileRepositoryEntity.setFile_path(fileDirectory + "/" + fileName +
                (fileExtension == null ? "" : "." + fileExtension));

        // 이미지 파일인지 조사
        BufferedImage bufferedImage;
        InputStream is = new ByteArrayInputStream(fileData);
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            bufferedImage = null;
        } finally {
            try { is.close(); } catch (IOException e) {}
        }

        fileRepositoryEntity.setFile_url("");

        if(bufferedImage != null) {
            fileRepositoryEntity.setWidth(bufferedImage.getWidth());
            fileRepositoryEntity.setHeight(bufferedImage.getHeight());
        } else {
            fileRepositoryEntity.setWidth(MDV.NONE);
            fileRepositoryEntity.setHeight(MDV.NONE);
        }

        fileRepositoryEntity.setIpaddress(requestIp);
        fileRepositoryEntity.setDeleted(isDelete ? MDV.YES : MDV.NO);
        fileRepositoryEntity.setC_date((int) (now.getMillis() / 1000));
        fileRepositoryEntity.setU_date((int) (now.getMillis() / 1000));

        fileRepositoryEntity.setThumb_path("");
        fileRepositoryEntity.setThumb_url("");
        fileRepositoryEntity.setThumb_width(MDV.NONE);
        fileRepositoryEntity.setThumb_height(MDV.NONE);

        fileRepositoryEntity.setFile_comment("");
        fileRepositoryEntity.setFile_type(fileType);
        fileRepositoryEntity.setUser_id(userId == null ? "" : userId);

        // 파일 저장할 디렉토리 생성
        File fDir = new File(fileDirectory);
        if(!fDir.exists()) {
            try {
                FileUtils.forceMkdir(fDir);
                LOG.info("make directory for file save. mkdir [" + fileDirectory + "]");
            } catch (IOException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mkdir", "failed mkdir [" + fileDirectory + "]. exeption [" + e.getMessage() + "]");
                LOG.error(reason.get("mkdir"));
                throw new CustomException("save_file_error", reason);
            }
        }

        // 파일 저장
        File fFile = new File(fileRepositoryEntity.getFile_path());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fFile);
            IOUtils.write(fileData, fos);
            LOG.info("saved file [" + fileRepositoryEntity.getFile_path() + "]");
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("save_file", "failed save file [" + fileRepositoryEntity.getFile_path() + "]. exeption [" + e.getMessage() + "]");
            LOG.error(reason.get("save_file"));
            throw new CustomException("save_file_error", reason);
        } finally {
            if(fos != null) { try { fos.close(); } catch (IOException e) {} }
        }

        fileRepositoryDao.add(fileRepositoryEntity);
        LOG.info("add file. fileRepositoryEntity [" + fileRepositoryEntity.toString() + "]");

        return fileRepositoryEntity;
    }

    @Override
    public FileRepositoryEntity saveFile(MultipartFile multipartFile, String userId,
                                         String requestIp, boolean isDelete) {
        if(multipartFile == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "file data is null");
            LOG.error(reason.get("no_file"));
            throw new CustomException("save_file_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        FileRepositoryEntity fileRepositoryEntity = new FileRepositoryEntity();
        fileRepositoryEntity.setOrig_name(multipartFile.getOriginalFilename());
        fileRepositoryEntity.setFile_size(multipartFile.getSize());

        // mime type 을 체크 한다.
        String fileExtension = null, mimeType = "";
        try {
            Tika tika = new Tika();
            mimeType = tika.detect(multipartFile.getInputStream());
        } catch (IOException e) {
            LOG.warn("check file mime type exception. e [" + e.getMessage() + "]");
        }

        fileRepositoryEntity.setMime_type(mimeType);

        // png 인지 체크 한다.
        String[] mimePng = StringUtils.split(confCommon.getProperty("mime_type_png"), ",");
        for(String mime : mimePng) {
            if(mimeType.equals(StringUtils.trim(mime))) {
                fileExtension = "png";
                break;
            }
        }

        // jpg 인지 체크 한다.
        if(fileExtension == null) {
            String[] mimeJpg = StringUtils.split(confCommon.getProperty("mime_type_jpg"), ",");
            for(String mime : mimeJpg) {
                if(mimeType.equals(StringUtils.trim(mime))) {
                    fileExtension = "jpg";
                    break;
                }
            }
        }

        // mime type 에서 파일 확장자 구하기가 실패 하면 원본 파일의 확장자를 본다.
        if(fileExtension == null) {
            LOG.debug("can't find file extension by mime-type. use origin file extension");
            fileExtension = StringUtils.lowerCase(org.springframework.util.StringUtils.getFilenameExtension(fileRepositoryEntity.getOrig_name()));
        }

        if(fileExtension == null) {
            LOG.warn("not found file extension. set file extension is empty string");
        }

        DateTime now = DateTime.now();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileDirectory = confDym.getProperty("file_repository_path") +
                now.toString(fmtYYYYMM) + "/" + now.toString(fmtDD);
        fileRepositoryEntity.setFile_path(fileDirectory + "/" + fileName +
                (fileExtension == null ? "" : "." + fileExtension));

        // 이미지 파일인지 조사
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            bufferedImage = null;
        }

        fileRepositoryEntity.setFile_url("");

        if(bufferedImage != null) {
            fileRepositoryEntity.setWidth(bufferedImage.getWidth());
            fileRepositoryEntity.setHeight(bufferedImage.getHeight());
        } else {
            fileRepositoryEntity.setWidth(MDV.NONE);
            fileRepositoryEntity.setHeight(MDV.NONE);
        }

        fileRepositoryEntity.setIpaddress(requestIp);
        fileRepositoryEntity.setDeleted(isDelete ? MDV.YES : MDV.NO);
        fileRepositoryEntity.setC_date((int) (now.getMillis() / 1000));
        fileRepositoryEntity.setU_date((int) (now.getMillis() / 1000));

        fileRepositoryEntity.setThumb_path("");
        fileRepositoryEntity.setThumb_url("");
        fileRepositoryEntity.setThumb_width(MDV.NONE);
        fileRepositoryEntity.setThumb_height(MDV.NONE);

        fileRepositoryEntity.setFile_comment("");
        fileRepositoryEntity.setUser_id(userId == null ? "" : userId);

        // 파일 저장할 디렉토리 생성
        File fDir = new File(fileDirectory);
        if(!fDir.exists()) {
            try {
                FileUtils.forceMkdir(fDir);
                LOG.info("make directory for file save. mkdir [" + fileDirectory + "]");
            } catch (IOException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mkdir", "failed mkdir [" + fileDirectory + "]. exeption [" + e.getMessage() + "]");
                LOG.error(reason.get("mkdir"));
                throw new CustomException("save_file_error", reason);
            }
        }

        // 파일 저장
        File fFile = new File(fileRepositoryEntity.getFile_path());
        try {
            multipartFile.transferTo(fFile);
            LOG.info("saved file [" + fileRepositoryEntity.getFile_path() + "]");
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("save_file", "failed save file [" + fileRepositoryEntity.getFile_path() + "]. exeption [" + e.getMessage() + "]");
            LOG.error(reason.get("save_file"));
            throw new CustomException("save_file_error", reason);
        }

        fileRepositoryDao.add(fileRepositoryEntity);
        LOG.info("add file. fileRepositoryEntity [" + fileRepositoryEntity.toString() + "]");

        return fileRepositoryEntity;
    }

    @Override
    public FileEntity savePushImage(MultipartFile multipartFile, String requestIp,
                                    boolean isDelete) {
        if(multipartFile == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "file data is null");
            LOG.error(reason.get("no_file"));
            throw new CustomException("save_push_gcm_apns_image_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setOrig_name(multipartFile.getOriginalFilename());
        fileEntity.setFile_size(multipartFile.getSize());

        // mime type 을 체크 한다.
        String fileExtension = null;
        try {
            Tika tika = new Tika();
            String mimeType = tika.detect(multipartFile.getInputStream());

            // png 인지 체크 한다.
            String[] mimePng = StringUtils.split(confCommon.getProperty("mime_type_png"), ",");
            for(String mime : mimePng) {
                if(mimeType.equals(StringUtils.trim(mime))) {
                    fileExtension = "png";
                    break;
                }
            }

            // jpg 인지 체크 한다.
            if(fileExtension == null) {
                String[] mimeJpg = StringUtils.split(confCommon.getProperty("mime_type_jpg"), ",");
                for(String mime : mimeJpg) {
                    if(mimeType.equals(StringUtils.trim(mime))) {
                        fileExtension = "jpg";
                        break;
                    }
                }
            }
        } catch (IOException e) {
            LOG.warn("check file mime type exception. e [" + e.getMessage() + "]");
        }

        // mime type 에서 파일 확장자 구하기가 실패 하면 원본 파일의 확장자를 본다.
        if(fileExtension == null) {
            LOG.debug("can't find file extension by mime-type. use origin file extension");
            fileExtension = StringUtils.lowerCase(org.springframework.util.StringUtils.getFilenameExtension(fileEntity.getOrig_name()));
        }

        if(fileExtension == null) {
            LOG.warn("not found file extension. set file extension is empty string");
        }

        DateTime now = DateTime.now();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileDirectory = confDym.getProperty("push_message_image_path") +
                now.toString(fmtYYYYMM) + "/" + now.toString(fmtDD);
        fileEntity.setFile_path(fileDirectory + "/" + fileName +
                (fileExtension == null ? "" : "." + fileExtension));

        // 이미지 파일이기 때문에 이미지 사이즈를 계산 한다.
        // 이미지 파일이 아니라면 에러 떨군다. 뭔가 잘못 타고 들어온 것임.
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("read_image", "failed read push image. file name [" + fileEntity.getOrig_name() +
                    "], file_size [" + fileEntity.getFile_size() + "]");
            LOG.error(reason.get("read_image"));
            throw new CustomException("save_push_gcm_apns_image_error", reason);
        }

        // file_url 은 기존의 고정 방식에서 데이터 보여 줄때 지정하는 방식으로 변경
        fileEntity.setFile_url("");

        fileEntity.setWidth(bufferedImage.getWidth());
        fileEntity.setHeight(bufferedImage.getHeight());
        fileEntity.setIpaddress(requestIp);
        fileEntity.setDeleted(isDelete ? MDV.YES : MDV.NO);
        fileEntity.setC_date((int) (now.getMillis() / 1000));
        fileEntity.setU_date((int) (now.getMillis() / 1000));

        // NOTE 썸네일 생성 방법은 추후 고민해 보자. 기존 방식은 유용하지 못한 듯 보임.
        fileEntity.setThumb_path("");
        fileEntity.setThumb_url("");
        fileEntity.setThumb_width(MDV.NONE);
        fileEntity.setThumb_height(MDV.NONE);

        // 파일 저장할 디렉토리 생성
        File fDir = new File(fileDirectory);
        if(!fDir.exists()) {
            try {
                FileUtils.forceMkdir(fDir);
                LOG.info("make directory for save push image. mkdir [" + fileDirectory + "]");
            } catch (IOException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mkdir", "failed mkdir [" + fileDirectory + "]. exeption [" + e.getMessage() + "]");
                LOG.error(reason.get("mkdir"));
                throw new CustomException("save_push_gcm_apns_image_error", reason);
            }
        }

        // 파일 저장
        File fFile = new File(fileEntity.getFile_path());
        try {
            multipartFile.transferTo(fFile);
            LOG.info("saved push image. file [" + fileEntity.getFile_path() + "]");
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("save_file", "failed save file [" + fileEntity.getFile_path() + "]. exeption [" + e.getMessage() + "]");
            LOG.error(reason.get("save_file"));
            throw new CustomException("save_push_gcm_apns_image_error", reason);
        }

        fileDao.add(confTblFile.getProperty("tbl_name_push_file"), fileEntity);
        LOG.info("add push file. fileEntity [" + fileEntity.toString() + "]");

        return fileEntity;
    }

    @Override
    public FileEntity savePushImage(String fileURL, byte[] fileData, String requestIp, boolean isDelete) {
        if(fileData == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "file data is null");
            LOG.error(reason.get("no_file"));
            throw new CustomException("save_push_gcm_apns_image_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        String mimeType = new Tika().detect(fileData);
        String fileExtension = "";
        int lastIndex = StringUtils.lastIndexOf(fileURL, ".");
        if(lastIndex >= 0) {
            fileExtension = StringUtils.substring(fileURL, lastIndex);
        }

        if(confExtension.containsKey(mimeType)) {
            String tmpExtension = confExtension.getProperty(mimeType);
            if(StringUtils.indexOf(tmpExtension, "ambiguous-") >= 0) {
                if(StringUtils.equals(fileExtension, "") || fileExtension.length() > 4)
                    fileExtension = "." + StringUtils.replace(tmpExtension, "ambiguous-", "");
            } else {
                fileExtension = "." + tmpExtension;
            }
        }

        DateTime now = DateTime.now();

        FileEntity fileEntity = new FileEntity();
        fileEntity.setOrig_name(Long.toString(now.getMillis()) + fileExtension);
        fileEntity.setFile_size(fileData.length);

        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        String fileDirectory = confDym.getProperty("push_message_image_path") +
                now.toString(fmtYYYYMM) + "/" + now.toString(fmtDD);
        fileEntity.setFile_path(fileDirectory + "/" + fileName +
                (fileExtension == null ? "" : "." + fileExtension));

        // 이미지 파일인지 조사
        BufferedImage bufferedImage;
        InputStream is = new ByteArrayInputStream(fileData);
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            bufferedImage = null;
        } finally {
            try { is.close(); } catch (IOException e) {}
        }

        fileEntity.setFile_url("");

        if(bufferedImage != null) {
            fileEntity.setWidth(bufferedImage.getWidth());
            fileEntity.setHeight(bufferedImage.getHeight());
        } else {
            fileEntity.setWidth(MDV.NONE);
            fileEntity.setHeight(MDV.NONE);
        }

        fileEntity.setIpaddress(requestIp);
        fileEntity.setDeleted(isDelete ? MDV.YES : MDV.NO);
        fileEntity.setC_date((int) (now.getMillis() / 1000));
        fileEntity.setU_date((int) (now.getMillis() / 1000));

        fileEntity.setThumb_path("");
        fileEntity.setThumb_url("");
        fileEntity.setThumb_width(MDV.NONE);
        fileEntity.setThumb_height(MDV.NONE);

        // 파일 저장할 디렉토리 생성
        File fDir = new File(fileDirectory);
        if(!fDir.exists()) {
            try {
                FileUtils.forceMkdir(fDir);
                LOG.info("make directory for file save. mkdir [" + fileDirectory + "]");
            } catch (IOException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("mkdir", "failed mkdir [" + fileDirectory + "]. exeption [" + e.getMessage() + "]");
                LOG.error(reason.get("mkdir"));
                throw new CustomException("save_push_gcm_apns_image_error", reason);
            }
        }

        // 파일 저장
        File fFile = new File(fileEntity.getFile_path());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fFile);
            IOUtils.write(fileData, fos);
            LOG.info("saved file [" + fileEntity.getFile_path() + "]");
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("save_file", "failed save file [" + fileEntity.getFile_path() + "]. exeption [" + e.getMessage() + "]");
            LOG.error(reason.get("save_file"));
            throw new CustomException("save_push_gcm_apns_image_error", reason);
        } finally {
            if(fos != null) { try { fos.close(); } catch (IOException e) {} }
        }

        fileDao.add(confTblFile.getProperty("tbl_name_push_file"), fileEntity);
        LOG.info("add outer push file. fileEntity [" + fileEntity.toString() + "]");

        return fileEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public long getPushMessagePicCount(String origName, int deleted) {
        if(origName != null) {
            origName = StringUtils.trim(origName);
            if(StringUtils.equals(origName, "")) origName = null;
        }

        return fileDao.count(confTblFile.getProperty("tbl_name_push_file"), null, origName, deleted, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public long getPushMessagePicCount(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.getPushMessagePicCount(null, MDV.NUSE);

        String origName = null;
        int deleted = MDV.NUSE;

        if(searchFilter.containsKey("orig_name")) origName = searchFilter.get("orig_name");
        if(searchFilter.containsKey("deleted")) {
            String num = searchFilter.get("deleted");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deleted = Integer.parseInt(num, 10);
        }

        return this.getPushMessagePicCount(origName, deleted);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> getPushMessagePic(String origName, int deleted,
                                                       Map<String, String> sort, int offset, int limit) {
        if(origName != null) {
            origName = StringUtils.trim(origName);
            if(StringUtils.equals(origName, "")) origName = null;
        }

        // sort 의 direction 체크
        if(sort == null || sort.size() <= 0) sort = null;
        else {
            Set<String> keys = sort.keySet();
            for(String key : keys) {
                if(!StringUtils.equals(sort.get(key), "desc") && !StringUtils.equals(sort.get(key), "asc")) {
                    LOG.warn("invalid column sort direction. column [" + key +"], direction [" +
                            sort.get(key) + "]. set default desc direction");
                    sort.put(key, "desc");
                }
            }
        }

        List<Map<String, Object>> ret = new ArrayList<>();

        // 파일 리스트를 구한다.
        List<FileEntity> fileEntities = fileDao.get(confTblFile.getProperty("tbl_name_push_file"), null,
                deleted, origName, MDV.NUSE, sort, offset, limit);

        if(fileEntities.size() <= 0) return ret;

        Map<Long, FileEntity> fileEntityMap = new LinkedHashMap<>();
        Map<Long, List<PushMessageEntity>> pushMessageEntitiesMap = new LinkedHashMap<>();
        List<Long> fileSrls = new ArrayList<>();

        for(FileEntity fileEntity : fileEntities) {
            fileSrls.add(fileEntity.getFile_srl());
            fileEntityMap.put(fileEntity.getFile_srl(), fileEntity);
            pushMessageEntitiesMap.put(fileEntity.getFile_srl(), new ArrayList<PushMessageEntity>());
        }

        // push message, app 리스트를 구한다.
        List<PushMessagePicEntity> pushMessagePicEntities = pushMessagePicDao.get(MDV.NUSE, null,
                MDV.NUSE, fileSrls, MDV.NUSE, MDV.NUSE);

        if(pushMessagePicEntities.size() > 0) {
            List<Long> pushSrls = new ArrayList<>();
            List<Integer> appSrls = new ArrayList<>();

            for(PushMessagePicEntity pushMessagePicEntity : pushMessagePicEntities)
                pushSrls.add(pushMessagePicEntity.getPush_srl());

            // 실제 push message 리스트를 구한다.
            List<PushMessageEntity> pushMessageEntities = pushMessageDao.get(MDV.NUSE, pushSrls, null,
                    MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            Map<Long, PushMessageEntity> pushMessageEntityMap = new HashMap<>();
            for(PushMessageEntity pushMessageEntity : pushMessageEntities) {
                pushMessageEntityMap.put(pushMessageEntity.getPush_srl(), pushMessageEntity);
                appSrls.add(pushMessageEntity.getApp_srl());
            }

            // 이미지와 메시지를 묶는다.
            for(PushMessagePicEntity pushMessagePicEntity : pushMessagePicEntities) {
                List<PushMessageEntity> tmp = pushMessageEntitiesMap.get(pushMessagePicEntity.getFile_srl());

                if(!tmp.contains(pushMessageEntityMap.get(pushMessagePicEntity.getPush_srl())))
                    tmp.add(pushMessageEntityMap.get(pushMessagePicEntity.getPush_srl()));
            }

            // 실제 app 리스트를 구한다.
            List<AppEntity> appEntities = appDao.get(appSrls, null, null, null, MDV.NUSE,
                    MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            Map<Integer, AppEntity> appEntityMap = new HashMap<>();
            for(AppEntity appEntity : appEntities) appEntityMap.put(appEntity.getApp_srl(), appEntity);

            // 최종으로 file, message, app 을 묶는다.
            Set<Long> keys = pushMessageEntitiesMap.keySet();
            for(long fileSrl : keys) {
                Map<String, Object> map = new HashMap<>();

                map.put("image", fileEntityMap.get(fileSrl));

                List<PushMessageEntity> messageEntities = pushMessageEntitiesMap.get(fileSrl);
                map.put("message", messageEntities);

                List<AppEntity> appEntityList = new ArrayList<>();
                for(PushMessageEntity messageEntity : messageEntities) {
                    appEntityList.add(appEntityMap.get(messageEntity.getApp_srl()));
                }
                map.put("app", appEntityList);

                ret.add(map);
            }
        } else {
            for(FileEntity fileEntity : fileEntities) {
                Map<String, Object> map = new HashMap<>();
                map.put("image", fileEntity);
                ret.add(map);
            }
        }

        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> getPushMessagePic(Map<String, String> searchFilter,
                                                       Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null) return this.getPushMessagePic(null, MDV.NUSE, null, offset, limit);

        String origName = null;
        int deleted = MDV.NUSE;

        if(searchFilter.containsKey("orig_name")) origName = searchFilter.get("orig_name");
        if(searchFilter.containsKey("deleted")) {
            String num = searchFilter.get("deleted");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) deleted = Integer.parseInt(num, 10);
        }

        return this.getPushMessagePic(origName, deleted, sort, offset, limit);
    }

    /**
     * repository 에 저장된 이미지를 읽는다.
     *
     * @param fileSrl 이미지 파일 시리얼 넘버
     * @return 오류 발생 한다면 404 에러를 던진다. 성공한다면 파일 정보가 들어 있는 map 을 리턴한다.
     *         ext      : 파일의 extension
     *         image    : BufferedImage object
     */
    private Map<String, Object> readRepositoryImage(long fileSrl) {
        if(fileSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "invalid file_srl. repository file_srl is less then zero");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        FileRepositoryEntity fileRepositoryEntity = fileRepositoryDao.get(fileSrl, MDV.NO);
        if(fileRepositoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "not found file data of file_srl. repository file_srl [" + fileSrl + "]");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = org.springframework.util.StringUtils.getFilenameExtension(fileRepositoryEntity.getFile_path());

        if(extension == null || StringUtils.equals(extension, "")) extension = "png";
        else extension = StringUtils.lowerCase(extension);

        File fImage = new File(fileRepositoryEntity.getFile_path());
        if(!fImage.exists()) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "not found repository file.");
            LOG.error(reason.get("no_file") + "file_path [" + fileRepositoryEntity.getFile_path() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(fImage);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("process_file", "failed processing repository image file. maybe not image file");
            LOG.error(reason.get("process_file") + " file_path [" + fileRepositoryEntity.getFile_path() + "]. e [" +
                    e.getMessage() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("ext", extension);
        ret.put("image", bufferedImage);

        return ret;
    }

    /**
     * push message 에서 사용하는 이미지를 읽는다.
     *
     * @param fileSrl 이미지 파일 시리얼 넘버
     * @return 오류 발생 한다면 404 에러를 던진다. 성공한다면 파일 정보가 들어 있는 map 을 리턴한다.
     *         ext      : 파일의 extension
     *         image    : BufferedImage object
     */
    private Map<String, Object> readPushMessageImage(long fileSrl) {
        if(fileSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "invalid file_srl. push file_srl is less then zero");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("read_image_for_gcm_apns_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        FileEntity fileEntity = fileDao.get(confTblFile.getProperty("tbl_name_push_file"), fileSrl, MDV.NO);
        if(fileEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "not found file data of file_srl. push file_srl [" + fileSrl + "]");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("read_image_for_gcm_apns_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = org.springframework.util.StringUtils.getFilenameExtension(fileEntity.getFile_path());

        if(extension == null || StringUtils.equals(extension, "")) extension = "png";
        else extension = StringUtils.lowerCase(extension);

        File fImage = new File(fileEntity.getFile_path());
        if(!fImage.exists()) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "not found push file.");
            LOG.error(reason.get("no_file") + "file_path [" + fileEntity.getFile_path() + "]");
            throw new CustomException("read_image_for_gcm_apns_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(fImage);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("process_file", "failed processing push image file. maybe not image file");
            LOG.error(reason.get("process_file") + " file_path [" + fileEntity.getFile_path() + "]. e [" +
                    e.getMessage() + "]");
            throw new CustomException("read_image_for_gcm_apns_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("ext", extension);
        ret.put("image", bufferedImage);

        return ret;
    }

    /**
     * 게시물 첨부에서 사용하는 이미지를 읽는다.
     *
     * @param fileSrl 이미지 파일 시리얼 넘버
     * @return 오류 발생 한다면 404 에러를 던진다. 성공한다면 파일 정보가 들어 있는 map 을 리턴한다.
     *         ext      : 파일의 extension
     *         image    : BufferedImage object
     */
    private Map<String, Object> readDocumentAttachImage(long fileSrl) {
        if(fileSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "invalid file_srl. document attach file_srl is less then zero");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        // 삭제 여부 상관 없이 파일을 읽는다.
        DocumentAttachEntity documentAttachEntity = documentAttachDao.get(fileSrl, MDV.NUSE);
        if(documentAttachEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("file_srl", "not found file data of file_srl. document attach file_srl [" + fileSrl + "]");
            LOG.error(reason.get("file_srl"));
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = org.springframework.util.StringUtils.getFilenameExtension(documentAttachEntity.getFile_path());

        if(extension == null || StringUtils.equals(extension, "")) extension = "png";
        else extension = StringUtils.lowerCase(extension);

        File fImage = new File(documentAttachEntity.getFile_path());
        if(!fImage.exists()) {
            Map<String, String> reason = new HashMap<>();
            reason.put("no_file", "not found document attach file.");
            LOG.error(reason.get("no_file") + "file_path [" + documentAttachEntity.getFile_path() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(fImage);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("process_file", "failed processing document attach image file. maybe not image file");
            LOG.error(reason.get("process_file") + " file_path [" + documentAttachEntity.getFile_path() + "]. e [" +
                    e.getMessage() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("ext", extension);
        ret.put("image", bufferedImage);

        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getImageFileRatio(long fileSrl, double ratio, int fileType) {

        // TODO 추후 로직을 타지 않게 한번 변경 해 둔 것은 파일로 저장하고, 파일에서 읽는 로직을 추가 해야 함

        Map<String, Object> map;

        switch (fileType) {
            case MDV.FILE_REPOSITORY:
                map = this.readRepositoryImage(fileSrl);
                break;

            case MDV.FILE_MSG_PUSH:
                map = this.readPushMessageImage(fileSrl);
                break;

            case MDV.FILE_DOC_ATTACH:
                map = this.readDocumentAttachImage(fileSrl);
                break;

            default:
                Map<String, String> reason = new HashMap<>();
                reason.put("image_type", "not supported image_type [" + fileType + "]");
                LOG.error(reason.get("image_type"));
                throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = (String) map.get("ext");
        BufferedImage bufferedImage = (BufferedImage) map.get("image");

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int neoWidth = (int) (ratio * width);
        int neoHeight = (int) (ratio * height);

        Image ratioImage = bufferedImage.getScaledInstance(neoWidth, neoHeight, Image.SCALE_SMOOTH);

        BufferedImage ratioBufferedImage = new BufferedImage(neoWidth, neoHeight,
                StringUtils.equals(extension, "png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = ratioBufferedImage.createGraphics();
        graphics2D.drawImage(ratioImage, 0, 0, null);
        graphics2D.dispose();

        LOG.info("ratio message image. ratio [" + ratio + "], file_srl [" + fileSrl + "]");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(ratioBufferedImage, extension, byteArrayOutputStream);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("write_file", "failed write ratio image file. file_srl [" +
                    fileSrl + "]");
            LOG.error(reason.get("write_file") + ". e [" + e.getMessage() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getImageFileResize(long fileSrl, int neoWidth, int neoHeight, int fileType) {

        // TODO 추후 로직을 타지 않게 한번 변경 해 둔 것은 파일로 저장하고, 파일에서 읽는 로직을 추가 해야 함

        Map<String, Object> map;

        switch (fileType) {
            case MDV.FILE_REPOSITORY:
                map = this.readRepositoryImage(fileSrl);
                break;

            case MDV.FILE_MSG_PUSH:
                map = this.readPushMessageImage(fileSrl);
                break;

            case MDV.FILE_DOC_ATTACH:
                map = this.readDocumentAttachImage(fileSrl);
                break;

            default:
                Map<String, String> reason = new HashMap<>();
                reason.put("image_type", "not supported image_type [" + fileType + "]");
                LOG.error(reason.get("image_type"));
                throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = (String) map.get("ext");
        BufferedImage bufferedImage = (BufferedImage) map.get("image");

        Image ratioImage = bufferedImage.getScaledInstance(neoWidth, neoHeight, Image.SCALE_SMOOTH);

        BufferedImage ratioBufferedImage = new BufferedImage(neoWidth, neoHeight,
                StringUtils.equals(extension, "png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = ratioBufferedImage.createGraphics();
        graphics2D.drawImage(ratioImage, 0, 0, null);
        graphics2D.dispose();

        LOG.info("resize image. resize w [" + neoWidth + "], h [ " +
                        neoHeight + "], file_srl [" + fileSrl + "]");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(ratioBufferedImage, extension, byteArrayOutputStream);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("write_file", "failed write ratio image file. file_srl [" +
                    fileSrl + "]");
            LOG.error(reason.get("write_file") + ". e [" + e.getMessage() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getImageFileCrop(long fileSrl, int x, int y, int neoWidth, int neoHeight, int fileType) {

        // TODO 추후 로직을 타지 않게 한번 변경 해 둔 것은 파일로 저장하고, 파일에서 읽는 로직을 추가 해야 함

        Map<String, Object> map;

        switch (fileType) {
            case MDV.FILE_REPOSITORY:
                map = this.readRepositoryImage(fileSrl);
                break;

            case MDV.FILE_MSG_PUSH:
                map = this.readPushMessageImage(fileSrl);
                break;

            case MDV.FILE_DOC_ATTACH:
                map = this.readDocumentAttachImage(fileSrl);
                break;

            default:
                Map<String, String> reason = new HashMap<>();
                reason.put("image_type", "not supported image_type [" + fileType + "]");
                LOG.error(reason.get("image_type"));
                throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = (String) map.get("ext");
        BufferedImage bufferedImage = (BufferedImage) map.get("image");

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int restWidth = width - x;
        int restHeight = height - y;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if(restWidth <= 0 || restHeight <= 0) {
            LOG.warn("ignore crop image. invalid crop position. crop position [" + x + "], y [" +
                y + "]. image size width [ " + width + "], height [" + height + "]. show original image");

            try {
                ImageIO.write(bufferedImage, extension, byteArrayOutputStream);
            } catch (IOException e) {
                Map<String, String> reason = new HashMap<>();
                reason.put("write_file", "failed write original image file. file_srl [" +
                        fileSrl + "]");
                LOG.error(reason.get("write_file") + ". e [" + e.getMessage() + "]");
                throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
            }

            return byteArrayOutputStream.toByteArray();
        }

        BufferedImage cropBufferedImage = bufferedImage.getSubimage(x, y, neoWidth, neoHeight);

        try {
            ImageIO.write(cropBufferedImage, extension, byteArrayOutputStream);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("write_file", "failed write crop image file. file_srl [" +
                    fileSrl + "]");
            LOG.error(reason.get("write_file") + ". e [" + e.getMessage() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        LOG.info("crop image. x [" + x + "], y [" + y + "], width [" +
                neoWidth + "], height [" + neoHeight + "], file_srl [" + fileSrl + "]");

        return byteArrayOutputStream.toByteArray();
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getImageFile(long fileSrl, int fileType) {
        Map<String, Object> map;

        switch (fileType) {
            case MDV.FILE_REPOSITORY:
                map = this.readRepositoryImage(fileSrl);
                break;

            case MDV.FILE_MSG_PUSH:
                map = this.readPushMessageImage(fileSrl);
                break;

            case MDV.FILE_DOC_ATTACH:
                map = this.readDocumentAttachImage(fileSrl);
                break;

            default:
                Map<String, String> reason = new HashMap<>();
                reason.put("image_type", "not supported image_type [" + fileType + "]");
                LOG.error(reason.get("image_type"));
                throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        String extension = (String) map.get("ext");
        BufferedImage bufferedImage = (BufferedImage) map.get("image");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, extension, byteArrayOutputStream);
        } catch (IOException e) {
            Map<String, String> reason = new HashMap<>();
            reason.put("write_file", "failed write original image file. file_srl [" +
                    fileSrl + "]");
            LOG.error(reason.get("write_file") + ". e [" + e.getMessage() + "]");
            throw new CustomException("read_file_error", MDV.HTTP_ERR_X_REQUEST_404, reason);
        }

        return byteArrayOutputStream.toByteArray();
    }

}
