package com.ckstack.ckpush.service.board.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.board.DocumentCommentDao;
import com.ckstack.ckpush.domain.board.DocumentCommentEntity;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.service.board.CommentService;
import com.ckstack.ckpush.service.board.DocumentService;
import com.ckstack.ckpush.service.board.SupervisorService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by kodaji on 2016. 1. 31..
 */
@Service
@Transactional(value = "transactionManager")
public class SupervisorServiceImpl implements SupervisorService {
    private final static Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    private DocumentService documentService;

    @Override
    public Map<String, Object> getJsonTemplateExtra(DocumentEntity documentEntity) {

        if(documentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentEntity", "documentEntity is null");
            LOG.error(reason.get("documentEntity"));
            throw new CustomException("read_document_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        Map<String, Object> ret = new HashMap<>();


        // 게시물의 template json 데이터를 넣는다.
        if(documentEntity.getTemplate_srl() > 0 && documentEntity.getTemplate_extra() != null) {
            List<Integer> templateSrls = new ArrayList<>();
            templateSrls.add(documentEntity.getTemplate_srl());
            Map<Integer, JSONObject> jsonStructMap = documentService.getDocumentTemplate(templateSrls);
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

        return ret;
    }


}
