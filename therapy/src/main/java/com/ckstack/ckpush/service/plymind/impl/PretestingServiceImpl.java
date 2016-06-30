package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.dao.plymind.PretestingDao;
import com.ckstack.ckpush.domain.plymind.PretestingEntity;
import com.ckstack.ckpush.service.plymind.PretestingService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class PretestingServiceImpl implements PretestingService {
    private final static Logger LOG = LoggerFactory.getLogger(PretestingServiceImpl.class);

    @Autowired
    private PretestingDao pretestingDao;

    /**
     * 사전검사 항목을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PretestingEntity> getPretestingList() {
        List<PretestingEntity> pretestingEntities = pretestingDao.get();

        return pretestingEntities;
    }

    /**
     * 사전검사 항목을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PretestingEntity> getQuestionList() {
        List<PretestingEntity> pretestingEntities = pretestingDao.getQuestion();

        return pretestingEntities;
    }

    /**
     * 사전검사 항목을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PretestingEntity> getKindList() {
        List<PretestingEntity> pretestingEntities = pretestingDao.getKind();

        return pretestingEntities;
    }

    /**
     * 사전검사 항목을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PretestingEntity> getItemList() {
        List<PretestingEntity> pretestingEntities = pretestingDao.getItem();

        return pretestingEntities;
    }

    /**
     * 사전검사를 등록한다.
     */
    @Override
    public int add(long member_srl, Map<String, Object> requestBody) {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        try {

            Date date = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, -6);

            int c_date = (int)(cal.getTimeInMillis()/1000L);

            List<PretestingEntity> pretestingEntities = pretestingDao.info(member_srl, c_date);

            if(pretestingEntities.size() > 0) {
                for(PretestingEntity pretestingEntity : pretestingEntities) {
                    return pretestingEntity.getC_date();
                }
            }


            String checkboxValues = requestBody.get("checkboxValues").toString();
            String etcContent = requestBody.get("etcContent").toString();
            String textareaContent = requestBody.get("textareaContent").toString();

            String[] arrayCheckboxValues = checkboxValues.split("@");

            int checkboxCount = arrayCheckboxValues.length;

            for (int i = 0; i < checkboxCount; i++) {
                String[] arrayCheckboxValue = arrayCheckboxValues[i].split("-");

                PretestingEntity pretestingEntity = new PretestingEntity();
                pretestingEntity.setMember_srl(member_srl);
                pretestingEntity.setQuestion_srl(Integer.parseInt(arrayCheckboxValue[0]));
                pretestingEntity.setKind_srl(Integer.parseInt(arrayCheckboxValue[1]));
                pretestingEntity.setItem_srl(Integer.parseInt(arrayCheckboxValue[2]));
                pretestingEntity.setContents("");

                if (Integer.parseInt(arrayCheckboxValue[2]) == 41) {
                    pretestingEntity.setContents(etcContent);
                }

                if (Integer.parseInt(arrayCheckboxValue[2]) == 67) {
                    pretestingEntity.setContents(textareaContent);
                }

                pretestingEntity.setC_date(ltm);

                int result = pretestingDao.add(pretestingEntity);
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return ltm;
    }

    /**
     * 사전검사 정보를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<PretestingEntity> getPretestingInfo(long member_srl) {
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -6);

        int c_date = (int)(cal.getTimeInMillis()/1000L);

        List<PretestingEntity> pretestingEntities = pretestingDao.info(member_srl, c_date);

        return pretestingEntities;
    }

    /**
     * 사전검사 정보를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getPretestingResult(long member_srl) {
        boolean isEmotion = false;
        boolean isPrivate = false;
        boolean isHousehold = false;
        boolean isPersonal = false;
        boolean isWork = false;
        boolean isDependence = false;
        boolean isEtc = false;
        int recommend_checkup_code = 100;
        int recommend_advisor_code = 100;
        int recommend_advice_code = 100;

        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -6);

        int c_date = (int)(cal.getTimeInMillis()/1000L);

        List<PretestingEntity> pretestingEntities = pretestingDao.info(member_srl, c_date);

        for(PretestingEntity pretestingEntity : pretestingEntities) {
            if(pretestingEntity.getKind_srl() == 1) {  isEmotion = true;       }
            if(pretestingEntity.getKind_srl() == 2) {  isPrivate = true;       }
            if(pretestingEntity.getKind_srl() == 3) {  isHousehold = true;     }
            if(pretestingEntity.getKind_srl() == 4) {  isPersonal = true;      }
            if(pretestingEntity.getKind_srl() == 5) {  isWork = true;          }
            if(pretestingEntity.getKind_srl() == 6) {  isDependence = true;    }
            if(pretestingEntity.getKind_srl() == 7) {  isEtc = true;           }
        }

        if(isEmotion && isPrivate && isHousehold) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 500;
        } else if(isEmotion && isPrivate && isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 500;
        } else if(isEmotion && isPrivate && isWork) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isEmotion && isPrivate && isDependence) {
            recommend_checkup_code = 300;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isEmotion && isPrivate && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isEmotion && isHousehold && isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 500;
        } else if(isEmotion && isHousehold && isWork) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isEmotion && isHousehold && isDependence) {
            recommend_checkup_code = 300;
            recommend_advisor_code = 300;
            recommend_advice_code = 500;
        } else if(isEmotion && isHousehold && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 500;
        } else if(isEmotion && isPersonal && isWork) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isEmotion && isPersonal && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isEmotion && isPersonal && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 300;
        } else if(isEmotion && isWork && isDependence) {
            recommend_checkup_code = 300;
            recommend_advisor_code = 600;
            recommend_advice_code = 300;
        } else if(isEmotion && isWork && isEtc) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isEmotion && isDependence && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 300;
        } else if(isPrivate && isHousehold && isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 500;
        } else if(isPrivate && isHousehold && isWork) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 400;
            recommend_advice_code = 500;
        } else if(isPrivate && isHousehold && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 500;
        } else if(isPrivate && isHousehold && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 500;
        } else if(isPrivate && isPersonal && isWork) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 400;
            recommend_advice_code = 300;
        } else if(isPrivate && isPersonal && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 100;
        } else if(isPrivate && isPersonal && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 100;
        } else if(isPrivate && isWork && isDependence) {
            recommend_checkup_code = 300;
            recommend_advisor_code = 400;
            recommend_advice_code = 300;
        } else if(isPrivate && isWork && isEtc) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 400;
            recommend_advice_code = 300;
        } else if(isPrivate && isDependence && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 500;
        } else if(isHousehold && isPersonal && isWork) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isHousehold && isPersonal && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isHousehold && isPersonal && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isHousehold && isWork && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isHousehold && isWork && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isHousehold && isDependence && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isPersonal && isWork && isDependence) {
            recommend_checkup_code = 300;
            recommend_advisor_code = 600;
            recommend_advice_code = 300;
        } else if(isPersonal && isWork && isEtc) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 400;
            recommend_advice_code = 300;
        } else if(isPersonal && isDependence && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 500;
        } else if(isWork && isDependence && isEtc) {
            recommend_checkup_code = 300;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isEmotion && isPrivate) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isEmotion && isHousehold) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 400;
        } else if(isEmotion && isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 200;
            recommend_advice_code = 200;
        } else if(isEmotion && isWork) {
            recommend_checkup_code = 400;
            recommend_advisor_code = 500;
            recommend_advice_code = 300;
        } else if(isEmotion && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isEmotion && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isPrivate && isHousehold) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 600;
        } else if(isPrivate && isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isPrivate && isWork) {
            recommend_checkup_code = 500;
            recommend_advisor_code = 400;
            recommend_advice_code = 300;
        } else if(isPrivate && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 200;
        } else if(isPrivate && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isHousehold && isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 400;
        } else if(isHousehold && isWork) {
            recommend_checkup_code = 400;
            recommend_advisor_code = 100;
            recommend_advice_code = 500;
        } else if(isHousehold && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 300;
        } else if(isHousehold && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 600;
            recommend_advice_code = 300;
        } else if(isPersonal && isWork) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isPersonal && isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isPersonal && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isWork && isDependence) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 300;
            recommend_advice_code = 300;
        } else if(isWork && isEtc) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isDependence && isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 300;
        } else if(isEmotion) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isPrivate) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isHousehold) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 400;
        } else if(isPersonal) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isWork) {
            recommend_checkup_code = 200;
            recommend_advisor_code = 200;
            recommend_advice_code = 100;
        } else if(isDependence) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        } else if(isEtc) {
            recommend_checkup_code = 100;
            recommend_advisor_code = 100;
            recommend_advice_code = 100;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isEmotion", isEmotion);
        map.put("isPrivate", isPrivate);
        map.put("isHousehold", isHousehold);
        map.put("isPersonal", isPersonal);
        map.put("isWork", isWork);
        map.put("isDependence", isDependence);
        map.put("isEtc", isEtc);
        map.put("recommendCheckupCode", recommend_checkup_code);
        map.put("recommendAdvisorCode", recommend_advisor_code);
        map.put("recommendAdviceCode", recommend_advice_code);

        return map;
    }
}
