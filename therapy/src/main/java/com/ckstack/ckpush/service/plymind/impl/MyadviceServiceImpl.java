package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
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
public class MyadviceServiceImpl implements MyadviceService {
    private final static Logger LOG = LoggerFactory.getLogger(MyadviceServiceImpl.class);

    @Autowired
    protected Properties confAdmin;

    @Autowired
    private CkFileService ckFileService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private PlymindMessageService plymindMessageService;

    @Autowired
    private MyadviceDao myadviceDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private DocumentDao documentDao;

    @Override
    public int addDiary(DiaryEntity diaryEntity) {
        int resultCode = 0;

        try {
            diaryEntity.setMind_diary(URLDecoder.decode(diaryEntity.getMind_diary(), "utf-8"));

            resultCode = myadviceDao.addDiary(diaryEntity);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return resultCode;
    }

    @Override
    public boolean addPush(PushEntity pushEntity) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = dateFormat.parse(pushEntity.getNoti_time2());

            int noti_time1 = (int)(date.getTime()/1000L);

            pushEntity.setPush_text(URLDecoder.decode(pushEntity.getPush_text(), "utf-8"));
            pushEntity.setNoti_time1(noti_time1);

            MemberEntity memberEntity = memberService.getMemberInfo(pushEntity.getMember_srl());

            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setMember_srl(pushEntity.getMember_srl());
            notificationEntity.setApplication_srl(pushEntity.getApplication_srl());
            notificationEntity.setApplication_group(pushEntity.getApplication_group());
            notificationEntity.setNoti_type(4);  //1=너나들이, 2=공지사항, 3=그룹테라피, 4=개인테라피
            notificationEntity.setPush_text(pushEntity.getPush_text());
            notificationEntity.setBook_time(noti_time1);
            notificationEntity.setPush_status(1); //1=발송전, 2=발송
            notificationEntity.setUser_id(memberEntity.getUser_id());

            if(pushEntity.getNoti_srl() > 0) {
                plymindMessageService.updatePushText(pushEntity.getNoti_srl(), pushEntity.getPush_text());
            } else {
                plymindMessageService.add(notificationEntity);
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean addMessage(PushEntity pushEntity) {
        try {
            List<ApplicationEntity> applicationEntities = applicationService.getApplicationListByGroup(MDV.NUSE, pushEntity.getApplication_group(), null, MDV.NUSE, MDV.NUSE);

            int application_number = 0;
            for(ApplicationEntity applictionEntity: applicationEntities) {
                if(applictionEntity.getApplication_srl() == pushEntity.getApplication_srl()) {
                    application_number = applictionEntity.getApplication_number();
                    break;
                }
            }

            if(pushEntity.getMember_srl() > 0) {
                for(ApplicationEntity applictionEntity: applicationEntities) {
                    if(applictionEntity.getMember_srl() == pushEntity.getMember_srl() && applictionEntity.getApplication_number() == application_number) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
                        Date date = dateFormat.parse(pushEntity.getNoti_time2());

                        int noti_time1 = (int)(date.getTime()/1000L);

                        pushEntity.setPush_text(URLDecoder.decode(pushEntity.getPush_text(), "utf-8"));
                        pushEntity.setNoti_time1(noti_time1);

                        MemberEntity memberEntity = memberService.getMemberInfo(pushEntity.getMember_srl());

                        NotificationEntity notificationEntity = new NotificationEntity();
                        notificationEntity.setMember_srl(pushEntity.getMember_srl());
                        notificationEntity.setApplication_srl(applictionEntity.getApplication_srl());
                        notificationEntity.setApplication_group(pushEntity.getApplication_group());
                        notificationEntity.setNoti_type(3); //1=너나들이, 2=공지사항, 3=그룹테라피, 4=개인테라피
                        notificationEntity.setPush_text(pushEntity.getPush_text());
                        notificationEntity.setBook_time(noti_time1);
                        notificationEntity.setPush_status(1); //1=발송전, 2=발송
                        notificationEntity.setUser_id(memberEntity.getUser_id());

                        plymindMessageService.add(notificationEntity);

                        break;
                    }
                }

            } else {
                List<ApplicationEntity> applicationMemberEntities = applicationService.getApplicationMember(pushEntity.getApplication_group());

                for(ApplicationEntity applictionMemberEntity: applicationMemberEntities) {
                    for (ApplicationEntity applictionEntity : applicationEntities) {
                        if (applictionEntity.getMember_srl() == applictionMemberEntity.getMember_srl() && applictionEntity.getApplication_number() == application_number) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
                            Date date = dateFormat.parse(pushEntity.getNoti_time2());

                            int noti_time1 = (int)(date.getTime()/1000L);

                            pushEntity.setPush_text(URLDecoder.decode(pushEntity.getPush_text(), "utf-8"));
                            pushEntity.setNoti_time1(noti_time1);

                            MemberEntity memberEntity = memberService.getMemberInfo(applictionMemberEntity.getMember_srl());

                            NotificationEntity notificationEntity = new NotificationEntity();
                            notificationEntity.setMember_srl(applictionMemberEntity.getMember_srl());
                            notificationEntity.setApplication_srl(applictionEntity.getApplication_srl());
                            notificationEntity.setApplication_group(pushEntity.getApplication_group());
                            notificationEntity.setNoti_type(3); //1=너나들이, 2=공지사항, 3=그룹테라피, 4=개인테라피
                            notificationEntity.setPush_text(pushEntity.getPush_text());
                            notificationEntity.setBook_time(noti_time1);
                            notificationEntity.setPush_status(1); //1=발송전, 2=발송
                            notificationEntity.setUser_id(memberEntity.getUser_id());

                            plymindMessageService.add(notificationEntity);

                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public int addPlymindDocument(PlymindDocumentEntity plymindDocumentEntity) {
        int resultCode = 0;

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        if(plymindDocumentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("plymindDocumentEntity", "invalid plymindDocumentEntity is null");
            LOG.error(reason.get("plymindDocumentEntity"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        try {
            List<ApplicationEntity> applicationEntities = applicationService.getApplicationListByGroup(MDV.NUSE, plymindDocumentEntity.getApplication_group(), null, MDV.NUSE, MDV.NUSE);

            int application_number = 0;
            for(ApplicationEntity applictionEntity: applicationEntities) {
                if(applictionEntity.getApplication_srl() == plymindDocumentEntity.getApplication_srl()) {
                    application_number = applictionEntity.getApplication_number();
                    break;
                }
            }


            if(plymindDocumentEntity.getMember_srl() > 0) {

                LOG.debug("=================== 1 : "  + plymindDocumentEntity.toString());
                for(ApplicationEntity applictionEntity: applicationEntities) {
                    if(applictionEntity.getMember_srl() == plymindDocumentEntity.getMember_srl()
                            && applictionEntity.getApplication_number() == application_number
                                && applictionEntity.getApplication_srl() == plymindDocumentEntity.getApplication_srl()) {

                        plymindDocumentEntity.setApplication_srl(applictionEntity.getApplication_srl());

                        if(plymindDocumentEntity.getCheckup_file_srl() <= 0) {
                            plymindDocumentEntity.setCheckup_file_srl(MDV.NONE);
                            plymindDocumentEntity.setCheckup_date(ltm);
                        }
                        if(plymindDocumentEntity.getReply_file_srl() <= 0) {
                            plymindDocumentEntity.setReply_file_srl(MDV.NONE);
                            plymindDocumentEntity.setReply_date(ltm);
                        }
                        if(plymindDocumentEntity.getResult_file_srl() <= 0) {
                            plymindDocumentEntity.setResult_file_srl(MDV.NONE);
                            plymindDocumentEntity.setResult_date(ltm);
                        }

                        if(plymindDocumentEntity.getAdvisor_comment() == null) plymindDocumentEntity.setAdvisor_comment("");
                        plymindDocumentEntity.setAdvisor_comment(URLDecoder.decode(plymindDocumentEntity.getAdvisor_comment(), "utf-8"));


                        resultCode = myadviceDao.addPlymindDocument(plymindDocumentEntity);

                        break;
                    }
                }

            } else {

                LOG.debug("=================== 2 : "  + plymindDocumentEntity.toString());

                List<ApplicationEntity> applicationMemberEntities = applicationService.getApplicationMember(plymindDocumentEntity.getApplication_group());

                for(ApplicationEntity applictionMemberEntity: applicationMemberEntities) {
                    for (ApplicationEntity applictionEntity : applicationEntities) {
                        if (applictionEntity.getMember_srl() == applictionMemberEntity.getMember_srl()
                                && applictionEntity.getApplication_number() == application_number
                                && applictionEntity.getApplication_srl() == plymindDocumentEntity.getApplication_srl()) {

                            plymindDocumentEntity.setApplication_srl(applictionEntity.getApplication_srl());

                            if(plymindDocumentEntity.getCheckup_file_srl() <= 0) {
                                plymindDocumentEntity.setCheckup_file_srl(MDV.NONE);
                                plymindDocumentEntity.setCheckup_date(ltm);
                            }
                            if(plymindDocumentEntity.getReply_file_srl() <= 0) {
                                plymindDocumentEntity.setReply_file_srl(MDV.NONE);
                                plymindDocumentEntity.setReply_date(ltm);
                            }
                            if(plymindDocumentEntity.getResult_file_srl() <= 0) {
                                plymindDocumentEntity.setResult_file_srl(MDV.NONE);
                                plymindDocumentEntity.setResult_date(ltm);
                            }

                            if(plymindDocumentEntity.getAdvisor_comment() == null) plymindDocumentEntity.setAdvisor_comment("");
                            plymindDocumentEntity.setAdvisor_comment(URLDecoder.decode(plymindDocumentEntity.getAdvisor_comment(), "utf-8"));


                            resultCode = myadviceDao.addPlymindDocument(plymindDocumentEntity);

                            break;
                        }
                    }
                }
            }

            LOG.debug("=================== 3 : "  + plymindDocumentEntity.toString());

            if(plymindDocumentEntity.getApplication_srl() == 0) resultCode = myadviceDao.addPlymindDocument(plymindDocumentEntity);

        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return resultCode;
    }

    @Override
    public void modifyPlymindDocument(PlymindDocumentEntity plymindDocumentEntity, long documentSrl) {

        if(plymindDocumentEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("plymindDocumentEntity", "invalid plymindDocumentEntity. plymindDocumentEntity is null");
            LOG.error(reason.get("plymindDocumentEntity"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        PlymindDocumentEntity savedEntity = myadviceDao.getPlymindDocumentD(documentSrl);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document", "no document for modify. document_srl [" + documentSrl + "]");
            LOG.error(reason.get("document"));
            throw new CustomException("modify_document_error", reason);
        }


        int ltm = (int)(DateTime.now().getMillis() / 1000);


        boolean willModify = false;
        PlymindDocumentEntity updateEntity = new PlymindDocumentEntity();
        updateEntity.init();

        if(plymindDocumentEntity.getAdvisor_comment() != null &&
                !StringUtils.equals(plymindDocumentEntity.getAdvisor_comment(), savedEntity.getAdvisor_comment())) {
            updateEntity.setAdvisor_comment(plymindDocumentEntity.getAdvisor_comment());
            willModify = true;
        }

        if(plymindDocumentEntity.getCheckup_file_srl() > 0 && plymindDocumentEntity.getCheckup_file_srl() != savedEntity.getCheckup_file_srl()) {
            updateEntity.setCheckup_file_srl(plymindDocumentEntity.getCheckup_file_srl());
            willModify = true;
        }

        if(plymindDocumentEntity.getReply_file_srl() > 0 && plymindDocumentEntity.getReply_file_srl() != savedEntity.getReply_file_srl()) {
            updateEntity.setReply_file_srl(plymindDocumentEntity.getReply_file_srl());
            updateEntity.setReply_date(ltm);
            willModify = true;
        }

        /*if(plymindDocumentEntity.getResult_file_srl() > 0 && plymindDocumentEntity.getResult_file_srl() != savedEntity.getResult_file_srl()) {
            updateEntity.setResult_file_srl(plymindDocumentEntity.getResult_file_srl());
            willModify = true;
        }*/

        if(plymindDocumentEntity.getResult_file_srl() > 0 && plymindDocumentEntity.getResult_file_srl() != savedEntity.getResult_file_srl()) {
            updateEntity.setResult_file_srl(plymindDocumentEntity.getResult_file_srl());
            updateEntity.setResult_date(ltm);
            willModify = true;

            if(savedEntity.getResult_file_srl() > 0){
                FileRepositoryEntity fileRepositoryEntity = ckFileService.getFile(savedEntity.getResult_file_srl());

                fileRepositoryEntity.setDeleted(MDV.YES);
                /*기존 파일(tbl_file_repository)은 delete 상태로 변경*/
                ckFileService.modifyFile(fileRepositoryEntity);
            }
        }

        if(!willModify) {
            LOG.info("ignore modify plymind document. document_srl [" + documentSrl +
                    "]. it is same between saved value and changed value");
        } else {
            //updateEntity.setU_date(ltm);
            myadviceDao.modifyPlymindDocument(updateEntity, documentSrl);

            LOG.info("modify document. updateEntity [" + updateEntity.toString() + "]");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public AddressEntity getAddressInfo(int application_group) {
        AddressEntity addressEntity = myadviceDao.getAddressInfo(application_group);

        return addressEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DiaryEntity> getDiary(long member_srl, long application_srl, int application_group) {
        List<DiaryEntity> diaryEntities = myadviceDao.getDiary(member_srl, application_srl, application_group);

        return diaryEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public long countPush(long application_srl,
                          int application_group,
                          List<Integer> application_statues) {
        long pushCount = myadviceDao.countPush(application_srl, application_group, application_statues);

        return pushCount;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PushEntity> getPushList(long application_srl,
                                        int application_group,
                                        List<Integer> application_statues,
                                        Map<String, String> sortValue,
                                        int offset,
                                        int limit) {
        List<PushEntity> pushEntities = myadviceDao.getPushList(application_srl, application_group, application_statues, null, offset, limit);

        return pushEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public long countAddPush(long application_srl,
                          int application_group,
                          List<Integer> application_statues) {
        long pushCount = myadviceDao.countAddPush(application_srl, application_group, application_statues);

        return pushCount;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PushEntity> getAddPushList(long application_srl,
                                        int application_group,
                                        List<Integer> application_statues,
                                        Map<String, String> sortValue,
                                        int offset,
                                        int limit) {
        List<PushEntity> pushEntities = myadviceDao.getAddPushList(application_srl, application_group, application_statues, null, offset, limit);

        return pushEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public PushEntity getPushInfo(long application_srl) {
        PushEntity pushEntity = myadviceDao.getPushInfo(application_srl);

        return pushEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PushEntity> getMessageList(long application_srl,
                                           int application_group,
                                           int push_status) {
        List<PushEntity> pushEntities = myadviceDao.getMessageList(application_srl, application_group, push_status);

        return pushEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PlymindDocumentEntity> getPlymindDocument(long application_srl, int application_group) {
        List<PlymindDocumentEntity> plymindDocumentEntities = myadviceDao.getPlymindDocument(application_srl, application_group);

        return plymindDocumentEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getPresent(long member_srl) {
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

        String strCategorySrl = confAdmin.getProperty("plymind.one.category.srl");
        int categorySrl = Integer.valueOf(strCategorySrl);

        long advice_cnt = applicationDao.countApplicationGroup(member_srl, MDV.NUSE, progress_application_statues, advice_contents_srls, null, null, null);

        long checkup_cnt = applicationDao.countApplicationGroup(member_srl, MDV.NUSE, progress_application_statues, checkup_contents_srls, null, null, null);

        long complete_cnt =  applicationDao.countApplicationGroupComplete(member_srl, MDV.NUSE, complete_application_statues, null, null, null, null);

        long one_cnt =  documentDao.count(MDV.NUSE, MDV.NUSE, categorySrl, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, member_srl, null, null, null);

        Map<String, Object> present = new HashMap<String, Object>();
        present.put("advice_cnt", advice_cnt);
        present.put("checkup_cnt", checkup_cnt);
        present.put("complete_cnt", complete_cnt);
        present.put("one_cnt", one_cnt);

        return present;
    }

    @Transactional(readOnly = true)
    @Override
    public PlymindDocumentEntity getPlymindDocumentD(long document_srl) {

        if(document_srl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("document_srl", "invalid document_srl. document_srl is less then zero");
            LOG.error(reason.get("document_srl"));
            throw new CustomException("invalid_method_parameter", reason);
        }

        PlymindDocumentEntity plymindDocumentEntity = myadviceDao.getPlymindDocumentD(document_srl);

        return plymindDocumentEntity;
    }
}