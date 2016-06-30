package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.board.DocumentCommentDao;
import com.ckstack.ckpush.dao.board.DocumentDao;
import com.ckstack.ckpush.dao.plymind.ApplicationDao;
import com.ckstack.ckpush.dao.plymind.MyadviceDao;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
import com.ckstack.ckpush.domain.plymind.*;
import com.ckstack.ckpush.domain.push.NotificationEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.mineral.CkFileService;
import com.ckstack.ckpush.service.plymind.ApplicationService;
import com.ckstack.ckpush.service.plymind.MyadviceService;
import com.ckstack.ckpush.service.plymind.PresentService;
import com.ckstack.ckpush.service.push.PlymindMessageService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
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
public class PresentServiceImpl implements PresentService {
    private final static Logger LOG = LoggerFactory.getLogger(PresentServiceImpl.class);

    @Autowired
    protected Properties confAdmin;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private DocumentCommentDao documentCommentDao;

    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getAdvisorPresent(long advisor_srl) {
        List<Integer> progress_application_statues = new ArrayList<Integer>();
        progress_application_statues.add(1);
        progress_application_statues.add(2);

        List<Integer> complete_application_statues = new ArrayList<Integer>();
        complete_application_statues.add(3);

        List<Long> advice_contents_srls = new ArrayList<Long>();
        advice_contents_srls.add(Long.parseLong("1"));
        advice_contents_srls.add(Long.parseLong("2"));
        advice_contents_srls.add(Long.parseLong("3"));
        advice_contents_srls.add(Long.parseLong("4"));
        advice_contents_srls.add(Long.parseLong("5"));

        List<Long> checkup_contents_srls = new ArrayList<Long>();
        checkup_contents_srls.add(Long.parseLong("6"));
        checkup_contents_srls.add(Long.parseLong("7"));
        checkup_contents_srls.add(Long.parseLong("8"));
        checkup_contents_srls.add(Long.parseLong("9"));

        long advice_cnt = applicationDao.countApplicationGroup(MDV.NUSE, advisor_srl, progress_application_statues, advice_contents_srls, null, null, null);

        long checkup_cnt = applicationDao.countApplicationGroup(MDV.NUSE, advisor_srl, progress_application_statues, checkup_contents_srls, null, null, null);

        long complete_cnt =  applicationDao.countApplicationGroupComplete(MDV.NUSE, advisor_srl, complete_application_statues, null, null, null, null);

        long one_cnt =  documentCommentDao.countAdvisorPresent(MDV.NUSE, MDV.NUSE, Long.valueOf(confAdmin.getProperty("plymind.one.category.srl")), advisor_srl);

        Map<String, Object> present = new HashMap<String, Object>();
        present.put("advice_cnt", advice_cnt);
        present.put("checkup_cnt", checkup_cnt);
        present.put("complete_cnt", complete_cnt);
        present.put("one_cnt", one_cnt);

        return present;
    }
}