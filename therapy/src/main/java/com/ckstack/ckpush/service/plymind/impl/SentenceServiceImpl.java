package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.board.*;
import com.ckstack.ckpush.dao.mineral.DocumentAttachDao;
import com.ckstack.ckpush.dao.mineral.DocumentFileDao;
import com.ckstack.ckpush.dao.plymind.SentenceDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.*;
import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import com.ckstack.ckpush.domain.mineral.DocumentFileEntity;
import com.ckstack.ckpush.domain.plymind.PushEntity;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.SentenceService;
import com.ckstack.ckpush.service.push.PlymindMessageService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class SentenceServiceImpl implements SentenceService {
    private final static Logger LOG = LoggerFactory.getLogger(SentenceServiceImpl.class);

    @Autowired
    protected Properties confSvc;

    @Autowired
    private AppDao appDao;

    @Autowired
    private DocumentCategoryDao documentCategoryDao;

    @Autowired
    private DocumentBoardDao documentBoardDao;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private DocumentTagDao documentTagDao;

    @Autowired
    private DocumentAttachDao documentAttachDao;

    @Autowired
    private DocumentFileDao documentFileDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private SentenceDao sentenceDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PlymindMessageService plymindMessageService;

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countSentence() {
        long paymentCount = sentenceDao.countSentence(null);

        return paymentCount;
    }

    /**
     * 해당 고객의 결제내역 중 검색 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countSearchSentence(Map<String, String> searchFilter) {
        String push_text = null;

        if(searchFilter != null && searchFilter.containsKey("push_text")) push_text = searchFilter.get("push_text");

        long paymentSearchCount = sentenceDao.countSentence(push_text);

        return paymentSearchCount;
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PushEntity> getSentenceList(Map<String, String> searchFilter, Map<String, String> sortValue, int offset, int limit) {
        String push_text = null;

        if(searchFilter != null && searchFilter.containsKey("push_text")) push_text = searchFilter.get("push_text");

        List<PushEntity> pushEntities = sentenceDao.getSentenceList(push_text, sortValue, offset, limit);

        return pushEntities;
    }

    @Override
    public void addDocument(DocumentEntity documentEntity) {
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentEntity", "invalid documentEntity is null");
            LOG.error(reason.get("documentEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        // app 체크
        if(documentEntity.getApp_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appDao.get(documentEntity.getApp_srl(), null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. not found app. app_srl [" +
                    documentEntity.getApp_srl() + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        // board 체크
        if(documentEntity.getBoard_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "invalid board_srl. board_srl is less then zero");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentBoardEntity documentBoardEntity = documentBoardDao.get(documentEntity.getBoard_srl(), MDV.NUSE);
        if(documentBoardEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "invalid board_srl. not found app. board_srl [" +
                    documentEntity.getBoard_srl() + "]");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        // category 체크
        if(documentEntity.getCategory_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srl", "invalid category_srl. category_srl is less then zero");
            LOG.error(reason.get("category_srl"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentCategoryEntity documentCategoryEntity = documentCategoryDao.get(documentEntity.getCategory_srl(), MDV.NUSE);
        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srl", "invalid category_srl. not found app. category_srl [" +
                    documentEntity.getCategory_srl() + "]");
            LOG.error(reason.get("category_srl"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getDocument_title() == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_title", "invalid document_title. document_title is null");
            LOG.error(reason.get("document_title"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        documentEntity.setDocument_title(StringUtils.trim(documentEntity.getDocument_title()));
        if(StringUtils.equals(documentEntity.getDocument_title(), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_title", "invalid document_title. document_title is empty string");
            LOG.error(reason.get("document_title"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getDocument_content() == null) documentEntity.setDocument_content("");

        documentEntity.setRead_count(0);
        documentEntity.setLike_count(0);
        documentEntity.setBlame_count(0);
        documentEntity.setComment_count(0);
        documentEntity.setFile_count(0);

        if(documentEntity.getOuter_link() == null) documentEntity.setOuter_link("");
        documentEntity.setOuter_link(StringUtils.trim(documentEntity.getOuter_link()));

        if(documentEntity.getSecret() != MDV.SECRET_DOCUMENT_OF_PUBLIC_CATEGORY &&
                documentEntity.getSecret() != MDV.NONE && documentEntity.getSecret() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("secret", "invalid secret. secret is unknown value. secret [" +
                    documentEntity.getSecret() + "]");
            LOG.error(reason.get("secret"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getBlock() != MDV.YES && documentEntity.getBlock() != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("block", "invalid block. block is unknown value. block [" +
                    documentEntity.getBlock() + "]");
            LOG.error(reason.get("block"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getAllow_comment() != MDV.YES && documentEntity.getAllow_comment() != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("allow_comment", "invalid allow_comment. allow_comment is unknown value. block [" +
                    documentEntity.getAllow_comment() + "]");
            LOG.error(reason.get("allow_comment"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentEntity.getAllow_notice() != MDV.YES && documentEntity.getAllow_notice() != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("allow_notice", "invalid allow_notice. allow_notice is unknown value. block [" +
                    documentEntity.getAllow_notice() + "]");
            LOG.error(reason.get("allow_notice"));
            throw new CustomException("add_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        documentEntity.setList_order(MDV.NONE);

        if(documentEntity.getTemplate_srl() <= 0) documentEntity.setTemplate_srl(MDV.NONE);

        if(documentEntity.getTemplate_extra() == null) documentEntity.setTemplate_extra("");
        documentEntity.setTemplate_extra(StringUtils.trim(documentEntity.getTemplate_extra()));

        if(documentEntity.getMember_srl() <= 0) documentEntity.setMember_srl(MDV.NONE);

        if(documentEntity.getUser_id() == null) documentEntity.setUser_id("");
        documentEntity.setUser_id(StringUtils.trim(documentEntity.getUser_id()));

        if(documentEntity.getUser_name() == null) documentEntity.setUser_name("");
        documentEntity.setUser_name(StringUtils.trim(documentEntity.getUser_name()));

        if(documentEntity.getNick_name() == null) documentEntity.setNick_name("");
        documentEntity.setNick_name(StringUtils.trim(documentEntity.getNick_name()));

        if(documentEntity.getEmail_address() == null) documentEntity.setEmail_address("");
        documentEntity.setEmail_address(StringUtils.trim(documentEntity.getEmail_address()));

        if(documentEntity.getDocument_password() == null) documentEntity.setDocument_password("");
        documentEntity.setDocument_password(StringUtils.trim(documentEntity.getDocument_password()));

        if(documentEntity.getIpaddress() == null) documentEntity.setIpaddress("");
        documentEntity.setIpaddress(StringUtils.trim(documentEntity.getIpaddress()));

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        documentEntity.setC_date(ltm);
        documentEntity.setU_date(ltm);

        // add document
        documentDao.add(documentEntity);
        LOG.info("add document. documentEntity [" + documentEntity.toString() + "]");

        // add admin tag
        this.setTag(documentEntity.getAdmin_tags(), documentEntity.getApp_srl(),
                documentEntity.getMember_srl(), documentEntity.getDocument_srl(), ltm, MDV.TAG_ADMIN);

        // add user tag
        this.setTag(documentEntity.getUser_tags(), documentEntity.getApp_srl(),
                documentEntity.getMember_srl(), documentEntity.getDocument_srl(), ltm, MDV.TAG_USER);

        // add attach file
        int fileCount = 0;
        if(documentEntity.getAttach_file_srls() != null && documentEntity.getAttach_file_srls().size() > 0) {
            DocumentFileEntity documentFileEntity = new DocumentFileEntity();
            documentFileEntity.setDocument_srl(documentEntity.getDocument_srl());
            documentFileEntity.setC_date(ltm);

            for(long fileSrl : documentEntity.getAttach_file_srls()) {
                documentFileEntity.setFile_srl(fileSrl);
                documentFileDao.add(documentFileEntity);
                LOG.info("mapping file to document. document_srl [" + documentEntity.getDocument_srl() +
                        "], file_srl [" + fileSrl + "]");
            }

            DocumentAttachEntity updateVo = new DocumentAttachEntity();
            updateVo.init();
            updateVo.setDeleted(MDV.NO);

            documentAttachDao.modify(updateVo, MDV.NUSE, documentEntity.getAttach_file_srls());
            LOG.info("document attach file activate. file_srls [" +
                    documentEntity.getAttach_file_srls().toString() + "]");

            fileCount = documentEntity.getAttach_file_srls().size();
        }

        // 게시물 업데이트
        DocumentEntity documentUpdateEntity = new DocumentEntity();
        documentUpdateEntity.init();
        documentUpdateEntity.setFile_count(fileCount);
        documentUpdateEntity.setList_order(documentEntity.getDocument_srl());

        documentDao.modify(documentUpdateEntity, documentEntity.getDocument_srl(), MDV.NUSE, MDV.NUSE);
        LOG.info("set document list_order and file_count [" + fileCount + "]");

        /* PUSH 발송 정보 저장 START */
        String templateExtra = StringUtils.trim(documentEntity.getTemplate_extra());

        int noti_time1 = 0;
        try {
            JSONObject templateExtraJSON = JSONObject.fromObject(templateExtra);

            noti_time1 = (int)(Long.parseLong(templateExtraJSON.get("send").toString())/1000L);
        } catch(Exception e) {
            LOG.debug(e.getMessage());
        }

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setMember_srl(MDV.NONE);
        notificationEntity.setUser_id(null);
        notificationEntity.setApplication_srl(MDV.NONE);
        notificationEntity.setApplication_group(MDV.NONE);
        notificationEntity.setNoti_type(1);  //1=너나들이, 2=공지사항, 3=그룹테라피, 4=개인테라피
        notificationEntity.setPush_text(documentEntity.getDocument_content());
        notificationEntity.setBook_time(noti_time1);
        notificationEntity.setPush_status(1); //1=발송전, 2=발송

        plymindMessageService.add(notificationEntity);
        /* PUSH 발송 정보 저장 END */
    }

    /**
     * 게시물의 태그를 세팅 한다. 태그 생성및 매핑
     *
     * @param tags 생성할 태그 정보
     * @param appSrl 태그가 생성되는 앱 시리얼 넘버
     * @param memberSrl 태그를 생성한 사용자의 시리얼 넘버
     * @param documentSrl 태그를 매핑할 게시물 시리얼 넘버
     * @param ltm 생성일. unix timestamp 형태
     * @param tagType 태그 형태. 관리자, 사용자 구분
     */
    private void setTag(List<Map<String, Object>> tags, int appSrl, long memberSrl,
                        long documentSrl, int ltm, int tagType) {
        if(tags != null && tags.size() > 0) {
            List<Long> mappingTagSrls = new ArrayList<>();
            List<String> createTagName = new ArrayList<>();

            for(Map<String, Object> map : tags) {
                if(map.containsKey("admin_tag")) {
                    String strTagSrl = StringUtils.trim(map.get("tag_srl").toString());
                    if(NumberUtils.isNumber(strTagSrl)) {
                        mappingTagSrls.add(Long.parseLong(strTagSrl, 10));
                    } else {
                        createTagName.add(strTagSrl);
                    }
                } else {
                    createTagName.add(StringUtils.trim(map.get("tag_name").toString()));
                }
            }

            // 신규 태그를 생성 한다.
            TagEntity tagEntity = new TagEntity();
            tagEntity.setApp_srl(appSrl);
            tagEntity.setMember_srl(memberSrl);
            tagEntity.setAdmin_tag(tagType);
            tagEntity.setC_date(ltm);
            tagEntity.setU_date(ltm);

            for(String tagName : createTagName) {
                tagEntity.setTag_name(tagName);
                tagDao.add(tagEntity);
                LOG.info("add admin tag. tag_srl [" + tagEntity.getTag_srl() + "], app_srl [" +
                        tagEntity.getApp_srl() + "]");

                mappingTagSrls.add(tagEntity.getTag_srl());
            }

            // 태그를 매핑 한다.
            DocumentTagEntity documentTagEntity = new DocumentTagEntity();
            documentTagEntity.setDocument_srl(documentSrl);
            documentTagEntity.setC_date(ltm);

            for(long tagSrl : mappingTagSrls) {
                documentTagEntity.setTag_srl(tagSrl);
                documentTagDao.add(documentTagEntity);
                LOG.info("add document admin tag mapping. document_srl [" + documentTagEntity.getDocument_srl() +
                        "], tag_srl [" + documentTagEntity.getTag_srl() + "]");
            }
        }
    }
}
