package com.ckstack.ckpush.service.plymind.impl;


import com.ckstack.ckpush.common.CustomException;
import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.plymind.ApplicationDao;
import com.ckstack.ckpush.dao.plymind.ProductDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.board.DocumentEntity;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.ProductEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.ApplicationService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class ApplicationServiceImpl implements ApplicationService {
    private final static Logger LOG = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ApplicationDao applicationDao;

    /**
     * 상담 준비중 처리를 한다.
     */
    @Override
    public int statusReady(long application_srl, int application_group) {
        int result = applicationDao.statusReady(application_srl, application_group);

        return result;
    }

    /**
     * 상담 진행중 처리를 한다.
     */
    @Override
    public int statusProgress(long application_srl, int application_group) {
        int result = applicationDao.statusProgress(application_srl, application_group);

        return result;
    }

    /**
     * 상담완료 처리를 한다.
     */
    @Override
    public int statusComplete(long application_srl, int application_group) {
        int result = applicationDao.statusComplete(application_srl, application_group);

        return result;
    }

    /**
     * 상담 진행중 처리를 한다.
     */
    @Override
    public int receiveCheck(int application_group) {
        int result = 0;

        int ltm = (int)(DateTime.now().getMillis() / 1000);


        List<ApplicationEntity> applicationEntities = applicationDao.getApplicationListByGroup(MDV.NUSE, application_group, null, MDV.NUSE, MDV.NUSE);

        String start_date = "";
        String end_date = "";
        int start_date_count = 0;

        for (ApplicationEntity applicationEntity : applicationEntities) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, (start_date_count * 7));

            start_date = dateFormat.format(new Date(cal.getTimeInMillis()));

            cal.add(Calendar.DATE, 6);

            end_date = dateFormat.format(new Date(cal.getTimeInMillis()));

            applicationEntity.setApplication_group(application_group);

            if(applicationEntity.getApplication_number() == 1) {
                applicationEntity.setApplication_status(2);
            } else {
                applicationEntity.setApplication_status(1);
            }
            applicationEntity.setStart_date(start_date);
            applicationEntity.setEnd_date(end_date);
            applicationEntity.setU_date(ltm);

            result = applicationDao.receiveCheck(applicationEntity);

            start_date_count++;
        }

        return result;
    }

    /**
     * 상담신청 내역 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countApplicationGroup(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls) {
        long applicationCount = applicationDao.countApplicationGroup(member_srl, advisor_srl, application_statues, contents_srls, null, null, null);

        return applicationCount;
    }

    /**
     * 상담 신청 내역 중 검색되어진 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countSearchApplicationGroup(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls, Map<String, String> searchFilter) {
        String title = null;
        String user_name = null;
        String advisor_name = null;

        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("title")) title = searchFilter.get("title");
        if(searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");

        List<Long> product_srls = new ArrayList<Long>();
        if(title != null && title != "") {
            List<ProductEntity> productEntities = productDao.getProductList(MDV.NUSE, title);

            for(ProductEntity productEntity : productEntities) {
                product_srls.add(productEntity.getProduct_srl());
            }

            if(productEntities.size() == 0) {
                product_srls.add((long)MDV.NUSE);
            }
        }

        List<Long> advisor_srls = new ArrayList<Long>();
        if(advisor_name != null && advisor_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, advisor_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        long applicationSearchCount = applicationDao.countApplicationGroup(member_srl, advisor_srl, application_statues, contents_srls, user_name, advisor_srls, product_srls);

        return applicationSearchCount;
    }

    /**
     * 상담신청 내역 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countApplicationGroupComplete(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls) {
        long applicationCount = applicationDao.countApplicationGroupComplete(member_srl, advisor_srl, application_statues, contents_srls, null, null, null);

        return applicationCount;
    }

    /**
     * 상담 신청 내역 중 검색되어진 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countSearchApplicationGroupComplete(long member_srl, long advisor_srl, List<Integer> application_statues, List<Long> contents_srls, Map<String, String> searchFilter) {
        String title = null;
        String user_name = null;
        String advisor_name = null;

        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("title")) title = searchFilter.get("title");
        if(searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");

        List<Long> product_srls = new ArrayList<Long>();
        if(title != null && title != "") {
            List<ProductEntity> productEntities = productDao.getProductList(MDV.NUSE, title);

            for(ProductEntity productEntity : productEntities) {
                product_srls.add(productEntity.getProduct_srl());
            }

            if(productEntities.size() == 0) {
                product_srls.add((long)MDV.NUSE);
            }
        }

        List<Long> advisor_srls = new ArrayList<Long>();
        if(advisor_name != null && advisor_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, advisor_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        long applicationSearchCount = applicationDao.countApplicationGroupComplete(member_srl, advisor_srl, application_statues, contents_srls, user_name, advisor_srls, product_srls);

        return applicationSearchCount;
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ApplicationEntity> getApplicationGroupList(long member_srl,
                                                           long advisor_srl,
                                                           List<Integer> application_statues,
                                                           List<Long> contents_srls,
                                                           Map<String, String> searchFilter,
                                                           Map<String, String> sortValue,
                                                           int offset,
                                                           int limit) {
        String title = null;
        String user_name = null;
        String advisor_name = null;
        String application_listType = "progressList";

        if(searchFilter != null) {
            if (searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
            if (searchFilter.containsKey("title")) title = searchFilter.get("title");
            if (searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");
            if (searchFilter.containsKey("application_listType")) application_listType = searchFilter.get("application_listType");
        }
        List<Long> product_srls = new ArrayList<Long>();
        if(title != null && title != "") {
            List<ProductEntity> productEntities = productDao.getProductList(MDV.NUSE, title);

            for(ProductEntity productEntity : productEntities) {
                product_srls.add(productEntity.getProduct_srl());
            }

            if(productEntities.size() == 0) {
                product_srls.add((long)MDV.NUSE);
            }
        }

        List<Long> advisor_srls = new ArrayList<Long>();
        if(advisor_name != null && advisor_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, advisor_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        Map<String, String> sortMap = new HashMap<String, String>();

        if(sortValue != null) {
            if (sortValue.containsKey("title")) {
                sortMap.put("product_srl", sortValue.get("title"));
            } else if (sortValue.containsKey("advisor_name")) {
                sortMap.put("advisor_srl", sortValue.get("advisor_name"));
            } else {
                sortMap = sortValue;
            }
        }
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        if(application_listType.equals("completeList")) {
            applicationEntities = applicationDao.getApplicationGroupCompleteList(member_srl, advisor_srl, application_statues, contents_srls,
                    user_name, advisor_srls, product_srls, sortMap, offset, limit);
        }else{
            applicationEntities = applicationDao.getApplicationGroupList(member_srl, advisor_srl, application_statues, contents_srls,
                    user_name, advisor_srls, product_srls, sortMap, offset, limit);

        }


        return applicationEntities;
    }

    @Override
    public ApplicationEntity getApplication(long application_srl) {

        if(application_srl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("application_srl", "invalid application_srl is less then zero");
            LOG.error(reason.get("application_srl"));
            throw new CustomException("read_application_error", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        ApplicationEntity applicationEntity = applicationDao.getApplication(application_srl);
        if(applicationEntity == null) return null;


        return applicationEntity;
    }

    @Override
    public void statusModify(long application_srl, int application_group, int application_status) {

        if(application_srl <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("application_srl", "invalid application_srl is less then zero");
            LOG.error(reason.get("application_srl"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        if(application_group <= 0) {
            Map<String, String> reason = new HashMap<>();
            reason.put("application_group", "invalid application_group is less then zero");
            LOG.error(reason.get("application_group"));
            throw new CustomException("invalid_method_parameter", MDV.HTTP_ERR_X_REQUEST, reason);
        }

        ApplicationEntity savedEntity = applicationDao.getApplication(application_srl);
        if(savedEntity == null) {
            Map<String, String> reason = new HashMap<>();
            reason.put("application", "no application for modify. application_srl [" + application_srl + "]");
            LOG.error(reason.get("application"));
            throw new CustomException("modify_application_error", reason);
        }

        boolean willModify = false;
        ApplicationEntity updateEntity = new ApplicationEntity();
        updateEntity.init();

        if(application_status >= MDV.NONE && application_status != savedEntity.getApplication_status()) {
            updateEntity.setApplication_status(application_status);
            willModify = true;
        }

        int ltm = (int)(DateTime.now().getMillis() / 1000);

        if(!willModify) {
            LOG.info("ignore modify application. application_srl [" + application_srl +
                    "]. it is same between saved value and changed value");
        } else {
            //updateEntity.setU_date(ltm);
            applicationDao.statusModify(application_srl, application_group, application_status);
            LOG.info("modify application. updateEntity [" + updateEntity.toString() + "]");
        }
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ApplicationEntity> getApplicationList(long member_srl,
                                                      long advisor_srl,
                                                      List<Integer> application_statues,
                                                      List<Long> contents_srls,
                                                      Map<String, String> searchFilter,
                                                      Map<String, String> sortValue,
                                                      int offset,
                                                      int limit) {
        String user_name = null;

        if(searchFilter != null && searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");

        List<ApplicationEntity> applicationEntities = applicationDao.getApplicationList(member_srl, advisor_srl, application_statues, contents_srls, user_name, sortValue, offset, limit);

        return applicationEntities;
    }

    /**
     * 상담신청 내역 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countApplicationByGroup(int application_group) {
        long applicationCount = applicationDao.countApplicationByGroup(application_group);

        return applicationCount;
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ApplicationEntity> getApplicationListByGroup(long memberl_srl,
                                                             int application_group,
                                                             Map<String, String> sortValue,
                                                             int offset,
                                                             int limit) {
        List<ApplicationEntity> applicationEntities = applicationDao.getApplicationListByGroup(memberl_srl, application_group, sortValue, offset, limit);

        return applicationEntities;
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ApplicationEntity> getApplicationMember(int application_group) {
        List<ApplicationEntity> applicationEntities = applicationDao.getApplicationMember(application_group);

        return applicationEntities;
    }
}
