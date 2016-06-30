package com.ckstack.ckpush.service.accessory.impl;

import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.accessory.MyDatabaseUtilService;
import com.ckstack.ckpush.service.accessory.ServiceHistoryService;
import com.ckstack.ckpush.dao.accessory.ServiceHistoryDao;
import com.ckstack.ckpush.domain.accessory.ServiceHistoryEntity;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by dhkim94 on 15. 3. 18..
 * 이력은 메시지 큐로 다른 곳으로 보내서 다른데서 쌓을 예정임.
 * 이력 추가 한다고 서비스가 느려지는 건 말이 안되지...
 */
@Service
@Transactional(value = "transactionManager")
public class ServiceHistoryServiceImpl implements ServiceHistoryService {
    private final static Logger LOG = LoggerFactory.getLogger(ServiceHistoryServiceImpl.class);

    @Autowired
    private MyDatabaseUtilService myDatabaseUtilService;
    @Autowired
    private ServiceHistoryDao serviceHistoryDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    protected Properties confAccessory;

    private final DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");
    private final DateTimeFormatter fmtyyyyMMddHHmmss = DateTimeFormat.forPattern("yyyy.MM.dd HH:mm:ss");

    /**
     * 서비스 이력 카운트를 타입별로 구한다.
     * @param startMonth 카운트를 구할 기준 월. yyyyMM 포맷.
     * @param periodMonth 카운트를 구할 기준 월로 부터의 기간. 월의 카운트임. 1달 을 구하려면 1, 2달이면 2 ...
     * @param historyType 카운트를 구할 이력 타입
     * @param memberSrl 카운트를 구할 유저 시리얼 넘버
     * @param maxHistorySrl 리스트 제한을 위한 max history_srl
     * @return 이력 카운트를 리턴한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long count(String startMonth, int periodMonth, int historyType, long memberSrl,
                      long maxHistorySrl) {
        DateTime dtStartMonth;
        int iStartMonth, iEndMonth;
        try {
            dtStartMonth = fmtYYYYMM.parseDateTime(startMonth);
            iStartMonth = (int)(dtStartMonth.getMillis() / 1000);
        } catch(Exception e) {
            LOG.error("failed read service history count. invalid startMonth["+startMonth+"]");
            Map<String, String> reason = new HashMap<>();
            reason.put("start_month", "invalid start month of getting history count. start_month ["+startMonth+"]");
            throw new CustomException("count_service_history_error", reason);
        }

        if(periodMonth <= 0) {
            LOG.warn("periodMonth is less then zero. set default value 1");
            periodMonth = 1;
        }
        iEndMonth = (int)(dtStartMonth.plusMonths(periodMonth).getMillis() / 1000);

        return serviceHistoryDao.count(iStartMonth, iEndMonth, historyType, memberSrl, maxHistorySrl);
    }

    /**
     * 계정 추가 서비스 history 메시지
     * @param tryUserId 계정 추가를 시도한 유저 아이디
     * @param resultUserId 추가된 계정의 유저 아이디
     * @return 계정 추가 메시지
     */
    @Transactional(readOnly = true)
    private String getCreateAccountContent(String tryUserId, String resultUserId) {
        if(!confAccessory.containsKey("msg_add_history_account")) return "";
        String content = confAccessory.getProperty("msg_add_history_account");

        if(tryUserId == null || StringUtils.trim(tryUserId).length() <= 0)
            tryUserId = "unknwon user";
        if(resultUserId == null || StringUtils.trim(resultUserId).length() <= 0)
            resultUserId = "unknwon user";

        String now = DateTime.now().toString(fmtyyyyMMddHHmmss);
        return String.format(content, tryUserId, resultUserId, now);
    }

    /**
     * 계정 추가 서비스 url 메시지
     * @param tryMemberSrl 계정 추가를 시도한 유저 시리얼 넘버
     * @param resultMemberSrl 추가된 계정의 유저 시리얼 넘버
     * @return 계정 추가 history용 URL
     */
    @Transactional(readOnly = true)
    private String getCreateAccountURL(long tryMemberSrl, long resultMemberSrl) {
        if(!confAccessory.containsKey("url_add_history_account")) return "";
        String content = confAccessory.getProperty("url_add_history_account");

        if(tryMemberSrl <= 0)       tryMemberSrl = MDV.NONE;
        if(resultMemberSrl <= 0)    resultMemberSrl = MDV.NONE;

        return String.format(content, tryMemberSrl, resultMemberSrl);
    }

    /**
     * 계정 추가 이력을 남긴다.
     * @param tryMemberSrl 계정 추가를 시도하는 유저 시리얼 넘버. 값이 없으면(0 이하) resultMemberSrl 을 그대로 사용한다.
     * @param resultMemberSrl 추가된 계정의 유저 시리얼 넘버
     * @param tryUserId 계정 추가를 시도하는 유저 아이디. 값이 없으면(null or empty string) tryMemberSrl 로 추출 한다.
     * @param resultUserId 추가된 계정의 유저 아이디. 값이 없으면(null or empty string) resultMemberSrl 로 추출 한다.
     * @param ip 계정 등록을 시도한 ip address
     */
    @Override
    public void addCreateAccountHistory(long tryMemberSrl, long resultMemberSrl,
                                        String tryUserId, String resultUserId, String ip) {
        /*
        if(!confAccessory.containsKey("code_history_account")) {
            LOG.error("failed add service history. not found code_history_account configuration");
            Map<String, String> reason = new HashMap<>();
            reason.put("code_history_account", "server error. not found code_history_account configuration");
            throw new CustomException("add_service_history_error", 500, reason);
        }
        if(!confAccessory.containsKey("sequence_history")) {
            LOG.error("failed add service history. not found sequence_history configuration");
            Map<String, String> reason = new HashMap<>();
            reason.put("sequence_history", "server error. not found sequence_history configuration");
            throw new CustomException("add_service_history_error", 500, reason);
        }
        if(resultMemberSrl <= 0) {
            LOG.error("failed add service history. no result member_srl[" + resultMemberSrl + "]");
            Map<String, String> reason = new HashMap<>();
            reason.put("member_srl", "not found member_srl of creating user. member_srl ["+resultMemberSrl+"]");
            throw new CustomException("add_service_history_error", reason);
        }

        if(resultUserId == null || StringUtils.trim(resultUserId).equals("")) {
            MemberEntity resultMemberEntity = memberDao.get(resultMemberSrl, null);
            if(resultMemberEntity == null) {
                LOG.error("failed add service history. no result member. member_srl[" + resultMemberSrl + "]");
                Map<String, String> reason = new HashMap<>();
                reason.put("memberEntity", "not found creating user information in database");
                throw new CustomException("add_service_history_error", reason);
            }
            resultUserId = resultMemberEntity.getUser_id();
        }

        if(tryMemberSrl <= 0) {
            tryMemberSrl = resultMemberSrl;
            tryUserId = resultUserId;
        } else {
            if(tryUserId == null || StringUtils.trim(tryUserId).equals("")) {
                MemberEntity tryMemberEntity = memberDao.get(tryMemberSrl, null);
                if(tryMemberEntity == null) {
                    LOG.error("failed add service history. no try member. member_srl[" + tryMemberSrl + "]");
                    Map<String, String> reason = new HashMap<>();
                    reason.put("member_srl", "no member_srl of history adding user. member_srl ["+tryMemberSrl+"]");
                    throw new CustomException("add_service_history_error", reason);
                }
                tryUserId = tryMemberEntity.getUser_id();
            }
        }

        if(ip == null)   ip = "";
        else             ip = StringUtils.trim(ip);

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        ServiceHistoryEntity serviceHistoryEntity = new ServiceHistoryEntity();
        serviceHistoryEntity.setHistory_srl(myDatabaseUtilService.getNextSequence(
                Integer.parseInt(confAccessory.getProperty("sequence_history"), 10)));
        serviceHistoryEntity.setC_date(ltm);
        serviceHistoryEntity.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_account"), 10));
        serviceHistoryEntity.setMember_srl(tryMemberSrl);
        serviceHistoryEntity.setHistory_content(this.getCreateAccountContent(tryUserId, resultUserId));
        serviceHistoryEntity.setHistory_url(this.getCreateAccountURL(tryMemberSrl, resultMemberSrl));
        serviceHistoryEntity.setIpaddress(ip);

        serviceHistoryDao.add(serviceHistoryEntity);
        LOG.info("add service history. serviceHistoryEntity["+ serviceHistoryEntity +"]");

        return serviceHistoryEntity.getHistory_srl();
        */

        LOG.debug("create account history send to message queue");
    }

    /**
     * 앱 추가/삭제 서비스 history 메시지
     * @param tryUserId 앱을 추가/삭제 하는 유저 아이디
     * @param appName 추가/삭제 된 앱의 이름
     * @param isCreate 추가 또는 삭제 이력 구분. true: 추가 이력, false: 삭제 이력
     * @return 앱 추가/삭제 메시지
     */
    @Transactional(readOnly = true)
    private String getCreateOrDeleteAppContent(String tryUserId, String appName, boolean isCreate) {
        if(!confAccessory.containsKey("msg_add_app") ||
                !confAccessory.containsKey("msg_delete_app")) return "";
        String content = isCreate ? confAccessory.getProperty("msg_add_app") :
                confAccessory.getProperty("msg_delete_app");

        if(tryUserId == null || StringUtils.trim(tryUserId).length() <= 0)
            tryUserId = "unknwon user";
        if(appName == null || StringUtils.trim(appName).length() <= 0)
            appName = "unknwon";

        String now = DateTime.now().toString(fmtyyyyMMddHHmmss);
        return String.format(content, tryUserId, appName, now);
    }

    /**
     * 앱 추가/삭제 서비스 url 메시지
     * @param tryMemberSrl 앱을 추가/삭제 하는 유저 시리얼 넘버
     * @param appSrl 추가/삭제 된 앱의 시리얼 넘버
     * @param isCreate 추가 또는 삭제 이력 구분. true: 추가 이력, false: 삭제 이력
     * @return 앱 추가/삭제 url
     */
    @Transactional(readOnly = true)
    private String getCreateOrDeleteAppURL(long tryMemberSrl, long appSrl, boolean isCreate) {
        if(!confAccessory.containsKey("url_add_app") ||
                !confAccessory.containsKey("url_delete_app")) return "";
        String content = isCreate ? confAccessory.getProperty("url_add_app") :
                confAccessory.getProperty("url_delete_app");

        if(tryMemberSrl <= 0)   tryMemberSrl = MDV.NONE;
        if(appSrl <= 0)         appSrl = MDV.NONE;

        return String.format(content, tryMemberSrl, appSrl);
    }

    /**
     * 앱 추가/삭제 이력을 남긴다.
     * @param tryMemberSrl 앱을 추가/삭제한 유저 시리얼 넘버
     * @param tryUserId 앱을 추가/삭제한 유저 아이디
     * @param app 추가/삭제 하는 앱의 정보. {app_srl : app_name} 형태로 들어 있는 hashmap.
     *            삭제는 동시에 일어 날 수 있기 때문에 hashmap 을 사용함.
     * @param ip 앱 추가/삭제 시도한 ip address
     * @param isCreate 추가 또는 삭제 이력 구분. true: 추가 이력, false: 삭제 이력
     * @return 추가된 이력의 history_srl 을 리턴한다.
     */
    @Override
    public void addCreateOrDeleteAppHistory(long tryMemberSrl, String tryUserId, Map<Integer, String> app,
                                            String ip, boolean isCreate) {
        /*
        if(!confAccessory.containsKey("code_history_app")) {
            LOG.error("failed add service history. not found code_history_app configuration");
            Map<String, String> reason = new HashMap<>();
            reason.put("code_history_app", "server error. not found code_history_app configuration");
            throw new CustomException("add_service_history_error", 500, reason);
        }
        if(!confAccessory.containsKey("sequence_history")) {
            LOG.error("failed add service history. not found sequence_history configuration");
            Map<String, String> reason = new HashMap<>();
            reason.put("sequence_history", "server error. not found sequence_history configuration");
            throw new CustomException("add_service_history_error", 500, reason);
        }

        if(ip == null)   ip = "";
        else             ip = StringUtils.trim(ip);

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        ServiceHistoryEntity serviceHistoryEntity = new ServiceHistoryEntity();
        serviceHistoryEntity.setC_date(ltm);
        serviceHistoryEntity.setHistory_type(Integer.parseInt(confAccessory.getProperty("code_history_app"), 10));
        serviceHistoryEntity.setMember_srl(tryMemberSrl);
        serviceHistoryEntity.setIpaddress(ip);

        Set<Integer> keys = app.keySet();
        for(int key : keys) {
            serviceHistoryEntity.setHistory_srl(myDatabaseUtilService.getNextSequence(
                    Integer.parseInt(confAccessory.getProperty("sequence_history"), 10)));
            serviceHistoryEntity.setHistory_content(this.getCreateOrDeleteAppContent(tryUserId,
                    app.get(key), isCreate));
            serviceHistoryEntity.setHistory_url(this.getCreateOrDeleteAppURL(tryMemberSrl, key, isCreate));
            serviceHistoryDao.add(serviceHistoryEntity);
            LOG.info("add service history. serviceHistoryEntity["+ serviceHistoryEntity +"]");
        }

        return serviceHistoryEntity.getHistory_srl();
        */

        LOG.debug("create or delete app history send to message queue");
    }
}
