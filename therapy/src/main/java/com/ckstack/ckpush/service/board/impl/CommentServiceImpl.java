package com.ckstack.ckpush.service.board.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.board.DocumentCommentDao;
import com.ckstack.ckpush.dao.mineral.DocumentAttachDao;
import com.ckstack.ckpush.domain.board.DocumentCommentEntity;
import com.ckstack.ckpush.domain.board.DocumentLinkEntity;
import com.ckstack.ckpush.service.board.CommentService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kodaji on 2016. 1. 31..
 */
@Service
@Transactional(value = "transactionManager")
public class CommentServiceImpl implements CommentService {
    private final static Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Autowired
    private DocumentCommentDao documentCommentDao;

    @Override
    public void addDocumentComment(DocumentCommentEntity documentCommentEntity) {
        if(documentCommentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("documentCommentEntity", "invalid documentCommentEntity is null");
            LOG.error(reason.get("documentCommentEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);
        documentCommentEntity.setC_date(ltm);

        documentCommentDao.add(documentCommentEntity);
        LOG.info("add document comment. documentCommentEntity [" + documentCommentEntity + "]");
    }

    @Transactional(readOnly = true)
    @Override
    public long countDocumentComment(int appSrl, long boardSrl, long categorySrl, long documentSrl) {
        return documentCommentDao.count(appSrl, boardSrl, categorySrl, documentSrl);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DocumentCommentEntity> getDocumentComment(int appSrl, long boardSrl, long categorySrl, long documentSrl,
                                                          Map<String, String> sort, int offset, int limit) {
        return documentCommentDao.get(appSrl, boardSrl, categorySrl, documentSrl, sort, offset, limit);
    }

    @Override
    public void deleteComments(long commentSrl, List<Long> commentSrls) {
        documentCommentDao.delete(commentSrl, commentSrls);
    }
}
