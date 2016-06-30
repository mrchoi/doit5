package com.ckstack.ckpush.service.board.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.board.*;
import com.ckstack.ckpush.dao.mineral.DocumentAttachDao;
import com.ckstack.ckpush.dao.mineral.DocumentFileDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.*;
import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import com.ckstack.ckpush.domain.mineral.DocumentFileEntity;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.service.board.DocumentService;
import com.ckstack.ckpush.service.mineral.CkFileService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by dhkim94 on 15. 7. 24..
 */
@Service
@Transactional(value = "transactionManager")
public class DocumentServiceImpl implements DocumentService {
    private final static Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    private DocumentAttachDao documentAttachDao;
    @Autowired
    private DocumentFileDao documentFileDao;
    @Autowired
    private DocumentTagDao documentTagDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private DocumentCategoryDao documentCategoryDao;
    @Autowired
    private DocumentBoardDao documentBoardDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private AppTemplateDao appTemplateDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private DocumentLinkDao documentLinkDao;
    @Autowired
    private CkFileService ckFileService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;
    @Autowired
    protected Properties confAdmin;
    @Autowired
    private PlymindMessageService plymindMessageService;


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

    /**
     * 게시판, 카테고리 리스트, 게시물 카운트를 구하기 위한 메소드를 위한 helper 메소드
     *
     * @param documentCategoryEntity 카테고리 entity
     * @param categoryListOfBoard output
     */
    private void getBoardCategoryInAppHelper(DocumentCategoryEntity documentCategoryEntity,
                                             Map<Long, List<Map<String, Object>>> categoryListOfBoard) {
        long boardSrl = documentCategoryEntity.getBoard_srl();
        Map<String, Object> cmap = new HashMap<>();
        cmap.put("category_srl", documentCategoryEntity.getCategory_srl());
        cmap.put("board_srl", boardSrl);
        cmap.put("category_name", documentCategoryEntity.getCategory_name());
        cmap.put("category_description", documentCategoryEntity.getCategory_description());
        cmap.put("category_type", documentCategoryEntity.getCategory_type());
        cmap.put("enabled", documentCategoryEntity.getEnabled());
        cmap.put("open_type", documentCategoryEntity.getOpen_type());
        cmap.put("document_count", documentCategoryEntity.getDocument_count());

        if(!categoryListOfBoard.containsKey(boardSrl))
            categoryListOfBoard.put(boardSrl, new ArrayList<Map<String, Object>>());

        List<Map<String, Object>> tmpList = categoryListOfBoard.get(boardSrl);
        tmpList.add(cmap);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getBoardCategoryInApp(int appSrl, int categoryType, int openType,
                                                           int enabled, List<Long> secrets, int block) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl is less then zero");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("list_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(categoryType != MDV.LINK_CATEGORY && categoryType != MDV.NORMAL_CATEGORY) categoryType = MDV.NUSE;
        if(openType != MDV.PUBLIC && openType != MDV.PRIVATE) openType = MDV.NUSE;
        if(enabled != MDV.YES && enabled != MDV.NO) enabled = MDV.NUSE;

        Map<String, String> boardSort = new HashMap<>();
        boardSort.put("board_name", "ASC");
        List<DocumentBoardEntity> documentBoardEntities = documentBoardDao.get(null, appSrl, null, enabled,
                openType, boardSort, MDV.NUSE, MDV.NUSE);

        // return 값
        List<Map<String, Object>> ret = new ArrayList<>();

        if(documentBoardEntities.size() <= 0) return ret;

        // 게시판 시리얼 넘버 리스트를 구한다.
        List<Long> boardSrls = new ArrayList<>();
        for(DocumentBoardEntity documentBoardEntity : documentBoardEntities) {
            long boardSrl = documentBoardEntity.getBoard_srl();
            if(!boardSrls.contains(boardSrl)) boardSrls.add(boardSrl);
        }

        Map<String, String> categorySort = new HashMap<>();
        categorySort.put("category_name", "ASC");
        List<DocumentCategoryEntity> documentCategoryEntities = documentCategoryDao.get(null, appSrl,
                MDV.NUSE, boardSrls, null, categoryType, enabled, openType, categorySort, MDV.NUSE, MDV.NUSE);

        // 카테고리 시리얼 넘버 리스트를 구한다.
        List<Long> normalPrivateCategorySrls = new ArrayList<>();
        List<Long> normalPublicCategorySrls = new ArrayList<>();
        List<Long> linkPrivateCategorySrls = new ArrayList<>();
        List<Long> linkPublicCategorySrls = new ArrayList<>();
        for(DocumentCategoryEntity documentCategoryEntity : documentCategoryEntities) {
            long categorySrl = documentCategoryEntity.getCategory_srl();

            if(documentCategoryEntity.getCategory_type() == MDV.NORMAL_CATEGORY) {
                if(documentCategoryEntity.getOpen_type() == MDV.PUBLIC) {
                    if(!normalPublicCategorySrls.contains(categorySrl)) normalPublicCategorySrls.add(categorySrl);
                } else {
                    if(!normalPrivateCategorySrls.contains(categorySrl)) normalPrivateCategorySrls.add(categorySrl);
                }
            } else {
                if(documentCategoryEntity.getOpen_type() == MDV.PUBLIC) {
                    if(!linkPublicCategorySrls.contains(categorySrl)) linkPublicCategorySrls.add(categorySrl);
                } else {
                    if(!linkPrivateCategorySrls.contains(categorySrl)) linkPrivateCategorySrls.add(categorySrl);
                }
            }
        }

        // 카테고리에 포함되어 있는 게시물 카운트
        Map<Long, Long> documentCountInCategory = new HashMap<>();

        // 1. 링크 카테고리(공개) 일때 링크 게시물 카운트
        if(linkPublicCategorySrls.size() > 0) {
            List<Map<String, Object>> documentCount = documentCategoryDao.countDocumentLink(MDV.NUSE,
                    linkPublicCategorySrls, MDV.NUSE, null, block);
            for(Map<String, Object> map : documentCount) {
                long categorySrl = (long) map.get("category_srl");
                long cnt = (long) map.get("cnt");
                documentCountInCategory.put(categorySrl, cnt);
            }
        }

        // 2. 링크 카테고리(비공개) 일때 링크 게시물 카운트
        if(linkPrivateCategorySrls.size() > 0) {
            List<Map<String, Object>> documentCount = documentCategoryDao.countDocumentLink(MDV.NUSE,
                    linkPrivateCategorySrls, MDV.NUSE, secrets, block);
            for(Map<String, Object> map : documentCount) {
                long categorySrl = (long) map.get("category_srl");
                long cnt = (long) map.get("cnt");
                documentCountInCategory.put(categorySrl, cnt);
            }
        }

        // 3. 일반 카테고리(공개) 일때 게시물 카운트
        if(normalPublicCategorySrls.size() > 0) {
            List<Map<String, Object>> documentCount = documentCategoryDao.countDocument(MDV.NUSE,
                    normalPublicCategorySrls, MDV.NUSE, null, block);
            for(Map<String, Object> map : documentCount) {
                long categorySrl = (long) map.get("category_srl");
                long cnt = (long) map.get("cnt");
                documentCountInCategory.put(categorySrl, cnt);
            }
        }

        // 4. 일반 카테고리(비공개) 일때 게시물 카운트
        if(normalPrivateCategorySrls.size() > 0) {
            List<Map<String, Object>> documentCount = documentCategoryDao.countDocument(MDV.NUSE,
                    normalPrivateCategorySrls, MDV.NUSE, secrets, block);
            for(Map<String, Object> map : documentCount) {
                long categorySrl = (long) map.get("category_srl");
                long cnt = (long) map.get("cnt");
                documentCountInCategory.put(categorySrl, cnt);
            }
        }

        // category_type, open_type으로 원하는 카테고리의 정보만 필터링 한다.
        // 게시판에 넣을 게시판별 카테고리 리스트를 만든다.
        Map<Long, List<Map<String, Object>>> categoryListOfBoard = new HashMap<>();

        for(DocumentCategoryEntity documentCategoryEntity : documentCategoryEntities) {
            long categorySrl = documentCategoryEntity.getCategory_srl();

            if(documentCountInCategory.containsKey(categorySrl))
                documentCategoryEntity.setDocument_count(documentCountInCategory.get(categorySrl));

            switch (categoryType) {
                case MDV.NORMAL_CATEGORY:
                    if(documentCategoryEntity.getCategory_type() == MDV.NORMAL_CATEGORY) {
                        if(openType == MDV.PUBLIC) {
                            if(documentCategoryEntity.getOpen_type() == MDV.PUBLIC)
                                this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                        } else if(openType == MDV.PRIVATE) {
                            if(documentCategoryEntity.getOpen_type() == MDV.PRIVATE)
                                this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                        } else {
                            this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                        }
                    }
                    break;

                case MDV.LINK_CATEGORY:
                    if(documentCategoryEntity.getCategory_type() == MDV.LINK_CATEGORY) {
                        if(openType == MDV.PUBLIC) {
                            if(documentCategoryEntity.getOpen_type() == MDV.PUBLIC)
                                this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                        } else if(openType == MDV.PRIVATE) {
                            if(documentCategoryEntity.getOpen_type() == MDV.PRIVATE)
                                this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                        } else {
                            this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                        }
                    }
                    break;

                default:
                    if(openType == MDV.PUBLIC) {
                        if(documentCategoryEntity.getOpen_type() == MDV.PUBLIC)
                            this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                    } else if(openType == MDV.PRIVATE) {
                        if(documentCategoryEntity.getOpen_type() == MDV.PRIVATE)
                            this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                    } else {
                        this.getBoardCategoryInAppHelper(documentCategoryEntity, categoryListOfBoard);
                    }
                    break;
            }
        }

        // 최종 리턴값을 만든다.
        for(DocumentBoardEntity documentBoardEntity : documentBoardEntities) {
            long boardSrl = documentBoardEntity.getBoard_srl();
            if(categoryListOfBoard.containsKey(boardSrl)) {
                Map<String, Object> map = new HashMap<>();
                map.put("board_srl", boardSrl);
                map.put("board_name", documentBoardEntity.getBoard_name());
                map.put("board_description", documentBoardEntity.getBoard_description());
                map.put("enabled", documentBoardEntity.getEnabled());
                map.put("open_type", documentBoardEntity.getOpen_type());
                map.put("categories", categoryListOfBoard.get(boardSrl));

                ret.add(map);
            }
        }

        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocument(int appSrl, long boardSrl, long categorySrl, String documentTitle, long secret,
                              List<Long> secrets, int block, int allowComment, int allowNotice, long memberSrl,
                              String userId, String userName, String nickName) {
        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        return documentDao.count(appSrl, boardSrl, categorySrl, documentTitle, secret, secrets,
                block, allowComment, allowNotice, memberSrl, userId, userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocument(Map<String, String> searchFilter, List<Long> secrets) {
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter == null)
            return this.countDocument(MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, secrets,
                    MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.countDocument(appSrl, boardSrl, categorySrl, documentTitle, secret, secrets,
                block, allowComment, allowNotice, memberSrl, userId, userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentEntity> getDocument(int appSrl, long boardSrl, long categorySrl, String documentTitle,
                                            long secret, List<Long> secrets, int block, int allowComment, int allowNotice, long memberSrl,
                                            String userId, String userName, String nickName,
                                            Map<String, String> sort, int offset, int limit) {
        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        return documentDao.get(null, appSrl, boardSrl, null, categorySrl, null,
                documentTitle, secret, secrets, block, allowComment, allowNotice, memberSrl,
                userId, userName, nickName, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentEntity> getDocument(Map<String, String> searchFilter, List<Long> secrets,
                                            Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null)
            return this.getDocument(MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE, MDV.NUSE,
                    MDV.NUSE, null, null, null, sort, offset, limit);

        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.getDocument(appSrl, boardSrl, categorySrl,
                documentTitle, secret, secrets, block, allowComment, allowNotice, memberSrl, userId,
                userName, nickName, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentEntity getDocument(long documentSrl) {
        if(documentSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "invalid document_srl is less then zero");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("read_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentEntity documentEntity = documentDao.get(documentSrl);
        if(documentEntity == null) return null;

        // tag 를 구한다.
        documentEntity.setAdmin_tags(new ArrayList<Map<String, Object>>());
        documentEntity.setUser_tags(new ArrayList<Map<String, Object>>());

        List<DocumentTagEntity> documentTagEntities = documentTagDao.get(documentSrl, null, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE);
        if(documentTagEntities.size() > 0) {
            List<Long> tagSrls = new ArrayList<>();
            for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                long tagSrl = documentTagEntity.getTag_srl();
                if(!tagSrls.contains(tagSrl)) tagSrls.add(tagSrl);
            }

            Map<String, String> sort = new HashMap<>();
            sort.put("tag_name", "ASC");
            List<TagEntity> tagEntities = tagDao.get(tagSrls, MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                    sort, MDV.NUSE, MDV.NUSE);

            for(TagEntity tagEntity : tagEntities) {
                Map<String, Object> map = new HashMap<>();
                map.put("admin_tag", tagEntity.getAdmin_tag());
                map.put("tag_srl", tagEntity.getTag_srl());
                map.put("tag_name", tagEntity.getTag_name());

                if(tagEntity.getAdmin_tag() == MDV.TAG_ADMIN)
                    documentEntity.getAdmin_tags().add(map);
                else
                    documentEntity.getUser_tags().add(map);
            }
        }

        // 첨부 파일을 구한다.
        documentEntity.setAttach_file_srls(new ArrayList<Long>());
        documentEntity.setDocumentAttachEntities(new ArrayList<DocumentAttachEntity>());

        DocumentFileEntity documentFileEntity = documentFileDao.getAndFile(documentSrl, MDV.NO);
        if(documentFileEntity != null && documentFileEntity.getDocumentAttachEntities() != null &&
                documentFileEntity.getDocumentAttachEntities().size() > 0) {
            for(DocumentAttachEntity documentAttachEntity : documentFileEntity.getDocumentAttachEntities()) {
                long fileSrl = documentAttachEntity.getFile_srl();
                documentEntity.getAttach_file_srls().add(fileSrl);
                documentEntity.getDocumentAttachEntities().add(documentAttachEntity);
            }
        }

        return documentEntity;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> cleanUpDocumentListForAPI(List<DocumentEntity> documentEntities,
                                                               boolean containDocumentContent,
                                                               HttpServletRequest httpServletRequest) {
        if(documentEntities == null || documentEntities.size() <= 0)
            return new ArrayList<>();

        List<Map<String, Object>> ret = new ArrayList<>();
        Map<Long, Object> documentMap = new LinkedHashMap<>();

        // 게시물 내용에서 보여 줘야 할것만 뽑는다
        List<Integer> templateSrls = new ArrayList<>();
        for(DocumentEntity documentEntity : documentEntities) {
            Map<String, Object> map = new HashMap<>();

            long documentSrl = documentEntity.getDocument_srl();
            int templateSrl = documentEntity.getTemplate_srl();

            if(documentMap.containsKey(documentSrl)) continue;
            if(templateSrl > 0 && !templateSrls.contains(templateSrl)) templateSrls.add(templateSrl);

            // 사용자에게 보여 줄 항목만 추가
            // 링크가 아니고 일반 게시물 이므로 링크 시리얼 넘버는 무조건 -1로 설정 된다.
            // 링크 게시물과 동일한 포맷으로 사용하기 위해서 link_srl을 넣어둠
            map.put("link_document", new HashMap<>());
            map.put("document_srl", documentEntity.getDocument_srl());
            map.put("category_srl", documentEntity.getCategory_srl());
            map.put("title", documentEntity.getDocument_title());
            if(containDocumentContent)  map.put("content", documentEntity.getDocument_content());
            else                        map.put("content", "");
            map.put("link", documentEntity.getOuter_link());
            map.put("read_count", documentEntity.getRead_count());
            map.put("comment_count", documentEntity.getComment_count());
            map.put("like_count", documentEntity.getLike_count());
            map.put("blame_count", documentEntity.getBlame_count());
            map.put("file_count", documentEntity.getFile_count());
            map.put("allow_comment", documentEntity.getAllow_comment() == MDV.YES);
            map.put("is_notice", documentEntity.getAllow_notice() == MDV.YES);
            map.put("is_secret", documentEntity.getSecret() != MDV.NONE);
            map.put("user_id", documentEntity.getUser_id());
            map.put("user_name", documentEntity.getUser_name());
            map.put("nick_name", documentEntity.getNick_name());
            map.put("email_address", documentEntity.getEmail_address());
            map.put("ipaddress", documentEntity.getIpaddress());
            map.put("u_date", documentEntity.getU_date());
            map.put("c_date", documentEntity.getC_date());

            documentMap.put(documentSrl, map);
        }
        List<Long> documentSrls = new ArrayList<>(documentMap.keySet());

        // 게시물의 json 데이터를 넣는다.
        Map<Integer, JSONObject> jsonStructMap = this.getDocumentTemplate(templateSrls);
        for(DocumentEntity documentEntity : documentEntities) {
            long documentSrl = documentEntity.getDocument_srl();
            int templareSrl = documentEntity.getTemplate_srl();
            Map<String, Object> map = (Map<String, Object>) documentMap.get(documentSrl);

            if(templareSrl <= 0) map.put("template", new HashMap<>());
            else {
                if(documentEntity.getTemplate_extra() == null) {
                    map.put("template", new HashMap<>());
                    continue;
                }

                String templateExtra = StringUtils.trim(documentEntity.getTemplate_extra());

                if(StringUtils.equals(templateExtra, "")) {
                    map.put("template", new HashMap<>());
                    continue;
                }

                JSONObject templateExtraJSON;
                try {
                    templateExtraJSON = JSONObject.fromObject(templateExtra);
                } catch (Exception e) {
                    LOG.warn("invalid template_extra. document_srl [" + documentSrl + "]");
                    map.put("template", new HashMap<>());
                    continue;
                }

                JSONObject templateStructJSON;
                if(!jsonStructMap.containsKey(templareSrl) ||
                        (templateStructJSON=jsonStructMap.get(templareSrl)) == null) {
                    map.put("template", new HashMap<>());
                    continue;
                }

                Set structKeys = templateStructJSON.keySet();
                Map<String, Object> map3 = new HashMap<>();
                for(Object structKey : structKeys) {
                    if(!templateExtraJSON.containsKey(structKey)) {
                        map3.put((String) structKey, "");
                    } else {
                        Object obj = templateExtraJSON.get(structKey);
                        if(obj instanceof String) {
                            String valueOfKey = (String) templateExtraJSON.get(structKey);
                            String decValueOfKey;

                            try {
                                decValueOfKey = URLDecoder.decode(valueOfKey, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                decValueOfKey = "";
                                LOG.warn("can't decoding template_extra value. document_srl [" + documentSrl +
                                        "], template element key [" + structKey + "]");
                            }

                            map3.put((String) structKey, decValueOfKey);
                        } else {
                            map3.put((String) structKey, templateExtraJSON.get(structKey));
                        }
                    }
                }
                map.put("template", map3);
            }
        }

        // 관리자 태그는 보여 주지 않는다.
        Map<Long, List<Map<String, Object>>> documentTagMap = new HashMap<>();
        List<DocumentTagEntity> documentTagEntities = documentTagDao.get(MDV.NUSE, documentSrls, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        if(documentTagEntities.size() > 0) {
            List<Long> tagSrls = new ArrayList<>();
            for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                long tagSrl = documentTagEntity.getTag_srl();
                if(!tagSrls.contains(tagSrl)) tagSrls.add(tagSrl);
            }

            List<TagEntity> tagEntities = tagDao.get(tagSrls, MDV.NUSE, MDV.NUSE, null, MDV.NO,
                    null, MDV.NUSE, MDV.NUSE);

            Map<Long, TagEntity> tagEntityMap = new HashMap<>();
            for(TagEntity tagEntity : tagEntities) {
                long tagSrl = tagEntity.getTag_srl();
                if(!tagEntityMap.containsKey(tagSrl)) tagEntityMap.put(tagSrl, tagEntity);
            }

            for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                long documentSrl = documentTagEntity.getDocument_srl();
                long tagSrl = documentTagEntity.getTag_srl();

                if(!documentTagMap.containsKey(documentSrl))
                    documentTagMap.put(documentSrl, new ArrayList<Map<String, Object>>());

                List<Map<String, Object>> tmpList = documentTagMap.get(documentSrl);
                if(!tagEntityMap.containsKey(tagSrl)) continue;

                TagEntity tagEntity = tagEntityMap.get(tagSrl);
                Map<String, Object> tmap = new HashMap<>();

                tmap.put("tag_srl", tagSrl);
                tmap.put("tag_name", tagEntity.getTag_name());

                tmpList.add(tmap);
            }
        }

        // 첨부 파일, 태그가 존재하면 추가 한다.
        Map<Long, List<DocumentAttachEntity>> attachFileMap = ckFileService.getDocumentAttachFile(documentSrls);
        for(long documentSrl : documentSrls) {
            Map<String, Object> map = (Map<String, Object>) documentMap.get(documentSrl);

            // 태그가 존재하면 태그를 넣는다
            if(documentTagMap.containsKey(documentSrl))
                map.put("tags", documentTagMap.get(documentSrl));
            else
                map.put("tags", new ArrayList<>());

            if(attachFileMap.containsKey(documentSrl)) {
                List<DocumentAttachEntity> attachEntities = attachFileMap.get(documentSrl);
                List<Map<String, Object>> attachList = new ArrayList<>();

                for(DocumentAttachEntity documentAttachEntity : attachEntities) {
                    Map<String, Object> map2 = new HashMap<>();

                    map2.put("file_url", confDym.getProperty("image_server_host") +
                            httpServletRequest.getAttribute("contextPath") +
                            confCommon.getProperty("document_attach_uri") + documentAttachEntity.getFile_srl());
                    map2.put("file_srl", documentAttachEntity.getFile_srl());
                    map2.put("orig_name", documentAttachEntity.getOrig_name());
                    map2.put("file_comment", documentAttachEntity.getFile_comment());
                    map2.put("height", documentAttachEntity.getHeight());
                    map2.put("width", documentAttachEntity.getWidth());
                    map2.put("mime_type", documentAttachEntity.getMime_type());
                    map2.put("file_size", documentAttachEntity.getFile_size());

                    attachList.add(map2);
                }

                // 파일 등록된 ASC 순서로 정렬 한다.
                Collections.sort(attachList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        long srl1 = (long) o1.get("file_srl");
                        long srl2 = (long) o2.get("file_srl");
                        return srl2 < srl1 ? 1 : (srl1 == srl2 ? 0 : -1);
                    }
                });

                map.put("attach", attachList);
            } else {
                map.put("attach", new ArrayList<>());
            }
        }

        // 리턴할 리스트 형태를 만든다.
        Set<Long> keys = documentMap.keySet();
        for(long documentSrl : keys) {
            ret.add((Map<String, Object>) documentMap.get(documentSrl));
        }
        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> cleanUpDocumentForAPI(DocumentEntity documentEntity,
                                                     HttpServletRequest httpServletRequest) {
        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentEntity", "documentEntity is null");
            LOG.error(reason.get("documentEntity"));
            throw new CustomException("read_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        Map<String, Object> ret = new HashMap<>();

        // 보여줄 항목만 넣는다.
        ret.put("document_srl", documentEntity.getDocument_srl());
        ret.put("category_srl", documentEntity.getCategory_srl());
        ret.put("title", documentEntity.getDocument_title());
        ret.put("content", documentEntity.getDocument_content());
        ret.put("link", documentEntity.getOuter_link());
        ret.put("read_count", documentEntity.getRead_count());
        ret.put("comment_count", documentEntity.getComment_count());
        ret.put("like_count", documentEntity.getLike_count());
        ret.put("blame_count", documentEntity.getBlame_count());
        ret.put("file_count", documentEntity.getFile_count());
        ret.put("allow_comment", documentEntity.getAllow_comment() == MDV.YES);
        ret.put("is_notice", documentEntity.getAllow_notice() == MDV.YES);
        ret.put("user_id", documentEntity.getUser_id());
        ret.put("user_name", documentEntity.getUser_name());
        ret.put("nick_name", documentEntity.getNick_name());
        ret.put("email_address", documentEntity.getEmail_address());
        ret.put("ipaddress", documentEntity.getIpaddress());
        ret.put("u_date", documentEntity.getU_date());
        ret.put("c_date", documentEntity.getC_date());

        // 게시물의 template json 데이터를 넣는다.
        if(documentEntity.getTemplate_srl() > 0 && documentEntity.getTemplate_extra() != null) {
            List<Integer> templateSrls = new ArrayList<>();
            templateSrls.add(documentEntity.getTemplate_srl());
            Map<Integer, JSONObject> jsonStructMap = this.getDocumentTemplate(templateSrls);
            String templateExtra = StringUtils.trim(documentEntity.getTemplate_extra());

            if(jsonStructMap.size() <= 0 || StringUtils.equals(templateExtra, "")) {
                ret.put("template", new HashMap<>());
            } else {
                JSONObject templateExtraJSON;
                try {
                    templateExtraJSON = JSONObject.fromObject(templateExtra);
                    JSONObject templateStructJSON = jsonStructMap.get(documentEntity.getTemplate_srl());
                    Set structKeys = templateStructJSON.keySet();
                    Map<String, Object> map3 = new HashMap<>();

                    for(Object structKey : structKeys) {
                        if(!templateExtraJSON.containsKey(structKey)) {
                            map3.put((String) structKey, "");
                        } else {
                            Object obj = templateExtraJSON.get(structKey);
                            if(obj instanceof String) {
                                String valueOfKey = (String) templateExtraJSON.get(structKey);
                                String decValueOfKey;

                                try {
                                    decValueOfKey = URLDecoder.decode(valueOfKey, "utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    decValueOfKey = "";
                                    LOG.warn("can't decoding template_extra value. document_srl [" +
                                            documentEntity.getDocument_srl() +
                                            "], template element key [" + structKey + "]");
                                }

                                map3.put((String) structKey, decValueOfKey);
                            } else {
                                map3.put((String) structKey, templateExtraJSON.get(structKey));
                            }
                        }
                    }
                    ret.put("template", map3);
                } catch (Exception e) {
                    LOG.warn("invalid template_extra. document_srl [" + documentEntity.getDocument_srl() + "]");
                    ret.put("template", new HashMap<>());
                }
            }
        } else {
            ret.put("template", new HashMap<>());
        }

        List<Long> documentSrls = new ArrayList<>();
        documentSrls.add(documentEntity.getDocument_srl());

        // 첨부 파일 정보 추가
        List<DocumentAttachEntity> documentAttachEntities = documentEntity.getDocumentAttachEntities();
        if(documentAttachEntities == null) {
            Map<Long, List<DocumentAttachEntity>> attachFileMap = ckFileService.getDocumentAttachFile(documentSrls);
            if(attachFileMap.containsKey(documentEntity.getDocument_srl()))
                documentAttachEntities = attachFileMap.get(documentEntity.getDocument_srl());
        }

        if(documentAttachEntities != null) {
            List<Map<String, Object>> attachList = new ArrayList<>();
            for (DocumentAttachEntity documentAttachEntity : documentAttachEntities) {
                Map<String, Object> map2 = new HashMap<>();

                map2.put("file_url", confDym.getProperty("image_server_host") +
                        httpServletRequest.getAttribute("contextPath") +
                        confCommon.getProperty("document_attach_uri") + documentAttachEntity.getFile_srl());
                map2.put("file_srl", documentAttachEntity.getFile_srl());
                map2.put("orig_name", documentAttachEntity.getOrig_name());
                map2.put("file_comment", documentAttachEntity.getFile_comment());
                map2.put("height", documentAttachEntity.getHeight());
                map2.put("width", documentAttachEntity.getWidth());
                map2.put("mime_type", documentAttachEntity.getMime_type());
                map2.put("file_size", documentAttachEntity.getFile_size());

                attachList.add(map2);
            }

            // 파일 등록된 ASC 순서로 정렬 한다.
            Collections.sort(attachList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    long srl1 = (long) o1.get("file_srl");
                    long srl2 = (long) o2.get("file_srl");
                    return srl2 < srl1 ? 1 : (srl1 == srl2 ? 0 : -1);
                }
            });

            ret.put("attach", attachList);
        } else {
            ret.put("attach", new ArrayList<>());
        }

        // 태그 추가(관리자 태그는 추가 하지 않는다)
        if(documentEntity.getUser_tags() != null) {
            Map<Long, List<Map<String, Object>>> documentTagMap = new HashMap<>();
            List<DocumentTagEntity> documentTagEntities = documentTagDao.get(MDV.NUSE, documentSrls, MDV.NUSE,
                    null, MDV.NUSE, MDV.NUSE);
            if(documentTagEntities.size() > 0) {
                List<Long> tagSrls = new ArrayList<>();
                for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                    long tagSrl = documentTagEntity.getTag_srl();
                    if(!tagSrls.contains(tagSrl)) tagSrls.add(tagSrl);
                }

                List<TagEntity> tagEntities = tagDao.get(tagSrls, MDV.NUSE, MDV.NUSE, null, MDV.NO,
                        null, MDV.NUSE, MDV.NUSE);

                Map<Long, TagEntity> tagEntityMap = new HashMap<>();
                for(TagEntity tagEntity : tagEntities) {
                    long tagSrl = tagEntity.getTag_srl();
                    if(!tagEntityMap.containsKey(tagSrl)) tagEntityMap.put(tagSrl, tagEntity);
                }

                for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                    long documentSrl = documentTagEntity.getDocument_srl();
                    long tagSrl = documentTagEntity.getTag_srl();

                    if(!documentTagMap.containsKey(documentSrl))
                        documentTagMap.put(documentSrl, new ArrayList<Map<String, Object>>());

                    List<Map<String, Object>> tmpList = documentTagMap.get(documentSrl);
                    if(!tagEntityMap.containsKey(tagSrl)) continue;

                    TagEntity tagEntity = tagEntityMap.get(tagSrl);
                    Map<String, Object> tmap = new HashMap<>();

                    tmap.put("tag_srl", tagSrl);
                    tmap.put("tag_name", tagEntity.getTag_name());

                    tmpList.add(tmap);
                }
            }

            if(documentTagMap.containsKey(documentEntity.getDocument_srl()))
                ret.put("tags", documentTagMap.get(documentEntity.getDocument_srl()));
            else
                ret.put("tags", new ArrayList<>());
        } else {
            List<Map<String, Object>> tmpList = new ArrayList<>();
            Map<String, Object> tmap = new HashMap<>();
            for(Map<String, Object> map : documentEntity.getUser_tags()) {
                tmap.put("tag_srl", map.get("tag_srl"));
                tmap.put("tag_name", map.get("tag_name"));
                tmpList.add(tmap);
            }
            ret.put("tags", tmpList);
        }

        // TODO 댓글 로직 추가 되면 댓글도 넣어야 함

        return ret;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Map<String, Object>> cleanUpDocumentLinkListForAPI(List<DocumentLinkEntity> documentLinkEntities,
                                                                   boolean containDocumentContent,
                                                                   HttpServletRequest httpServletRequest) {
        if(documentLinkEntities == null || documentLinkEntities.size() <= 0)
            return new ArrayList<>();

        Map<Long, Object> documentMap = new LinkedHashMap<>();

        List<Long> duplicatedDocumentSrl = new ArrayList<>();
        List<DocumentEntity> documentEntities = new ArrayList<>();
        for(DocumentLinkEntity documentLinkEntity : documentLinkEntities) {
            long documentSrl = documentLinkEntity.getDocument_srl();
            if(duplicatedDocumentSrl.contains(documentSrl)) continue;

            duplicatedDocumentSrl.add(documentSrl);
            documentEntities.add(documentLinkEntity.getDocumentEntity());
        }

        // 게시물 내용에서 보여 줘야 할것만 뽑는다
        List<Integer> templateSrls = new ArrayList<>();
        for(DocumentEntity documentEntity : documentEntities) {
            Map<String, Object> map = new HashMap<>();

            long documentSrl = documentEntity.getDocument_srl();
            int templateSrl = documentEntity.getTemplate_srl();

            if(documentMap.containsKey(documentSrl)) continue;
            if(templateSrl > 0 && !templateSrls.contains(templateSrl)) templateSrls.add(templateSrl);

            // 사용자에게 보여 줄 항목만 추가
            // 링크가 아니고 일반 게시물 이므로 링크 시리얼 넘버는 무조건 -1로 설정 된다.
            // 링크 게시물과 동일한 포맷으로 사용하기 위해서 link_srl을 넣어둠
            map.put("document_srl", documentEntity.getDocument_srl());
            map.put("category_srl", documentEntity.getCategory_srl());
            map.put("title", documentEntity.getDocument_title());
            if(containDocumentContent)  map.put("content", documentEntity.getDocument_content());
            else                        map.put("content", "");
            map.put("link", documentEntity.getOuter_link());
            map.put("read_count", documentEntity.getRead_count());
            map.put("comment_count", documentEntity.getComment_count());
            map.put("like_count", documentEntity.getLike_count());
            map.put("blame_count", documentEntity.getBlame_count());
            map.put("file_count", documentEntity.getFile_count());
            map.put("allow_comment", documentEntity.getAllow_comment() == MDV.YES);
            map.put("is_notice", documentEntity.getAllow_notice() == MDV.YES);
            map.put("is_secret", documentEntity.getSecret() != MDV.NONE);
            map.put("user_id", documentEntity.getUser_id());
            map.put("user_name", documentEntity.getUser_name());
            map.put("nick_name", documentEntity.getNick_name());
            map.put("email_address", documentEntity.getEmail_address());
            map.put("ipaddress", documentEntity.getIpaddress());
            map.put("u_date", documentEntity.getU_date());
            map.put("c_date", documentEntity.getC_date());

            documentMap.put(documentSrl, map);
        }
        List<Long> documentSrls = new ArrayList<>(documentMap.keySet());

        // 게시물의 json 데이터를 넣는다.
        Map<Integer, JSONObject> jsonStructMap = this.getDocumentTemplate(templateSrls);
        for(DocumentEntity documentEntity : documentEntities) {
            long documentSrl = documentEntity.getDocument_srl();
            int templareSrl = documentEntity.getTemplate_srl();
            Map<String, Object> map = (Map<String, Object>) documentMap.get(documentSrl);

            if(templareSrl <= 0) map.put("template", new HashMap<>());
            else {
                if(documentEntity.getTemplate_extra() == null) {
                    map.put("template", new HashMap<>());
                    continue;
                }

                String templateExtra = StringUtils.trim(documentEntity.getTemplate_extra());

                if(StringUtils.equals(templateExtra, "")) {
                    map.put("template", new HashMap<>());
                    continue;
                }

                JSONObject templateExtraJSON;
                try {
                    templateExtraJSON = JSONObject.fromObject(templateExtra);
                } catch (Exception e) {
                    LOG.warn("invalid template_extra. document_srl [" + documentSrl + "]");
                    map.put("template", new HashMap<>());
                    continue;
                }

                JSONObject templateStructJSON;
                if(!jsonStructMap.containsKey(templareSrl) ||
                        (templateStructJSON=jsonStructMap.get(templareSrl)) == null) {
                    map.put("template", new HashMap<>());
                    continue;
                }

                Set structKeys = templateStructJSON.keySet();
                Map<String, Object> map3 = new HashMap<>();
                for(Object structKey : structKeys) {
                    if(!templateExtraJSON.containsKey(structKey)) {
                        map3.put((String) structKey, "");
                    } else {
                        Object obj = templateExtraJSON.get(structKey);
                        if(obj instanceof String) {
                            String valueOfKey = (String) templateExtraJSON.get(structKey);
                            String decValueOfKey;

                            try {
                                decValueOfKey = URLDecoder.decode(valueOfKey, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                decValueOfKey = "";
                                LOG.warn("can't decoding template_extra value. document_srl [" + documentSrl +
                                        "], template element key [" + structKey + "]");
                            }

                            map3.put((String) structKey, decValueOfKey);
                        } else {
                            map3.put((String) structKey, templateExtraJSON.get(structKey));
                        }
                    }
                }
                map.put("template", map3);
            }
        }

        // 관리자 태그는 보여 주지 않는다.
        Map<Long, List<Map<String, Object>>> documentTagMap = new HashMap<>();
        List<DocumentTagEntity> documentTagEntities = documentTagDao.get(MDV.NUSE, documentSrls, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        if(documentTagEntities.size() > 0) {
            List<Long> tagSrls = new ArrayList<>();
            for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                long tagSrl = documentTagEntity.getTag_srl();
                if(!tagSrls.contains(tagSrl)) tagSrls.add(tagSrl);
            }

            List<TagEntity> tagEntities = tagDao.get(tagSrls, MDV.NUSE, MDV.NUSE, null, MDV.NO,
                    null, MDV.NUSE, MDV.NUSE);

            Map<Long, TagEntity> tagEntityMap = new HashMap<>();
            for(TagEntity tagEntity : tagEntities) {
                long tagSrl = tagEntity.getTag_srl();
                if(!tagEntityMap.containsKey(tagSrl)) tagEntityMap.put(tagSrl, tagEntity);
            }

            for(DocumentTagEntity documentTagEntity : documentTagEntities) {
                long documentSrl = documentTagEntity.getDocument_srl();
                long tagSrl = documentTagEntity.getTag_srl();

                if(!documentTagMap.containsKey(documentSrl))
                    documentTagMap.put(documentSrl, new ArrayList<Map<String, Object>>());

                List<Map<String, Object>> tmpList = documentTagMap.get(documentSrl);
                if(!tagEntityMap.containsKey(tagSrl)) continue;

                TagEntity tagEntity = tagEntityMap.get(tagSrl);
                Map<String, Object> tmap = new HashMap<>();

                tmap.put("tag_srl", tagSrl);
                tmap.put("tag_name", tagEntity.getTag_name());

                tmpList.add(tmap);
            }
        }

        // 첨부 파일, 태그가 존재하면 추가 한다.
        Map<Long, List<DocumentAttachEntity>> attachFileMap = ckFileService.getDocumentAttachFile(documentSrls);
        for(long documentSrl : documentSrls) {
            Map<String, Object> map = (Map<String, Object>) documentMap.get(documentSrl);

            // 태그가 존재하면 태그를 넣는다
            if(documentTagMap.containsKey(documentSrl))
                map.put("tags", documentTagMap.get(documentSrl));
            else
                map.put("tags", new ArrayList<>());

            if(attachFileMap.containsKey(documentSrl)) {
                List<DocumentAttachEntity> attachEntities = attachFileMap.get(documentSrl);
                List<Map<String, Object>> attachList = new ArrayList<>();

                for(DocumentAttachEntity documentAttachEntity : attachEntities) {
                    Map<String, Object> map2 = new HashMap<>();

                    map2.put("file_url", confDym.getProperty("image_server_host") +
                            httpServletRequest.getAttribute("contextPath") +
                            confCommon.getProperty("document_attach_uri") + documentAttachEntity.getFile_srl());
                    map2.put("file_srl", documentAttachEntity.getFile_srl());
                    map2.put("orig_name", documentAttachEntity.getOrig_name());
                    map2.put("file_comment", documentAttachEntity.getFile_comment());
                    map2.put("height", documentAttachEntity.getHeight());
                    map2.put("width", documentAttachEntity.getWidth());
                    map2.put("mime_type", documentAttachEntity.getMime_type());
                    map2.put("file_size", documentAttachEntity.getFile_size());

                    attachList.add(map2);
                }

                // 파일 등록된 ASC 순서로 정렬 한다.
                Collections.sort(attachList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        long srl1 = (long) o1.get("file_srl");
                        long srl2 = (long) o2.get("file_srl");
                        return srl2 < srl1 ? 1 : (srl1 == srl2 ? 0 : -1);
                    }
                });

                map.put("attach", attachList);
            } else {
                map.put("attach", new ArrayList<>());
            }
        }

        List<Map<String, Object>> ret = new ArrayList<>();

        for(DocumentLinkEntity documentLinkEntity : documentLinkEntities) {
            long documentSrl = documentLinkEntity.getDocument_srl();

            Map<String, Object> linkDocument = new HashMap<>();
            Map<String, Long> linkInfo = new HashMap<>();

            linkInfo.put("link_srl", documentLinkEntity.getDocument_link_srl());
            linkInfo.put("category_srl", documentLinkEntity.getCategory_srl());

            linkDocument.put("link_document", linkInfo);
            linkDocument.putAll((Map<? extends String, ?>) documentMap.get(documentSrl));

            ret.add(linkDocument);
        }

        return ret;
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


        if(documentEntity.getCategory_srl() == Long.parseLong(confAdmin.getProperty("plymind.notice.category.srl"))) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setMember_srl(MDV.NONE);
            notificationEntity.setUser_id(null);
            notificationEntity.setApplication_srl(MDV.NONE);
            notificationEntity.setApplication_group(MDV.NONE);
            notificationEntity.setNoti_type(2);  //1=너나들이, 2=공지사항, 3=그룹테라피, 4=개인테라피
            notificationEntity.setPush_text(documentEntity.getDocument_content());
            notificationEntity.setBook_time(ltm);
            notificationEntity.setPush_status(1); //1=발송전, 2=발송

            plymindMessageService.add(notificationEntity);
        }



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
    }

    @Override
    public void deleteDocument(List<Long> documentSrls) {
        if(documentSrls == null || documentSrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srls", "invalid document_srls. document_srls is null or size zero");
            LOG.error(reason.get("document_srls"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        // 게시물에 매핑된 파일의 flag를 delete로 변경 한다.
        List<DocumentFileEntity> documentFileEntities = documentFileDao.get(MDV.NUSE, documentSrls, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        if(documentFileEntities.size() > 0) {
            List<Long> fileSrls = new ArrayList<>();
            for(DocumentFileEntity documentFileEntity : documentFileEntities) {
                long fileSrl = documentFileEntity.getFile_srl();
                if(!fileSrls.contains(fileSrl)) fileSrls.add(fileSrl);
            }

            DocumentAttachEntity updateVo = new DocumentAttachEntity();
            updateVo.init();
            updateVo.setDeleted(MDV.YES);
            documentAttachDao.modify(updateVo, MDV.NUSE, fileSrls);
            LOG.info("set delete flag yes by removing document. document_srls [" + documentSrls.toString() + "]");
        }

        // 게시물을 삭제 한다.
        documentDao.delete(MDV.NUSE, documentSrls);
        LOG.info("delete document. document_srls [" + documentSrls.toString() + "]");
    }

    @Override
    public void modifyDocument(DocumentEntity documentEntity, int documentSrl) {
        if(documentSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "invalid document_srl. document_srl is less then zero");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentEntity", "invalid documentEntity. documentEntity is null");
            LOG.error(reason.get("documentEntity"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        DocumentEntity savedEntity = documentDao.get(documentSrl);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document", "no document for modify. document_srl [" + documentSrl + "]");
            LOG.error(reason.get("document"));
            throw new CustomException("modify_document_error", reason);
        }

        boolean willModify = false, isModifyCategorySrl = false;
        DocumentEntity updateEntity = new DocumentEntity();
        updateEntity.init();

        //if(documentEntity.getApp_srl() > 0 && documentEntity.getApp_srl() != savedEntity.getApp_srl()) {
        //    updateEntity.setApp_srl(documentEntity.getApp_srl());
        //    willModify = true;
        //}

        //if(documentEntity.getBoard_srl() > 0 && documentEntity.getBoard_srl() != savedEntity.getBoard_srl()) {
        //    updateEntity.setBoard_srl(documentEntity.getBoard_srl());
        //    willModify = true;
        //}

        if(documentEntity.getCategory_srl() > 0 && documentEntity.getCategory_srl() != savedEntity.getCategory_srl()) {
            updateEntity.setCategory_srl(documentEntity.getCategory_srl());
            willModify = true;
            isModifyCategorySrl = true;
        }

        if(documentEntity.getDocument_title() != null &&
                !StringUtils.equals(documentEntity.getDocument_title(), savedEntity.getDocument_title())) {
            updateEntity.setDocument_title(documentEntity.getDocument_title());
            willModify = true;
        }

        if(documentEntity.getDocument_content() != null &&
                !StringUtils.equals(documentEntity.getDocument_content(), savedEntity.getDocument_content())) {
            updateEntity.setDocument_content(documentEntity.getDocument_content());
            willModify = true;
        }

        if(documentEntity.getRead_count() >= 0 && documentEntity.getRead_count() != savedEntity.getRead_count()) {
            updateEntity.setRead_count(documentEntity.getRead_count());
            willModify = true;
        }

        if(documentEntity.getLike_count() >= 0 && documentEntity.getLike_count() != savedEntity.getLike_count()) {
            updateEntity.setLike_count(documentEntity.getLike_count());
            willModify = true;
        }

        if(documentEntity.getBlame_count() >= 0 && documentEntity.getBlame_count() != savedEntity.getBlame_count()) {
            updateEntity.setBlame_count(documentEntity.getBlame_count());
            willModify = true;
        }

        if(documentEntity.getComment_count() >= 0 &&
                documentEntity.getComment_count() != savedEntity.getComment_count()) {
            updateEntity.setComment_count(documentEntity.getComment_count());
            willModify = true;
        }

        if(documentEntity.getFile_count() >= 0 &&
                documentEntity.getFile_count() != savedEntity.getFile_count()) {
            updateEntity.setFile_count(documentEntity.getFile_count());
            willModify = true;
        }

        if(documentEntity.getOuter_link() != null &&
                !StringUtils.equals(documentEntity.getOuter_link(), savedEntity.getOuter_link())) {
            updateEntity.setOuter_link(documentEntity.getOuter_link());
            willModify = true;
        }

        if(documentEntity.getSecret() != MDV.NUSE && documentEntity.getSecret() != savedEntity.getSecret()){
            updateEntity.setSecret(documentEntity.getSecret());
            willModify = true;
        }

        if(documentEntity.getBlock() != savedEntity.getBlock() &&
                (documentEntity.getBlock() == MDV.YES || documentEntity.getBlock() == MDV.NO)) {
            updateEntity.setBlock(documentEntity.getBlock());
            willModify = true;
        }

        if(documentEntity.getAllow_comment() != savedEntity.getAllow_comment() &&
                (documentEntity.getAllow_comment() == MDV.YES || documentEntity.getAllow_comment() == MDV.NO)) {
            updateEntity.setAllow_comment(documentEntity.getAllow_comment());
            willModify = true;
        }

        if(documentEntity.getAllow_notice() != savedEntity.getAllow_notice() &&
                (documentEntity.getAllow_notice() == MDV.YES || documentEntity.getAllow_notice() == MDV.NO)) {
            updateEntity.setAllow_notice(documentEntity.getAllow_notice());
            willModify = true;
        }

        if(documentEntity.getList_order() >= MDV.NONE &&
                documentEntity.getList_order() != savedEntity.getList_order()) {
            updateEntity.setList_order(documentEntity.getList_order());
            willModify = true;
        }

        if(documentEntity.getTemplate_extra() != null &&
                !StringUtils.equals(documentEntity.getTemplate_extra(), savedEntity.getTemplate_extra())) {
            updateEntity.setTemplate_extra(documentEntity.getTemplate_extra());
            willModify = true;
        }

        if(documentEntity.getTemplate_srl() >= MDV.NONE &&
                documentEntity.getTemplate_srl() != savedEntity.getTemplate_srl()) {
            updateEntity.setTemplate_srl(documentEntity.getTemplate_srl());
            if(updateEntity.getTemplate_srl() == MDV.NONE) updateEntity.setTemplate_extra("");
            willModify = true;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        if(!willModify) {
            LOG.info("ignore modify document. document_srl [" + documentSrl +
                    "]. it is same between saved value and changed value");
        } else {
            updateEntity.setU_date(ltm);
            documentDao.modify(documentEntity, documentSrl, MDV.NUSE, MDV.NUSE);
            LOG.info("modify document. updateEntity [" + updateEntity.toString() + "]");
        }

        // 기존에 저장되어 있는 tag를 구한다.
        List<DocumentTagEntity> documentTagEntities = documentTagDao.get(documentSrl, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        List<Long> tagSrls = new ArrayList<>();
        for(DocumentTagEntity documentTagEntity : documentTagEntities) {
            long tagSrl = documentTagEntity.getTag_srl();
            if(!tagSrls.contains(tagSrl)) tagSrls.add(tagSrl);
        }

        List<TagEntity> tagEntities, adminTags = new ArrayList<>(), userTags = new ArrayList<>();
        if(tagSrls.size() > 0) {
            tagEntities = tagDao.get(tagSrls, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
            for(TagEntity tagEntity : tagEntities) {
                if(tagEntity.getAdmin_tag() == MDV.TAG_ADMIN)   adminTags.add(tagEntity);
                else                                            userTags.add(tagEntity);
            }
        }

        // modify admin tag
        if(adminTags.size() > 0) {
            List<Long> adminTagSrls = new ArrayList<>();
            for(TagEntity tagEntity : adminTags) {
                long tagSrl = tagEntity.getTag_srl();
                adminTagSrls.add(tagSrl);
            }

            List<Long> modifyMappingTagSrls = new ArrayList<>();
            for(Map<String, Object> map : documentEntity.getAdmin_tags()) {
                if(map.containsKey("admin_tag")) {
                    String strTagSrl = StringUtils.trim(map.get("tag_srl").toString());
                    if(NumberUtils.isNumber(strTagSrl)) modifyMappingTagSrls.add(Long.parseLong(strTagSrl, 10));
                }
            }

            List<Long> removeMappingTagSrls = new ArrayList<>();
            List<Long> newMappingTagSrls = new ArrayList<>();

            if(modifyMappingTagSrls.size() > 0) {
                for(long tagSrl : adminTagSrls) {
                    if(!modifyMappingTagSrls.contains(tagSrl)) removeMappingTagSrls.add(tagSrl);
                }
                for(long tagSrl : modifyMappingTagSrls) {
                    if(!adminTagSrls.contains(tagSrl)) newMappingTagSrls.add(tagSrl);
                }
            } else {
                for(TagEntity tagEntity : adminTags) {
                    long tagSrl = tagEntity.getTag_srl();
                    removeMappingTagSrls.add(tagSrl);
                }
            }

            // 새로운 매핑은 추가
            if(newMappingTagSrls.size() > 0) {
                DocumentTagEntity documentTagEntity1 = new DocumentTagEntity();
                documentTagEntity1.setDocument_srl(savedEntity.getDocument_srl());
                documentTagEntity1.setC_date(ltm);
                for (long tagSrl : newMappingTagSrls) {
                    documentTagEntity1.setTag_srl(tagSrl);
                    documentTagDao.add(documentTagEntity1);
                    LOG.info("add document admin tag mapping. document_srl [" + savedEntity.getDocument_srl() +
                            "], tag_srl [" + documentTagEntity1.getTag_srl() + "]");
                }
            }

            // 매핑 없어진 것은 삭제
            if(removeMappingTagSrls.size() > 0) {
                documentTagDao.delete(savedEntity.getDocument_srl(), MDV.NUSE, removeMappingTagSrls);
                LOG.info("delete document admin tag mapping. document_srl [" + savedEntity.getDocument_srl() +
                        "], tag_srls [" + removeMappingTagSrls.toString() + "]");
            }

            // 신규 태그를 생성해서 매핑 한다.
            List<Long> mappingTagSrls = new ArrayList<>();
            List<String> createTagName = new ArrayList<>();

            for(Map<String, Object> map : documentEntity.getAdmin_tags()) {
                if(map.containsKey("admin_tag")) {
                    String strTagSrl = StringUtils.trim(map.get("tag_srl").toString());
                    if(!NumberUtils.isNumber(strTagSrl)) createTagName.add(strTagSrl);
                } else {
                    createTagName.add(StringUtils.trim(map.get("tag_name").toString()));
                }
            }

            if(createTagName.size() > 0) {
                // 신규 태그를 생성 한다.
                TagEntity tagEntity = new TagEntity();
                tagEntity.setApp_srl(savedEntity.getApp_srl());
                tagEntity.setMember_srl(savedEntity.getMember_srl());
                tagEntity.setAdmin_tag(MDV.TAG_ADMIN);
                tagEntity.setC_date(ltm);
                tagEntity.setU_date(ltm);

                for (String tagName : createTagName) {
                    tagEntity.setTag_name(tagName);
                    tagDao.add(tagEntity);
                    LOG.info("add admin tag. tag_srl [" + tagEntity.getTag_srl() + "], app_srl [" +
                            tagEntity.getApp_srl() + "]");

                    mappingTagSrls.add(tagEntity.getTag_srl());
                }

                // 태그를 매핑 한다.
                DocumentTagEntity documentTagEntity = new DocumentTagEntity();
                documentTagEntity.setDocument_srl(savedEntity.getDocument_srl());
                documentTagEntity.setC_date(ltm);

                for (long tagSrl : mappingTagSrls) {
                    documentTagEntity.setTag_srl(tagSrl);
                    documentTagDao.add(documentTagEntity);
                    LOG.info("add document admin tag mapping. document_srl [" + documentTagEntity.getDocument_srl() +
                            "], tag_srl [" + documentTagEntity.getTag_srl() + "]");
                }
            }
        } else {
            // 기존에 저장된 것이 없는데 수정 하는 admin tag가 존재하면 태그를 매핑
            this.setTag(documentEntity.getAdmin_tags(), savedEntity.getApp_srl(),
                    savedEntity.getMember_srl(), savedEntity.getDocument_srl(), ltm, MDV.TAG_ADMIN);
        }

        // modify user tag
        if(userTags.size() > 0) {
            List<Long> userTagSrls = new ArrayList<>();
            for(TagEntity tagEntity : userTags) {
                long tagSrl = tagEntity.getTag_srl();
                userTagSrls.add(tagSrl);
            }

            List<Long> modifyMappingTagSrls = new ArrayList<>();
            for(Map<String, Object> map : documentEntity.getUser_tags()) {
                if(map.containsKey("admin_tag")) {
                    String strTagSrl = StringUtils.trim(map.get("tag_srl").toString());
                    if(NumberUtils.isNumber(strTagSrl)) modifyMappingTagSrls.add(Long.parseLong(strTagSrl, 10));
                }
            }

            List<Long> removeMappingTagSrls = new ArrayList<>();
            List<Long> newMappingTagSrls = new ArrayList<>();

            if(modifyMappingTagSrls.size() > 0) {
                for(long tagSrl : userTagSrls) {
                    if(!modifyMappingTagSrls.contains(tagSrl)) removeMappingTagSrls.add(tagSrl);
                }
                for(long tagSrl : modifyMappingTagSrls) {
                    if(!userTagSrls.contains(tagSrl)) newMappingTagSrls.add(tagSrl);
                }
            } else {
                for(TagEntity tagEntity : userTags) {
                    long tagSrl = tagEntity.getTag_srl();
                    removeMappingTagSrls.add(tagSrl);
                }
            }

            // 새로운 매핑은 추가
            if(newMappingTagSrls.size() > 0) {
                DocumentTagEntity documentTagEntity1 = new DocumentTagEntity();
                documentTagEntity1.setDocument_srl(savedEntity.getDocument_srl());
                documentTagEntity1.setC_date(ltm);
                for (long tagSrl : newMappingTagSrls) {
                    documentTagEntity1.setTag_srl(tagSrl);
                    documentTagDao.add(documentTagEntity1);
                    LOG.info("add document user tag mapping. document_srl [" + savedEntity.getDocument_srl() +
                            "], tag_srl [" + documentTagEntity1.getTag_srl() + "]");
                }
            }

            // 매핑 없어진 것은 삭제
            if(removeMappingTagSrls.size() > 0) {
                documentTagDao.delete(savedEntity.getDocument_srl(), MDV.NUSE, removeMappingTagSrls);
                LOG.info("delete document user tag mapping. document_srl [" + savedEntity.getDocument_srl() +
                        "], tag_srls [" + removeMappingTagSrls.toString() + "]");
            }

            // 신규 태그를 생성해서 매핑 한다.
            List<Long> mappingTagSrls = new ArrayList<>();
            List<String> createTagName = new ArrayList<>();

            for(Map<String, Object> map : documentEntity.getUser_tags()) {
                if(map.containsKey("admin_tag")) {
                    String strTagSrl = StringUtils.trim(map.get("tag_srl").toString());
                    if(!NumberUtils.isNumber(strTagSrl)) createTagName.add(strTagSrl);
                } else {
                    createTagName.add(StringUtils.trim(map.get("tag_name").toString()));
                }
            }

            if(createTagName.size() > 0) {
                // 신규 태그를 생성 한다.
                TagEntity tagEntity = new TagEntity();
                tagEntity.setApp_srl(savedEntity.getApp_srl());
                tagEntity.setMember_srl(savedEntity.getMember_srl());
                tagEntity.setAdmin_tag(MDV.TAG_USER);
                tagEntity.setC_date(ltm);
                tagEntity.setU_date(ltm);

                for (String tagName : createTagName) {
                    tagEntity.setTag_name(tagName);
                    tagDao.add(tagEntity);
                    LOG.info("add admin tag. tag_srl [" + tagEntity.getTag_srl() + "], app_srl [" +
                            tagEntity.getApp_srl() + "]");

                    mappingTagSrls.add(tagEntity.getTag_srl());
                }

                // 태그를 매핑 한다.
                DocumentTagEntity documentTagEntity = new DocumentTagEntity();
                documentTagEntity.setDocument_srl(savedEntity.getDocument_srl());
                documentTagEntity.setC_date(ltm);

                for (long tagSrl : mappingTagSrls) {
                    documentTagEntity.setTag_srl(tagSrl);
                    documentTagDao.add(documentTagEntity);
                    LOG.info("add document admin tag mapping. document_srl [" + documentTagEntity.getDocument_srl() +
                            "], tag_srl [" + documentTagEntity.getTag_srl() + "]");
                }
            }
        } else {
            // 기존에 저장된 것이 없는데 수정 하는 admin tag가 존재하면 태그를 매핑
            this.setTag(documentEntity.getUser_tags(), savedEntity.getApp_srl(),
                    savedEntity.getMember_srl(), savedEntity.getDocument_srl(), ltm, MDV.TAG_USER);
        }

        List<DocumentFileEntity> documentFileEntities = documentFileDao.get(savedEntity.getDocument_srl(), null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        List<Long> attachFileSrls = new ArrayList<>();
        for(DocumentFileEntity documentFileEntity : documentFileEntities) {
            long fileSrl = documentFileEntity.getFile_srl();
            if(!attachFileSrls.contains(fileSrl)) attachFileSrls.add(fileSrl);
        }

        if(attachFileSrls.size() > 0) {
            List<Long> removeFileSrls = new ArrayList<>();  // 매핑 삭제할 file_srl
            List<Long> newFileSrls = new ArrayList<>();     // 매핑 추가할 file_srl

            if(documentEntity.getAttach_file_srls().size() > 0) {
                for(long fileSrl : attachFileSrls) {
                    if(!documentEntity.getAttach_file_srls().contains(fileSrl)) removeFileSrls.add(fileSrl);
                }
                for(long fileSrl : documentEntity.getAttach_file_srls()) {
                    if(!attachFileSrls.contains(fileSrl)) newFileSrls.add(fileSrl);
                }
            } else {
                for(long fileSrl : attachFileSrls) removeFileSrls.add(fileSrl);
            }

            // 신규 파일 매핑 추가및 파일 delete 를 undelete 로 표시
            if(newFileSrls.size() > 0) {
                DocumentFileEntity documentFileEntity = new DocumentFileEntity();
                documentFileEntity.setDocument_srl(savedEntity.getDocument_srl());
                documentFileEntity.setC_date(ltm);

                for(long fileSrl : newFileSrls) {
                    documentFileEntity.setFile_srl(fileSrl);
                    documentFileDao.add(documentFileEntity);
                    LOG.info("mapping file to document. document_srl [" + savedEntity.getDocument_srl() +
                            "], file_srl [" + fileSrl + "]");
                }

                DocumentAttachEntity attachEntity = new DocumentAttachEntity();
                attachEntity.init();
                attachEntity.setDeleted(MDV.NO);
                documentAttachDao.modify(attachEntity, MDV.NUSE, newFileSrls);
            }

            // 삭제된 파일 매핑 삭제및 파일 delete 표시
            if(removeFileSrls.size() > 0) {
                documentFileDao.delete(savedEntity.getDocument_srl(), MDV.NUSE, removeFileSrls);
                LOG.info("delete document file mapping. document_srl [" + savedEntity.getDocument_srl() +
                        "], file_srls [" + removeFileSrls.toString() + "]");

                DocumentAttachEntity attachEntity = new DocumentAttachEntity();
                attachEntity.init();
                attachEntity.setDeleted(MDV.YES);
                documentAttachDao.modify(attachEntity, MDV.NUSE, removeFileSrls);
            }

            // 첨부 파일 카운트 업데이트
            int fileCount = savedEntity.getFile_count() + newFileSrls.size() - removeFileSrls.size();
            DocumentEntity updateVo = new DocumentEntity();
            updateVo.init();
            updateVo.setFile_count(fileCount <= 0 ? 0 : fileCount);
            documentDao.modify(updateVo, documentSrl, MDV.NUSE, MDV.NUSE);
            LOG.info("update file_count [" + updateVo.getFile_count() +
                    "], document_srl [" + documentSrl + "]");
        } else {
            if(documentEntity.getAttach_file_srls().size() > 0) {
                DocumentFileEntity documentFileEntity = new DocumentFileEntity();
                documentFileEntity.setDocument_srl(savedEntity.getDocument_srl());
                documentFileEntity.setC_date(ltm);

                for(long fileSrl : documentEntity.getAttach_file_srls()) {
                    documentFileEntity.setFile_srl(fileSrl);
                    documentFileDao.add(documentFileEntity);
                    LOG.info("mapping file to document. document_srl [" + savedEntity.getDocument_srl() +
                            "], file_srl [" + fileSrl + "]");
                }

                // 첨부 파일 카운트 업데이트
                DocumentEntity updateVo = new DocumentEntity();
                updateVo.init();
                updateVo.setFile_count(documentEntity.getAttach_file_srls().size());
                documentDao.modify(updateVo, documentSrl, MDV.NUSE, MDV.NUSE);
                LOG.info("update file_count [" + updateVo.getFile_count() +
                        "], document_srl [" + documentSrl + "]");

                // 첨부파일 삭제 여부를 삭제 안함으로 바꿈
                DocumentAttachEntity attachEntity = new DocumentAttachEntity();
                attachEntity.init();
                attachEntity.setDeleted(MDV.NO);
                documentAttachDao.modify(attachEntity, MDV.NUSE, documentEntity.getAttach_file_srls());
                LOG.info("update document attach file set no deleted. file_srl [" +
                        documentEntity.getAttach_file_srls() + "]");
            }
        }

        if(isModifyCategorySrl) {
            // TODO 게시물의 카테고리 이동시 댓글도 카테고리 이동 시켜야 한다.

        }
    }

    @Override
    public void deleteDocumentCategory(List<Long> categorySrls) {
        if(categorySrls == null || categorySrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srls", "invalid category_srls. category_srls is null or size zero");
            LOG.error(reason.get("category_srls"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        documentCategoryDao.delete(MDV.NUSE, categorySrls);
        LOG.info("delete document category. category_srls [" + categorySrls.toString() + "]");
    }

    @Override
    public void addDocumentCategory(DocumentCategoryEntity documentCategoryEntity) {
        if(documentCategoryEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentCategoryEntity", "invalid documentCategoryEntity is null");
            LOG.error(reason.get("documentCategoryEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentCategoryEntity.getApp_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appDao.get(documentCategoryEntity.getApp_srl(), null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. not found app. app_srl [" +
                    documentCategoryEntity.getApp_srl() + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentBoardEntity documentBoardEntity = documentBoardDao.get(documentCategoryEntity.getBoard_srl(), MDV.NUSE);
        if(documentBoardEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "invalid board_srl. not found app. board_srl [" +
                    documentCategoryEntity.getBoard_srl() + "]");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentCategoryEntity.getCategory_name() == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_name", "invalid category_name. category_name is null");
            LOG.error(reason.get("category_name"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }
        documentCategoryEntity.setCategory_name(StringUtils.trim(documentCategoryEntity.getCategory_name()));
        if(StringUtils.equals(documentCategoryEntity.getCategory_name(), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_name", "invalid category_name. category_name is empty string");
            LOG.error(reason.get("category_name"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentCategoryEntity.getCategory_description() == null) documentCategoryEntity.setCategory_description("");
        documentCategoryEntity.setCategory_description(StringUtils.trim(documentCategoryEntity.getCategory_description()));

        if(documentCategoryEntity.getCategory_type() != MDV.NORMAL_CATEGORY &&
                documentCategoryEntity.getCategory_type() != MDV.LINK_CATEGORY) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_type", "invalid category_type. category_type is unknown value. category_type [" +
                    documentCategoryEntity.getCategory_type() + "]");
            LOG.error(reason.get("category_type"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentCategoryEntity.getEnabled() != MDV.YES && documentCategoryEntity.getEnabled() != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("enabled", "invalid enabled. enabled is unknown value. enabled [" +
                    documentCategoryEntity.getEnabled() + "]");
            LOG.error(reason.get("enabled"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentCategoryEntity.getOpen_type() != MDV.PUBLIC && documentCategoryEntity.getOpen_type() != MDV.PRIVATE) {
            Map<String, String> reason = new HashMap<>();
            reason.put("open_type", "invalid open_type. open_type is unknown value. open_type [" +
                    documentCategoryEntity.getOpen_type() + "]");
            LOG.error(reason.get("open_type"));
            throw new CustomException("add_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        documentCategoryEntity.setC_date(ltm);
        documentCategoryEntity.setU_date(ltm);

        documentCategoryDao.add(documentCategoryEntity);
        LOG.info("add document category. documentCategoryEntity [" + documentCategoryEntity.toString() + "]");
    }

    @Override
    public void modifyDocumentCategory(long categorySrl, Map<String, String> modifyValue) {
        if(categorySrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srl", "invalid category_srl. category_srl is less then zero. category_srl [" +
                    categorySrl + "]");
            LOG.error(reason.get("category_srl"));
            throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(modifyValue == null || modifyValue.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "invalid modify value. modify value is null or empty. category_srl [" +
                    categorySrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(!modifyValue.containsKey("app_srl") && !modifyValue.containsKey("board_srl") &&
                !modifyValue.containsKey("category_name") && !modifyValue.containsKey("category_description") &&
                !modifyValue.containsKey("category_type") && !modifyValue.containsKey("enabled") &&
                !modifyValue.containsKey("open_type")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "not found category modify value. modifyValue [" + modifyValue.toString() +
                    "], category_srl [" + categorySrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentCategoryEntity savedEntity = documentCategoryDao.get(categorySrl, MDV.NUSE);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found board. category_srl [" + categorySrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);

        }

        boolean willModify = false, isModifyBoardSrl = false;
        DocumentCategoryEntity documentCategoryEntity = new DocumentCategoryEntity();
        documentCategoryEntity.init();

        if(modifyValue.containsKey("app_srl")) {
            String appSrl = modifyValue.get("app_srl");
            if(appSrl == null || StringUtils.equals(StringUtils.trim(appSrl), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_srl", "failed modify app_srl. app_srl is null or empty");
                LOG.error(reason.get("app_srl"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(appSrl)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("app_srl", "failed modify app_srl. app_srl is not numeric. app_srl [" +
                        appSrl + "]");
                LOG.error(reason.get("app_srl"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iAppSrl = Integer.parseInt(appSrl, 10);
            if(savedEntity.getApp_srl() != iAppSrl) {
                AppEntity appEntity = appDao.get(iAppSrl, null, null);
                if (appEntity == null) {
                    Map<String, String> reason = new HashMap<>();
                    reason.put("app_srl", "failed modify app_srl. not found app. app_srl [" +
                            appSrl +"]");
                    LOG.error(reason.get("app_srl"));
                    throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
                }

                documentCategoryEntity.setApp_srl(iAppSrl);
                willModify = true;
                isModifyBoardSrl = true;
            }
        }

        if(modifyValue.containsKey("board_srl")) {
            String boardSrl = modifyValue.get("board_srl");
            if(boardSrl == null || StringUtils.equals(StringUtils.trim(boardSrl), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("board_srl", "failed modify board_srl. board_srl is null or empty");
                LOG.error(reason.get("board_srl"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(boardSrl)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("board_srl", "failed modify board_srl. board_srl is not numeric. board_srl [" +
                        boardSrl + "]");
                LOG.error(reason.get("board_srl"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iBoardSrl = Integer.parseInt(boardSrl, 10);
            if(savedEntity.getBoard_srl() != iBoardSrl) {
                DocumentBoardEntity documentBoardEntity = documentBoardDao.get(iBoardSrl, MDV.NUSE);
                if (documentBoardEntity == null) {
                    Map<String, String> reason = new HashMap<>();
                    reason.put("board_srl", "failed modify board_srl. not found board. board_srl [" +
                            boardSrl +"]");
                    LOG.error(reason.get("board_srl"));
                    throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
                }

                documentCategoryEntity.setBoard_srl(iBoardSrl);
                willModify = true;
                isModifyBoardSrl = true;
            }
        }

        if(modifyValue.containsKey("category_name")) {
            String categoryName = modifyValue.get("category_name");
            if(categoryName == null || StringUtils.equals(StringUtils.trim(categoryName), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("category_name", "failed modify category_name. category_name is null or empty");
                LOG.error(reason.get("category_name"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int length = categoryName.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("category_name", "failed modify category_name. category_name length less then 64 byte. length [" +
                        length + "]");
                LOG.error(reason.get("category_name"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getCategory_name(), categoryName)) {
                documentCategoryEntity.setCategory_name(categoryName);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("category_description")) {
            String categoryDescription = modifyValue.get("category_description");
            if(categoryDescription == null) categoryDescription = "";
            categoryDescription = StringUtils.trim(categoryDescription);

            int length = categoryDescription.getBytes().length;
            if(length > 256) {
                Map<String, String> reason = new HashMap<>();
                reason.put("category_description", "failed modify category_description. category_description length less then 64 byte. length [" +
                        length + "]");
                LOG.error(reason.get("category_description"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getCategory_description(), categoryDescription)) {
                documentCategoryEntity.setCategory_description(categoryDescription);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("category_type")) {
            String categoryType = modifyValue.get("category_type");
            if(categoryType == null || StringUtils.equals(StringUtils.trim(categoryType), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("category_type", "failed modify category_type. category_type is null or empty");
                LOG.error(reason.get("category_type"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(categoryType)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("category_type", "failed modify category_type. category_type is not numeric. category_type [" +
                        categoryType +"]");
                LOG.error(reason.get("category_type"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iCategoryType = Integer.parseInt(categoryType, 10);

            if(iCategoryType != MDV.NORMAL_CATEGORY && iCategoryType != MDV.LINK_CATEGORY) {
                Map<String, String> reason = new HashMap<>();
                reason.put("category_type", "failed modify category_type. invalid category_type value [" +
                        categoryType +"]");
                LOG.error(reason.get("category_type"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getCategory_type() != iCategoryType) {
                documentCategoryEntity.setCategory_type(iCategoryType);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("enabled")) {
            String enabled = modifyValue.get("enabled");
            if(enabled == null || StringUtils.equals(StringUtils.trim(enabled), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is null or empty");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(enabled)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is not numeric. enabled [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iEnabled = Integer.parseInt(enabled, 10);
            if(iEnabled != MDV.YES && iEnabled != MDV.NO) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. invalid enabled value [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getEnabled() != iEnabled) {
                documentCategoryEntity.setEnabled(iEnabled);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("open_type")) {
            String openType = modifyValue.get("open_type");
            if(openType == null || StringUtils.equals(StringUtils.trim(openType), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("open_type", "failed modify open_type. open_type is null or empty");
                LOG.error(reason.get("open_type"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(openType)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("open_type", "failed modify open_type. open_type is not numeric. open_type [" +
                        openType +"]");
                LOG.error(reason.get("open_type"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iOpenType = Integer.parseInt(openType, 10);
            if(iOpenType != MDV.PUBLIC && iOpenType != MDV.PRIVATE) {
                Map<String, String> reason = new HashMap<>();
                reason.put("open_type", "failed modify open_type. invalid open_type value [" +
                        openType +"]");
                LOG.error(reason.get("open_type"));
                throw new CustomException("modify_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getOpen_type() != iOpenType) {
                documentCategoryEntity.setOpen_type(iOpenType);
                willModify = true;
            }
        }

        // 만일 바뀌는게 하나도 없다면 update 를 하지 않고 무시 한다.
        if(!willModify) {
            LOG.info("ignore modify document category. category_srl [" + categorySrl +
                    "]. it is same between saved value and changed value");
            return;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        documentCategoryEntity.setU_date(ltm);

        documentCategoryDao.modify(documentCategoryEntity, categorySrl, MDV.NUSE);
        LOG.info("modify category info. documentCategoryEntity [" + documentCategoryEntity.toString() +
                "], category_srl [" + categorySrl + "]");

        if(isModifyBoardSrl) {
            DocumentEntity documentEntity = new DocumentEntity();
            documentEntity.init();
            documentEntity.setBoard_srl(documentCategoryEntity.getBoard_srl());

            documentDao.modify(documentEntity, MDV.NUSE, categorySrl, MDV.NUSE);
            LOG.info("modify category of document. board_srl [" + documentCategoryEntity.getBoard_srl() +
                    "], category_srl [" + categorySrl + "]");

            // TODO 카테고리의 게시판 이동시 포함된 댓글도 같이 게시판 이동 시켜야 한다.

        }


    }

    @Transactional(readOnly = true)
    @Override
    public DocumentCategoryEntity getDocumentCategory(long categorySrl) {
        if(categorySrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("category_srl", "invalid category_srl is less then zero");
            LOG.error(reason.get("category_srl"));
            throw new CustomException("read_document_category_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        return documentCategoryDao.get(categorySrl, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentCategoryEntity> getDocumentCategory(int appSrl, long boardSrl, String categoryName,
                                                            int categoryType, int enabled, int openType,
                                                            Map<String, String> sort, int offset, int limit) {
        if(categoryName != null) {
            categoryName = StringUtils.trim(categoryName);
            if(StringUtils.equals(categoryName, "")) categoryName = null;
        }

        return documentCategoryDao.get(null, appSrl, boardSrl, null, categoryName, categoryType,
                enabled, openType, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentCategoryEntity> getDocumentCategory(Map<String, String> searchFilter,
                                                            Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null)
            return this.getDocumentCategory(MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                    MDV.NUSE, MDV.NUSE, sort, offset, limit);

        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        String categoryName = null;
        int categoryType = MDV.NUSE;
        int enabled = MDV.NUSE;
        int openType = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_name")) categoryName = searchFilter.get("category_name");
        if(searchFilter.containsKey("category_type")) {
            String num = searchFilter.get("category_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categoryType = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("open_type")) {
            String num = searchFilter.get("open_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) openType = Integer.parseInt(num, 10);
        }

        return this.getDocumentCategory(appSrl, boardSrl, categoryName, categoryType,
                enabled, openType, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentCategory(int appSrl, long boardSrl, String categoryName, int categoryType,
                                      int enabled, int openType) {
        if(categoryName != null) {
            categoryName = StringUtils.trim(categoryName);
            if(StringUtils.equals(categoryName, "")) categoryName = null;
        }

        return documentCategoryDao.count(appSrl, boardSrl, categoryName, categoryType, enabled, openType);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentCategory(Map<String, String> searchFilter) {
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        String categoryName = null;
        int categoryType = MDV.NUSE;
        int enabled = MDV.NUSE;
        int openType = MDV.NUSE;

        if(searchFilter == null)
            return this.countDocumentCategory(MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_name")) categoryName = searchFilter.get("category_name");
        if(searchFilter.containsKey("category_type")) {
            String num = searchFilter.get("category_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categoryType = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("open_type")) {
            String num = searchFilter.get("open_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) openType = Integer.parseInt(num, 10);
        }

        return this.countDocumentCategory(appSrl, boardSrl, categoryName, categoryType, enabled, openType);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentBoard(int appSrl, String boardName, int enabled, int openType) {
        if(boardName != null) {
            boardName = StringUtils.trim(boardName);
            if(StringUtils.equals(boardName, "")) boardName = null;
        }

        return documentBoardDao.count(appSrl, boardName, enabled, openType);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentBoard(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.countDocumentBoard(MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

        int appSrl = MDV.NUSE;
        String boardName = null;
        int enabled = MDV.NUSE;
        int openType = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_name")) boardName = searchFilter.get("board_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("open_type")) {
            String num = searchFilter.get("open_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) openType = Integer.parseInt(num, 10);
        }

        return this.countDocumentBoard(appSrl, boardName, enabled, openType);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentBoardEntity> getDocumentBoard(int appSrl, String boardName, int enabled, int openType,
                                                      Map<String, String> sort, int offset, int limit) {
        if(boardName != null) {
            boardName = StringUtils.trim(boardName);
            if(StringUtils.equals(boardName, "")) boardName = null;
        }

        return documentBoardDao.get(null, appSrl, boardName, enabled, openType,
                sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentBoardEntity> getDocumentBoard(Map<String, String> searchFilter, Map<String, String> sort,
                                                      int offset, int limit) {
        if(searchFilter == null) return this.getDocumentBoard(MDV.NUSE, null, MDV.NUSE, MDV.NUSE, sort, offset, limit);

        int appSrl = MDV.NUSE;
        String boardName = null;
        int enabled = MDV.NUSE;
        int openType = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_name")) boardName = searchFilter.get("board_name");
        if(searchFilter.containsKey("enabled")) {
            String num = searchFilter.get("enabled");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) enabled = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("open_type")) {
            String num = searchFilter.get("open_type");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) openType = Integer.parseInt(num, 10);
        }

        return this.getDocumentBoard(appSrl, boardName, enabled, openType,
                sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public DocumentBoardEntity getDocumentBoard(long boardSrl) {
        if(boardSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "invalid board_srl is less then zero");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("read_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        return documentBoardDao.get(boardSrl, MDV.NUSE);
    }

    @Override
    public void addDocumentBoard(DocumentBoardEntity documentBoardEntity) {
        if(documentBoardEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentBoardEntity", "invalid documentBoardEntity is null");
            LOG.error(reason.get("documentBoardEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentBoardEntity.getApp_srl() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. app_srl is less then zero");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        AppEntity appEntity = appDao.get(documentBoardEntity.getApp_srl(), null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "invalid app_srl. not found app. app_srl [" +
                    documentBoardEntity.getApp_srl() + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("add_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentBoardEntity.getBoard_name() == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_name", "invalid board_name. board_name is null");
            LOG.error(reason.get("board_name"));
            throw new CustomException("add_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        documentBoardEntity.setBoard_name(StringUtils.trim(documentBoardEntity.getBoard_name()));
        if(StringUtils.equals(documentBoardEntity.getBoard_name(), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_name", "invalid board_name. board_name is empty string");
            LOG.error(reason.get("board_name"));
            throw new CustomException("add_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentBoardEntity.getBoard_description() == null) documentBoardEntity.setBoard_description("");
        documentBoardEntity.setBoard_description(StringUtils.trim(documentBoardEntity.getBoard_description()));

        if(documentBoardEntity.getEnabled() != MDV.YES && documentBoardEntity.getEnabled() != MDV.NO) {
            Map<String, String> reason = new HashMap<>();
            reason.put("enabled", "invalid enabled. enabled is unknown value. enabled [" +
                    documentBoardEntity.getEnabled() + "]");
            LOG.error(reason.get("enabled"));
            throw new CustomException("add_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(documentBoardEntity.getOpen_type() != MDV.PUBLIC && documentBoardEntity.getOpen_type() != MDV.PRIVATE) {
            Map<String, String> reason = new HashMap<>();
            reason.put("open_type", "invalid open_type. open_type is unknown value. open_type [" +
                    documentBoardEntity.getOpen_type() + "]");
            LOG.error(reason.get("open_type"));
            throw new CustomException("add_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        documentBoardEntity.setC_date(ltm);
        documentBoardEntity.setU_date(ltm);

        documentBoardDao.add(documentBoardEntity);
        LOG.info("add document board. documentBoardEntity [" + documentBoardEntity.toString() + "]");
    }

    @Override
    public void deleteDocumentBoard(List<Long> boardSrls) {
        if(boardSrls == null || boardSrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srls", "invalid board_srls. board_srls is null or size zero");
            LOG.error(reason.get("board_srls"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        documentBoardDao.delete(MDV.NUSE, boardSrls);
        LOG.info("delete document board. board_srls [" + boardSrls.toString() + "]");
    }

    @Override
    public void modifyDocumentBoard(long boardSrl, Map<String, String> modifyValue) {
        if(boardSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("board_srl", "invalid board_srl. board_srl is less then zero. board_srl [" + boardSrl + "]");
            LOG.error(reason.get("board_srl"));
            throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(modifyValue == null || modifyValue.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "invalid modify value. modify value is null or empty. board_srl [" + boardSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(!modifyValue.containsKey("board_name") && !modifyValue.containsKey("board_description") &&
                !modifyValue.containsKey("enabled") && !modifyValue.containsKey("open_type")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("modify_value", "not found board modify value. modifyValue [" + modifyValue.toString() +
                    "], board_srl [" + boardSrl +"]");
            LOG.error(reason.get("modify_value"));
            throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        DocumentBoardEntity savedEntity = documentBoardDao.get(boardSrl, MDV.NUSE);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("not_found", "not found board. board_srl [" + boardSrl + "]");
            LOG.error(reason.get("not_found"));
            throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        boolean willModify = false;
        DocumentBoardEntity documentBoardEntity = new DocumentBoardEntity();
        documentBoardEntity.init();

        if(modifyValue.containsKey("board_name")) {
            String boardName = modifyValue.get("board_name");
            if(boardName == null || StringUtils.equals(StringUtils.trim(boardName), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("board_name", "failed modify board_name. board_name is null or empty");
                LOG.error(reason.get("board_name"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int length = boardName.getBytes().length;
            if(length > 64) {
                Map<String, String> reason = new HashMap<>();
                reason.put("board_name", "failed modify board_name. board_name length less then 64 byte. length [" +
                        length + "]");
                LOG.error(reason.get("board_name"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getBoard_name(), boardName)) {
                documentBoardEntity.setBoard_name(boardName);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("board_description")) {
            String boardDescription = modifyValue.get("board_description");
            if(boardDescription == null) boardDescription = "";
            boardDescription = StringUtils.trim(boardDescription);

            int length = boardDescription.getBytes().length;
            if(length > 256) {
                Map<String, String> reason = new HashMap<>();
                reason.put("board_description", "failed modify board_description. board_description length less then 64 byte. length [" +
                        length + "]");
                LOG.error(reason.get("board_description"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            if(!StringUtils.equals(savedEntity.getBoard_description(), boardDescription)) {
                documentBoardEntity.setBoard_description(boardDescription);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("enabled")) {
            String enabled = modifyValue.get("enabled");
            if(enabled == null || StringUtils.equals(StringUtils.trim(enabled), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is null or empty");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(enabled)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. enabled is not numeric. enabled [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iEnabled = Integer.parseInt(enabled, 10);
            if(iEnabled != MDV.YES && iEnabled != MDV.NO) {
                Map<String, String> reason = new HashMap<>();
                reason.put("enabled", "failed modify enabled. invalid enabled value [" +
                        enabled +"]");
                LOG.error(reason.get("enabled"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getEnabled() != iEnabled) {
                documentBoardEntity.setEnabled(iEnabled);
                willModify = true;
            }
        }

        if(modifyValue.containsKey("open_type")) {
            String openType = modifyValue.get("open_type");
            if(openType == null || StringUtils.equals(StringUtils.trim(openType), "")) {
                Map<String, String> reason = new HashMap<>();
                reason.put("open_type", "failed modify open_type. open_type is null or empty");
                LOG.error(reason.get("open_type"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(!NumberUtils.isNumber(openType)) {
                Map<String, String> reason = new HashMap<>();
                reason.put("open_type", "failed modify open_type. open_type is not numeric. open_type [" +
                        openType +"]");
                LOG.error(reason.get("open_type"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }

            int iOpenType = Integer.parseInt(openType, 10);
            if(iOpenType != MDV.PUBLIC && iOpenType != MDV.PRIVATE) {
                Map<String, String> reason = new HashMap<>();
                reason.put("open_type", "failed modify open_type. invalid open_type value [" +
                        openType +"]");
                LOG.error(reason.get("open_type"));
                throw new CustomException("modify_document_board_error", MDV.HTTP_ERR_X_REQUEST, reason);
            }
            if(savedEntity.getOpen_type() != iOpenType) {
                documentBoardEntity.setOpen_type(iOpenType);
                willModify = true;
            }
        }

        // 만일 바뀌는게 하나도 없다면 update 를 하지 않고 무시 한다.
        if(!willModify) {
            LOG.info("ignore modify document board. board_srl [" + boardSrl +
                    "]. it is same between saved value and changed value");
            return;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        documentBoardEntity.setU_date(ltm);

        documentBoardDao.modify(documentBoardEntity, boardSrl);
        LOG.info("modify board info. documentBoardEntity [" + documentBoardEntity.toString() +
                "], board_srl [" + boardSrl + "]");
    }

    @Override
    public void addDocumentTemplate(TemplateEntity templateEntity) {
        if(templateEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("templateEntity", "invalid templateEntity is null");
            LOG.error(reason.get("templateEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(templateEntity.getTemplate_title() == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_title", "invalid template_title. template_title is null");
            LOG.error(reason.get("template_title"));
            throw new CustomException("add_document_template_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        templateEntity.setTemplate_title(StringUtils.trim(templateEntity.getTemplate_title()));
        if(StringUtils.equals(templateEntity.getTemplate_title(), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_title", "invalid template_title. template_title is empty string");
            LOG.error(reason.get("template_title"));
            throw new CustomException("add_document_template_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(templateEntity.getTemplate_content() == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_content", "invalid template_content. template_content is null");
            LOG.error(reason.get("template_content"));
            throw new CustomException("add_document_template_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        templateEntity.setTemplate_content(StringUtils.trim(templateEntity.getTemplate_content()));
        if(StringUtils.equals(templateEntity.getTemplate_content(), "")) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_content", "invalid template_content. template_content is empty string");
            LOG.error(reason.get("template_content"));
            throw new CustomException("add_document_template_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(templateEntity.getTemplate_description() == null) templateEntity.setTemplate_description("");
        templateEntity.setTemplate_description(StringUtils.trim(templateEntity.getTemplate_description()));

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        templateEntity.setC_date(ltm);
        templateEntity.setU_date(ltm);

        templateDao.add(templateEntity);
        LOG.info("add template. templateEntity [" + templateEntity.toString() + "]");

        // 추가한 템플릿과 앱 매핑
        if(templateEntity.getApp_srls() == null || templateEntity.getApp_srls().size() <= 0) {
            LOG.warn("added template but no app_srls. can\'t mapping");
            return;
        }

        AppTemplateEntity appTemplateEntity = new AppTemplateEntity();
        appTemplateEntity.setTemplate_srl(templateEntity.getTemplate_srl());
        appTemplateEntity.setC_date(ltm);
        for(int appSrl : templateEntity.getApp_srls()) {
            appTemplateEntity.setApp_srl(appSrl);

            appTemplateDao.add(appTemplateEntity);
            LOG.info("add app template. appTemplateEntity [" + appTemplateEntity.toString() + "]");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentTemplate(int templateSrl, String templateTitle) {
        if(templateTitle != null) {
            templateTitle = StringUtils.trim(templateTitle);
            if(StringUtils.equals(templateTitle, "")) templateTitle = null;
        }

        return templateDao.count(templateSrl, templateTitle);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentTemplate(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.countDocumentTemplate(MDV.NUSE, null);

        String templateTitle = null;

        if(searchFilter.containsKey("template_title")) templateTitle = searchFilter.get("template_title");

        return this.countDocumentTemplate(MDV.NUSE, templateTitle);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TemplateEntity> getDocumentTemplate(List<Integer> templateSrls, String templateTitle,
                                                    Map<String, String> sort, int offset, int limit) {
        if(templateTitle != null) {
            templateTitle = StringUtils.trim(templateTitle);
            if(StringUtils.equals(templateTitle, "")) templateTitle = null;
        }

        List<TemplateEntity> templateEntities = templateDao.get(templateSrls, templateTitle, sort, offset, limit);

        // template 을 사용하는 app count 를 구한다.
        List<Integer> tSrls = new ArrayList<>();
        for(TemplateEntity templateEntity : templateEntities) tSrls.add(templateEntity.getTemplate_srl());

        List<Map<String, Object>> countTemplateUsingApps =  appTemplateDao.countTemplateUsingApp(tSrls);
        Map<Integer, Long> countMap = new HashMap<>();

        for(Map<String, Object> map : countTemplateUsingApps) {
            int srl = Integer.parseInt(map.get("template_srl").toString(), 10);
            if(!countMap.containsKey(srl)) countMap.put(srl, Long.parseLong(map.get("cnt").toString(), 10));
        }

        for(TemplateEntity templateEntity : templateEntities) {
            int srl = templateEntity.getTemplate_srl();

            if(countMap.containsKey(srl))   templateEntity.setApp_count(countMap.get(srl));
            else                            templateEntity.setApp_count(0);
        }

        return templateEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TemplateEntity> getDocumentTemplate(Map<String, String> searchFilter, Map<String, String> sort,
                                                    int offset, int limit) {
        if(searchFilter == null) return this.getDocumentTemplate(null, null, sort, offset, limit);

        String templateTitle = null;
        if(searchFilter.containsKey("template_title")) templateTitle = searchFilter.get("template_title");

        return this.getDocumentTemplate(null, templateTitle, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Integer, JSONObject> getDocumentTemplate(List<Integer> templateSrls) {

        Map<Integer, JSONObject> ret = new HashMap<>();
        List<TemplateEntity> templateEntities = templateDao.get(templateSrls, null, null, MDV.NUSE, MDV.NUSE);

        if(templateEntities.size() <= 0) return ret;

        for(TemplateEntity templateEntity : templateEntities) {
            int templateSrl = templateEntity.getTemplate_srl();
            if(ret.containsKey(templateSrl)) continue;

            if(templateEntity.getTemplate_content() == null) {
                ret.put(templateSrl, null);
                continue;
            }

            String templateContent = StringUtils.trim(templateEntity.getTemplate_content());
            if(StringUtils.equals(templateContent, "")) {
                ret.put(templateSrl, null);
                continue;
            }

            try {
                JSONObject jsonObject = JSONObject.fromObject(templateContent);
                ret.put(templateSrl, jsonObject);
            } catch(Exception e) {
                LOG.warn("invalid template struct. it is not json data. template_srl [" + templateSrl + "]");
                ret.put(templateSrl, null);
            }
        }

        return ret;
    }

    @Transactional(readOnly = true)
    @Override
    public TemplateEntity getDocumentTemplate(int templateSrl) {
        if(templateSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_srl", "template_srl is less then zero. template_srl ["+templateSrl+"]");
            LOG.error(reason.get("template_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        return templateDao.get(templateSrl);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppEntity> getAppInfoUsingTemplate(int templateSrl) {
        List<AppTemplateEntity> appTemplateEntities = appTemplateDao.get(MDV.NUSE, templateSrl, null,
                MDV.NUSE, MDV.NUSE);

        if(appTemplateEntities.size() <= 0) return new ArrayList<>();

        List<Integer> appSrls = new ArrayList<>();
        for(AppTemplateEntity appTemplateEntity : appTemplateEntities)
            appSrls.add(appTemplateEntity.getApp_srl());

        return appDao.get(appSrls, null, null, null, MDV.YES, MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TemplateEntity> getTemplateOfApp(int appSrl) {
        if(appSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "app_srl is less then zero. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        AppEntity appEntity = appDao.get(appSrl, null, null);
        if(appEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("app_srl", "not found app. app_srl [" + appSrl + "]");
            LOG.error(reason.get("app_srl"));
            throw new CustomException("read_app_error", reason);
        }

        List<Integer> templateSrls= new ArrayList<>();
        List<AppTemplateEntity> appTemplateEntities = appTemplateDao.get(appSrl, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        for(AppTemplateEntity appTemplateEntity : appTemplateEntities) {
            int templateSrl = appTemplateEntity.getTemplate_srl();
            if(!templateSrls.contains(templateSrl)) templateSrls.add(templateSrl);
        }

        Map<String, String> sort = new HashMap<>();
        sort.put("template_title", "ASC");
        return templateDao.get(templateSrls, null, sort, MDV.NUSE, MDV.NUSE);
    }

    @Override
    public void deleteDocumentTemplate(List<Integer> templateSrls) {
        if(templateSrls == null || templateSrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_srls", "invalid template_srls. template_srls is null or size zero");
            LOG.error(reason.get("template_srls"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        templateDao.delete(MDV.NUSE, templateSrls);
        LOG.info("delete template. template_srls [" + templateSrls.toString() + "]");
    }

    @Override
    public void modifyDocumentTemplate(TemplateEntity templateEntity, int templateSrl) {
        if(templateSrl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template_srl", "invalid template_srl. template_srl is less then zero");
            LOG.error(reason.get("template_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(templateEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("templateEntity", "invalid templateEntity. templateEntity is null");
            LOG.error(reason.get("templateEntity"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        TemplateEntity savedEntity = templateDao.get(templateSrl);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("template", "no template for modify. template_srl [" + templateSrl + "]");
            LOG.error(reason.get("template"));
            throw new CustomException("modify_document_template_error", reason);
        }

        boolean willModify = false;
        TemplateEntity updateEntity = new TemplateEntity();
        updateEntity.init();

        if(templateEntity.getTemplate_title() != null &&
                !StringUtils.equals(templateEntity.getTemplate_title(), savedEntity.getTemplate_title())) {
            updateEntity.setTemplate_title(StringUtils.trim(templateEntity.getTemplate_title()));
            willModify = true;
        }

        if(templateEntity.getTemplate_description() != null &&
                !StringUtils.equals(templateEntity.getTemplate_description(), savedEntity.getTemplate_description())) {
            updateEntity.setTemplate_description(StringUtils.trim(templateEntity.getTemplate_description()));
            willModify = true;
        }

        if(templateEntity.getTemplate_content() != null &&
                !StringUtils.equals(templateEntity.getTemplate_content(), savedEntity.getTemplate_content())) {
            updateEntity.setTemplate_content(StringUtils.trim(templateEntity.getTemplate_content()));
            willModify = true;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        if(!willModify) {
            LOG.info("ignore modify document template. template_srl [" + templateSrl +"]. it is same between saved value and changed value");
        } else {
            updateEntity.setU_date(ltm);
            templateDao.modify(updateEntity, templateSrl);
            LOG.info("modify template. updateEntity [" + updateEntity.toString() + "]");
        }

        if(templateEntity.getApp_srls() == null) {
            LOG.debug("no changed app, app template mapping");
            return;
        }

        // 앱 매핑 정보 업데이트
        List<AppTemplateEntity> appTemplateEntities = appTemplateDao.get(MDV.NUSE, templateSrl,
                null, MDV.NUSE, MDV.NUSE);

        List<Integer> savedAppSrls = new ArrayList<>();
        for(AppTemplateEntity appTemplateEntity : appTemplateEntities)
            savedAppSrls.add(appTemplateEntity.getApp_srl());

        // 매핑 삭제 할 것이 있으면 삭제 한다
        List<Integer> deleteAppSrls = new ArrayList<>();
        for(int srl : savedAppSrls) {
            if(!templateEntity.getApp_srls().contains(srl)) deleteAppSrls.add(srl);
        }

        if(deleteAppSrls.size() > 0) {
            appTemplateDao.delete(MDV.NUSE, deleteAppSrls, templateSrl);
            LOG.info("delete app template mapping. template_srl [" + templateSrl + "], app_srls [" +
                    deleteAppSrls.toString() + "]");
        }

        // 추가 할게 있으면 추가 한다.
        List<Integer> addAppSrls = new ArrayList<>();
        for(int srl : templateEntity.getApp_srls()) {
            if(!savedAppSrls.contains(srl)) addAppSrls.add(srl);
        }

        if(addAppSrls.size() > 0) {
            AppTemplateEntity appTemplateEntity = new AppTemplateEntity();
            appTemplateEntity.setTemplate_srl(templateSrl);
            appTemplateEntity.setC_date(ltm);
            for(int srl : addAppSrls) {
                appTemplateEntity.setApp_srl(srl);
                appTemplateDao.add(appTemplateEntity);
                LOG.info("add app template mapping. template_srl [" + templateSrl + "], app_srl [" + srl + "]");
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentTag(int appSrl, long memberSrl, String tagName, int adminTag) {
        if(tagName != null) {
            tagName = StringUtils.trim(tagName);
            if(StringUtils.equals(tagName, "")) tagName = null;
        }

        return tagDao.count(appSrl, memberSrl, tagName, adminTag);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentTag(Map<String, String> searchFilter) {
        if(searchFilter == null) return this.countDocumentTag(MDV.NUSE, MDV.NUSE, null, MDV.NUSE);

        int appSrl = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String tagName = null;
        int adminTag = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("tag_name")) tagName = searchFilter.get("tag_name");
        if(searchFilter.containsKey("admin_tag")) {
            String num = searchFilter.get("admin_tag");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) adminTag = Integer.parseInt(num, 10);
        }

        return this.countDocumentTag(appSrl, memberSrl, tagName, adminTag);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TagEntity> getDocumentTag(int appSrl, long memberSrl, String tagName, int adminTag,
                                          Map<String, String> sort, int offset, int limit) {
        if(tagName != null) {
            tagName = StringUtils.trim(tagName);
            if(StringUtils.equals(tagName, "")) tagName = null;
        }

        return tagDao.get(null, appSrl, memberSrl, tagName, adminTag, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TagEntity> getDocumentTag(Map<String, String> searchFilter, Map<String, String> sort,
                                          int offset, int limit) {
        if(searchFilter == null)
            return this.getDocumentTag(MDV.NUSE, MDV.NUSE, null, MDV.NUSE, sort, offset, limit);

        int appSrl = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String tagName = null;
        int adminTag = MDV.NUSE;

        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("tag_name")) tagName = searchFilter.get("tag_name");
        if(searchFilter.containsKey("admin_tag")) {
            String num = searchFilter.get("admin_tag");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) adminTag = Integer.parseInt(num, 10);
        }

        return this.getDocumentTag(appSrl, memberSrl, tagName, adminTag, sort, offset, limit);
    }

    @Override
    public void addDocumentLink(DocumentLinkEntity documentLinkEntity) {
        if(documentLinkEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentLinkEntity", "invalid documentLinkEntity is null");
            LOG.error(reason.get("documentLinkEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        documentLinkEntity.setC_date(ltm);

        documentLinkDao.add(documentLinkEntity);
        LOG.info("add document link. documentLinkEntity [" + documentLinkEntity + "]");

        DocumentLinkEntity updateEntity = new DocumentLinkEntity();
        updateEntity.setList_order(documentLinkEntity.getDocument_link_srl());

        documentLinkDao.modify(updateEntity, documentLinkEntity.getDocument_link_srl(), MDV.NUSE, MDV.NUSE);
        LOG.info("set list order of document link. document_link_srl [" +
                documentLinkEntity.getDocument_link_srl() + "], list_order [" +
                updateEntity.getList_order() + "]");
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentLink(long documentSrl, List<Long> documentSrls, int appSrl, long boardSrl,
                                  List<Long> boardSrls, long categorySrl, List<Long> categorySrls,
                                  String documentTitle, long secret, List<Long> secrets, int block,
                                  int allowComment, int allowNotice, long memberSrl, String userId,
                                  String userName, String nickName) {
        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        return documentLinkDao.countFullInfo(documentSrl, documentSrls, appSrl, boardSrl, boardSrls,
                categorySrl, categorySrls, documentTitle, secret, secrets, block, allowComment,
                allowNotice, memberSrl, userId, userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentLink(Map<String, String> searchFilter, List<Long> secrets) {
        long documentSrl = MDV.NUSE;
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter == null)
            return this.countDocumentLink(MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, null, null,
                    MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);

        if(searchFilter.containsKey("document_srl")) {
            String num = searchFilter.get("document_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) documentSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.countDocumentLink(documentSrl, null, appSrl, boardSrl, null, categorySrl, null, documentTitle,
                secret, secrets, block, allowComment, allowNotice, memberSrl, userId, userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentLinkEntity> getDocumentLink(List<Long> documentLinkSrls, long documentSrl,
                                                    List<Long> documentSrls, int appSrl, long boardSrl,
                                                    List<Long> boardSrls, long categorySrl, List<Long> categorySrls,
                                                    String documentTitle, long secret, List<Long> secrets, int block,
                                                    int allowComment, int allowNotice, long memberSrl, String userId,
                                                    String userName, String nickName, Map<String, String> sort,
                                                    int offset, int limit) {
        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        return documentLinkDao.getFullInfo(documentLinkSrls, documentSrl, documentSrls, appSrl, boardSrl, boardSrls,
                categorySrl, categorySrls, documentTitle, secret, secrets, block, allowComment, allowNotice,
                memberSrl, userId, userName, nickName, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentLinkEntity> getDocumentLink(Map<String, String> searchFilter, List<Long> secrets,
                                                    Map<String, String> sort, int offset, int limit) {
        if(searchFilter == null)
            return this.getDocumentLink(null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, null, null,
                    MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null, sort, offset, limit);

        long documentSrl = MDV.NUSE;
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter.containsKey("document_srl")) {
            String num = searchFilter.get("document_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) documentSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.getDocumentLink(null, documentSrl, null, appSrl, boardSrl, null, categorySrl, null, documentTitle,
                secret, secrets, block, allowComment, allowNotice, memberSrl, userId, userName, nickName,
                sort, offset, limit);
    }

    @Override
    public void deleteDocumentLink(List<Long> documentLinkSrls) {
        if(documentLinkSrls == null || documentLinkSrls.size() <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_link_srls", "invalid document_link_srls. document_link_srls is null or size zero");
            LOG.error(reason.get("document_link_srls"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        // 게시물 링크를 삭제 한다. 링크만 삭제하고 원본은 건드리지 않는다.
        documentLinkDao.delete(MDV.NUSE, documentLinkSrls);
        LOG.info("delete document link. document_link_srls [" + documentLinkSrls.toString() + "]");
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentOfTag(long tagSrl, List<Long> tagSrls, long documentSrl, List<Long> documentSrls,
                                   int appSrl, long boardSrl, List<Long> boardSrls, long categorySrl,
                                   List<Long> categorySrls, String documentTitle, long secret, List<Long> secrets,
                                   int block, int allowComment, int allowNotice, long memberSrl,
                                   String userId, String userName, String nickName) {
        if(tagSrl <= 0 && tagSrls == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("tag_srl", "tag_srl is less then zero and tag_srls is null");
            LOG.error(reason.get("tag_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        return documentTagDao.countFullInfo(tagSrl, tagSrls, documentSrl, documentSrls, appSrl, boardSrl,
                boardSrls, categorySrl, categorySrls, documentTitle, secret, secrets, block,
                allowComment, allowNotice, memberSrl, userId, userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentOfTag(Map<String, String> searchFilter, List<Long> tagSrls, List<Long> secrets) {
        long tagSrl = MDV.NUSE;
        long documentSrl = MDV.NUSE;
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter == null)
            return this.countDocumentOfTag(MDV.NUSE, tagSrls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                    null, MDV.NUSE, null, null, MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE,
                    MDV.NUSE, MDV.NUSE, null, null, null);

        if(searchFilter.containsKey("tag_srl")) {
            String num = searchFilter.get("tag_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) tagSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("document_srl")) {
            String num = searchFilter.get("document_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) documentSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.countDocumentOfTag(tagSrl, tagSrls, documentSrl, null, appSrl, boardSrl, null, categorySrl,
                null, documentTitle, secret, secrets, block, allowComment, allowNotice, memberSrl, userId, userName,
                nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentEntity> getDocumentOfTag(long tagSrl, List<Long> tagSrls, long documentSrl,
                                                 List<Long> documentSrls, int appSrl, long boardSrl,
                                                 List<Long> boardSrls, long categorySrl, List<Long> categorySrls,
                                                 String documentTitle, long secret, List<Long> secrets, int block,
                                                 int allowComment, int allowNotice, long memberSrl, String userId,
                                                 String userName, String nickName, Map<String, String> sort,
                                                 int offset, int limit) {
        if(tagSrl <= 0 && tagSrls == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("tag_srl", "tag_srl is less then zero and tag_srls is null");
            LOG.error(reason.get("tag_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        List<DocumentTagEntity> documentTagEntities = documentTagDao.getFullInfo(tagSrl, tagSrls, documentSrl,
                documentSrls, appSrl, boardSrl, boardSrls, categorySrl, categorySrls, documentTitle,
                secret, secrets, block, allowComment, allowNotice, memberSrl, userId, userName, nickName,
                sort, offset, limit);

        List<DocumentEntity> documentEntities = new ArrayList<>();
        if(documentTagEntities.size() <= 0) return documentEntities;

        for(DocumentTagEntity documentTagEntity : documentTagEntities)
            documentEntities.add(documentTagEntity.getDocumentEntity());

        return documentEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentEntity> getDocumentOfTag(Map<String, String> searchFilter, List<Long> tagSrls,
                                                 List<Long> secrets, Map<String, String> sort, int offset, int limit) {
        long tagSrl = MDV.NUSE;
        long documentSrl = MDV.NUSE;
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter == null)
            return this.getDocumentOfTag(MDV.NUSE, tagSrls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                    null, null, MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null,
                    sort, offset, limit);

        if(searchFilter.containsKey("tag_srl")) {
            String num = searchFilter.get("tag_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) tagSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("document_srl")) {
            String num = searchFilter.get("document_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) documentSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.getDocumentOfTag(tagSrl, tagSrls, documentSrl, null, appSrl, boardSrl, null, categorySrl,
                null, documentTitle, secret, secrets, block, allowComment, allowNotice, memberSrl, userId,
                userName, nickName, sort, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentLinkOfTag(long tagSrl, List<Long> tagSrls, long documentSrl, List<Long> documentSrls,
                                       int appSrl, long boardSrl, List<Long> boardSrls, long categorySrl,
                                       List<Long> categorySrls, String documentTitle, long secret,
                                       List<Long> secrets, int block, int allowComment, int allowNotice,
                                       long memberSrl, String userId, String userName, String nickName) {
        if(tagSrl <= 0 && tagSrls == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("tag_srl", "tag_srl is less then zero and tag_srls is null");
            LOG.error(reason.get("tag_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        return documentTagDao.countFullLinkInfo(tagSrl, tagSrls, documentSrl, documentSrls, appSrl, boardSrl,
                boardSrls, categorySrl, categorySrls, documentTitle, secret, secrets, block, allowComment,
                allowNotice, memberSrl, userId, userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentLinkOfTag(Map<String, String> searchFilter, List<Long> tagSrls, List<Long> secrets) {
        long tagSrl = MDV.NUSE;
        long documentSrl = MDV.NUSE;
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter == null)
            return this.countDocumentLinkOfTag(MDV.NUSE, tagSrls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null,
                    MDV.NUSE, null, null, MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE,
                    null, null, null);

        if(searchFilter.containsKey("tag_srl")) {
            String num = searchFilter.get("tag_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) tagSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("document_srl")) {
            String num = searchFilter.get("document_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) documentSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.countDocumentLinkOfTag(tagSrl, tagSrls, documentSrl, null, appSrl, boardSrl, null, categorySrl,
                null, documentTitle, secret, secrets, block, allowComment, allowNotice, memberSrl, userId,
                userName, nickName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentLinkEntity> getDocumentLinkOfTag(long tagSrl, List<Long> tagSrls, long documentSrl,
                                                         List<Long> documentSrls, int appSrl, long boardSrl,
                                                         List<Long> boardSrls, long categorySrl, List<Long> categorySrls,
                                                         String documentTitle, long secret, List<Long> secrets,
                                                         int block, int allowComment, int allowNotice, long memberSrl,
                                                         String userId, String userName, String nickName,
                                                         Map<String, String> sort, int offset, int limit) {
        if(tagSrl <= 0 && tagSrls == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("tag_srl", "tag_srl is less then zero and tag_srls is null");
            LOG.error(reason.get("tag_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        if(documentTitle != null) {
            documentTitle = StringUtils.trim(documentTitle);
            if(StringUtils.equals(documentTitle, "")) documentTitle = null;
        }
        if(userId != null) {
            userId = StringUtils.trim(userId);
            if(StringUtils.equals(userId, "")) userId = null;
        }
        if(userName != null) {
            userName = StringUtils.trim(userName);
            if(StringUtils.equals(userName, "")) userName = null;
        }
        if(nickName != null) {
            nickName = StringUtils.trim(nickName);
            if(StringUtils.equals(nickName, "")) nickName = null;
        }

        List<DocumentTagEntity> documentTagEntities =  documentTagDao.getFullLinkInfo(tagSrl, tagSrls, documentSrl,
                documentSrls, appSrl, boardSrl, boardSrls, categorySrl, categorySrls, documentTitle, secret, secrets,
                block, allowComment, allowNotice, memberSrl, userId, userName, nickName, sort, offset, limit);

        List<DocumentLinkEntity> documentLinkEntities = new ArrayList<>();
        if(documentTagEntities.size() <= 0) return documentLinkEntities;

        for(DocumentTagEntity documentTagEntity : documentTagEntities)
            documentLinkEntities.add(documentTagEntity.getDocumentLinkEntity());

        return documentLinkEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentLinkEntity> getDocumentLinkOfTag(Map<String, String> searchFilter, List<Long> tagSrls,
                                                         List<Long> secrets, Map<String, String> sort,
                                                         int offset, int limit) {
        long tagSrl = MDV.NUSE;
        long documentSrl = MDV.NUSE;
        int appSrl = MDV.NUSE;
        long boardSrl = MDV.NUSE;
        long categorySrl = MDV.NUSE;
        String documentTitle = null;
        long secret = MDV.NUSE;
        int block = MDV.NUSE;
        int allowComment = MDV.NUSE;
        int allowNotice = MDV.NUSE;
        long memberSrl = MDV.NUSE;
        String userId = null;
        String userName = null;
        String nickName = null;

        if(searchFilter == null)
            return this.getDocumentLinkOfTag(MDV.NUSE, tagSrls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE,
                    null, null, MDV.NUSE, secrets, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null,
                    sort, offset, limit);

        if(searchFilter.containsKey("tag_srl")) {
            String num = searchFilter.get("tag_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) tagSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("document_srl")) {
            String num = searchFilter.get("document_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) documentSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("app_srl")) {
            String num = searchFilter.get("app_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) appSrl = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("board_srl")) {
            String num = searchFilter.get("board_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) boardSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("category_srl")) {
            String num = searchFilter.get("category_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) categorySrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("document_title")) documentTitle = searchFilter.get("document_title");
        if(secrets == null || secrets.size() <= 0) {
            if (searchFilter.containsKey("secret")) {
                String num = searchFilter.get("secret");
                if (num != null) {
                    num = StringUtils.trim(num);
                }
                if (!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) secret = Integer.parseInt(num, 10);
            }
        }
        if(searchFilter.containsKey("block")) {
            String num = searchFilter.get("block");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) block = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_comment")) {
            String num = searchFilter.get("allow_comment");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowComment = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("allow_notice")) {
            String num = searchFilter.get("allow_notice");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) allowNotice = Integer.parseInt(num, 10);
        }
        if(searchFilter.containsKey("member_srl")) {
            String num = searchFilter.get("member_srl");
            if(num != null) { num = StringUtils.trim(num); }
            if(!StringUtils.equals(num, "") && NumberUtils.isNumber(num)) memberSrl = Long.parseLong(num, 10);
        }
        if(searchFilter.containsKey("user_id")) userId = searchFilter.get("user_id");
        if(searchFilter.containsKey("user_name")) userName = searchFilter.get("user_name");
        if(searchFilter.containsKey("nick_name")) nickName = searchFilter.get("nick_name");

        return this.getDocumentLinkOfTag(tagSrl, tagSrls, documentSrl, null, appSrl, boardSrl, null, categorySrl, null,
                documentTitle, secret, secrets, block, allowComment, allowNotice, memberSrl, userId, userName,
                nickName, sort, offset, limit);
    }

    @Override
    public void getReadCount(long documentSrl){

        documentDao.increase(documentSrl,true,false,false,false,false);

    }
}
