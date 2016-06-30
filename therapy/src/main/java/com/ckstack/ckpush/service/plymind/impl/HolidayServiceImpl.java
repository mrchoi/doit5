package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.plymind.ApplicationDao;
import com.ckstack.ckpush.dao.plymind.AppointmentDao;
import com.ckstack.ckpush.dao.plymind.HolidayDao;
import com.ckstack.ckpush.dao.plymind.PaymentDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.plymind.ApplicationEntity;
import com.ckstack.ckpush.domain.plymind.AppointmentEntity;
import com.ckstack.ckpush.domain.plymind.HolidayEntity;
import com.ckstack.ckpush.domain.plymind.PaymentEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.HolidayService;
import com.ckstack.ckpush.service.plymind.PaymentService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class HolidayServiceImpl implements HolidayService {
    private final static Logger LOG = LoggerFactory.getLogger(HolidayServiceImpl.class);

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private HolidayDao holidayDao;

    @Override
    public int addHoliday(HolidayEntity holidayEntity) {
        int result = 0;

        try {
            holidayEntity.setHoliday_title(URLDecoder.decode(holidayEntity.getHoliday_title(), "utf-8"));

            String[] arrayTime = holidayEntity.getHoliday_time().split(",");

            for(int i=0; i<arrayTime.length; i++) {
                holidayEntity.setHoliday_time(arrayTime[i]);

                result = holidayDao.addHoliday(holidayEntity);
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public int countHoliday(Map<String, String> searchFilter) {
        if(searchFilter == null) {
            return holidayDao.countHoliday(null);
        }

        String holiday_title = null;

        if(searchFilter.containsKey("holiday_title")) holiday_title = searchFilter.get("holiday_title");

        return holidayDao.countHoliday(holiday_title);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HolidayEntity> getHolidayList(Map<String, String> searchFilter,
                                              Map<String, String> sortValue,
                                              int offset,
                                              int limit) {
        if(searchFilter == null) {
            return holidayDao.getHolidayList(null, sortValue, offset, limit);
        }

        String holiday_title = null;

        if(searchFilter.containsKey("holiday_title")) holiday_title = searchFilter.get("holiday_title");

        return holidayDao.getHolidayList(holiday_title, sortValue, offset, limit);
    }

    @Transactional(readOnly = true)
    @Override
    public int countAdvisorHoliday(long advisor_srl, Map<String, String> searchFilter) {
        if(searchFilter == null) {
            return holidayDao.countAdvisorHoliday(advisor_srl, null, null);
        }

        String nick_name = null;
        String holiday_title = null;

        if(searchFilter.containsKey("nick_name")) nick_name = searchFilter.get("nick_name");
        if(searchFilter.containsKey("holiday_title")) holiday_title = searchFilter.get("holiday_title");

        List<Long> advisor_srls = new ArrayList<Long>();
        if(nick_name != null && nick_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, nick_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        return holidayDao.countAdvisorHoliday(advisor_srl, advisor_srls, holiday_title);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HolidayEntity> getAdvisorHolidayList(long advisor_srl,
                                                     Map<String, String> searchFilter,
                                                     Map<String, String> sort,
                                                     int offset,
                                                     int limit) {
        if(searchFilter == null) {
            return holidayDao.getAdvisorHolidayList(advisor_srl, null, null, sort, offset, limit);
        }

        String nick_name = null;
        String holiday_title = null;

        if(searchFilter.containsKey("nick_name")) nick_name = searchFilter.get("nick_name");
        if(searchFilter.containsKey("holiday_title")) holiday_title = searchFilter.get("holiday_title");

        List<Long> advisor_srls = new ArrayList<Long>();
        if(nick_name != null && nick_name != "") {
            List<MemberEntity> memberEntities = memberDao.getMemberListByGroup(MDV.NUSE, null, null, null, nick_name, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);

            for(MemberEntity memberEntity : memberEntities) {
                advisor_srls.add(memberEntity.getMember_srl());
            }

            if(memberEntities.size() == 0) {
                advisor_srls.add((long)MDV.NUSE);
            }
        }

        return holidayDao.getAdvisorHolidayList(advisor_srl, advisor_srls, holiday_title, sort, offset, limit);
    }

    @Override
    public void deleteHoliday(List<Long> holiday_srls) {
        holidayDao.deleteHoliday(holiday_srls);
    }
}
