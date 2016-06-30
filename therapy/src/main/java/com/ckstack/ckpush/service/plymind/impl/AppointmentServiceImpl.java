package com.ckstack.ckpush.service.plymind.impl;


import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.plymind.ApplicationDao;
import com.ckstack.ckpush.dao.plymind.AppointmentDao;
import com.ckstack.ckpush.dao.plymind.HolidayDao;
import com.ckstack.ckpush.dao.plymind.ProductDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
import com.ckstack.ckpush.domain.plymind.HolidayEntity;
import com.ckstack.ckpush.domain.plymind.ProductEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class AppointmentServiceImpl implements AppointmentService {
    private final static Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private HolidayDao holidayDao;

    /**
     * 예약 정보를 등록한다.
     */
    @Override
    public int add(AppointmentEntity appointmentEntity) {
        int result = 0;

        result = appointmentDao.add(appointmentEntity);

        result = applicationDao.statusProgress(appointmentEntity.getApplication_srl(), MDV.NUSE);

        return result;
    }

    /**
     * 해당 고객의 결제내역 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countAppointment(long member_srl, long advisor_srl, List<Integer> application_statues) {
        long appointmentCount = appointmentDao.countAppointment(member_srl, advisor_srl, application_statues, null, null, null);

        return appointmentCount;
    }

    /**
     * 해당 고객의 결제내역 중 검색 총 갯수를 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public long countSearchAppointment(long member_srl, long advisor_srl, List<Integer> application_statues, Map<String, String> searchFilter) {
        String title = null;
        String user_name = null;
        String advisor_name = null;

        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("title")) title = searchFilter.get("title");
        if(searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");

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

        long appointmentSearchCount = appointmentDao.countAppointment(member_srl, advisor_srl, application_statues, user_name, advisor_srls, product_srls);

        return appointmentSearchCount;
    }

    /**
     * 해당 고객의 결제내역을 조회한다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<AppointmentEntity> getAppointmentList(long member_srl,
                                                      long advisor_srl,
                                                      List<Integer> application_statues,
                                                      Map<String, String> searchFilter,
                                                      Map<String, String> sortValue,
                                                      int offset,
                                                      int limit) {
        String title = null;
        String user_name = null;
        String advisor_name = null;

        if(searchFilter.containsKey("user_name")) user_name = searchFilter.get("user_name");
        if(searchFilter.containsKey("title")) title = searchFilter.get("title");
        if(searchFilter.containsKey("advisor_name")) advisor_name = searchFilter.get("advisor_name");

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

        Map<String, String> sortMap = new HashMap<String, String>();
        if(sortValue.containsKey("title")) {
            sortMap.put("product_srl", sortValue.get("title"));
        } else if(sortValue.containsKey("advisor_name")) {
            sortMap.put("advisor_srl", sortValue.get("advisor_name"));
        } else {
            sortMap = sortValue;
        }

        List<AppointmentEntity> appointmentEntities = appointmentDao.getAppointmentList(member_srl, advisor_srl, application_statues, user_name, advisor_srls, product_srls, sortMap, offset, limit);

        return appointmentEntities;
    }

    @Override
    public int change(AppointmentEntity appointmentEntity) {
        int result = appointmentDao.change(appointmentEntity);

        return result;
    }

    @Override
    public int changeStatus(long appointment_srl, int appointment_status) {
        int result = appointmentDao.changeStatus(appointment_srl, appointment_status);

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentEntity> getAppointmentList(long member_srl) {
        List<AppointmentEntity> appointmentEntities = appointmentDao.getAppointmentList(member_srl);

        return appointmentEntities;
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentEntity getAppointmentBySrl(long appointment_srl) {
        AppointmentEntity appointmentEntity = appointmentDao.getAppointmentBySrl(appointment_srl);

        return appointmentEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentEntity> getAppointmentCheckList(long advisor_srl, String yearMonth) {
        List<AppointmentEntity> appointmentEntities = appointmentDao.getAppointmentCheckList(advisor_srl, yearMonth);

        List<Long> member_srls = new ArrayList<>();
        member_srls.add(Long.valueOf(0));
        member_srls.add(advisor_srl);

        List<HolidayEntity> holidayEntities = holidayDao.getHolidayList(null, null, MDV.NONE, MDV.NONE);

        List<HolidayEntity> advisorHolidayEntities = holidayDao.getAdvisorHolidayList(advisor_srl, null, null, null, MDV.NONE, MDV.NONE);


        String[] array_time = {"9", "10", "11", "12", "14", "15", "16", "17", "18", "19", "20", "21"};
        for(HolidayEntity holidayEntity : holidayEntities) {
            for (int i=0; i<array_time.length; i++) {
                AppointmentEntity appointmentEntity = new AppointmentEntity();

                appointmentEntity.setAppointment_srl(MDV.NONE);
                appointmentEntity.setMember_srl(MDV.NONE);
                appointmentEntity.setAdvisor_srl(MDV.NONE);
                appointmentEntity.setAppointment_date(holidayEntity.getHoliday_date());
                appointmentEntity.setAppointment_time(array_time[i]);
                appointmentEntity.setAppointment_status(MDV.NONE);
                appointmentEntity.setC_date(MDV.NONE);
                appointmentEntity.setU_date(MDV.NONE);

                appointmentEntities.add(appointmentEntity);
            }
        }

        for(HolidayEntity holidayEntity : advisorHolidayEntities) {
            AppointmentEntity appointmentEntity = new AppointmentEntity();

            appointmentEntity.setAppointment_srl(MDV.NONE);
            appointmentEntity.setMember_srl(MDV.NONE);
            appointmentEntity.setAdvisor_srl(MDV.NONE);
            appointmentEntity.setAppointment_date(holidayEntity.getHoliday_date());
            appointmentEntity.setAppointment_time(holidayEntity.getHoliday_time());
            appointmentEntity.setAppointment_status(MDV.NONE);
            appointmentEntity.setC_date(MDV.NONE);
            appointmentEntity.setU_date(MDV.NONE);

            appointmentEntities.add(appointmentEntity);
        }

        return appointmentEntities;
    }
}
