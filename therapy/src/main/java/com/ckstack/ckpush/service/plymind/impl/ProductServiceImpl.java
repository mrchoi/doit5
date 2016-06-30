package com.ckstack.ckpush.service.plymind.impl;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.plymind.*;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.plymind.*;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.plymind.ProductService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wypark on 16. 1. 19.
 */
@Service
@Transactional(value = "transactionManager")
public class ProductServiceImpl implements ProductService {
    private final static Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private PretestingDao pretestingDao;

    /**
     * 컨텐츠 정보 리스트를 가져온다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> getContentsList(int kind_srl) {
        List<ProductEntity> contentsEntities = productDao.getContentsList(kind_srl);

        return contentsEntities;
    }

    /**
     * 상품 정보 리스트를 가져온다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> getProductList(int kind_srl) {
        List<ProductEntity> productEntities = productDao.getProductList(kind_srl, null);

        return productEntities;
    }

    /**
     * 상품 정보 가져온다.
     */
    @Transactional(readOnly = true)
    @Override
    public ProductEntity getProductInfoBySrl(long product_srl) {
        ProductEntity productEntitity = productDao.getProductInfoBySrl(product_srl);

        return productEntitity;
    }

    @Override
    public int add(long member_srl, ApplicationEntity applicationEntity) {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        try {
            /* 1=싸이케어세라피, 2=커플케어세라피 */
            if (applicationEntity.getContents_srl() == 1) {
                boolean bool = this.addSingleCaretherapy(member_srl, ltm, applicationEntity);
                return ltm;
            } else if (applicationEntity.getContents_srl() == 2) {
                boolean bool = this.addCoupleCaretherapy(member_srl, ltm, applicationEntity);
                return ltm;
            /* 3=스카이프케어세라피, 4=텍스트케어세라피 */
            } else if(applicationEntity.getContents_srl() == 3 || applicationEntity.getContents_srl() == 4) {
                boolean bool = this.addTSCaretherapy(member_srl, ltm, applicationEntity);
                return ltm;
            /* 5=컨텐츠 케어 세라피 */
            } else if(applicationEntity.getContents_srl() == 5) {
                boolean bool = this.addContentCaretherapy(member_srl, ltm, applicationEntity);
                return ltm;
            /* 심리검사 */
            } else if(applicationEntity.getContents_srl() == 6
                   || applicationEntity.getContents_srl() == 7
                   || applicationEntity.getContents_srl() == 8
                   || applicationEntity.getContents_srl() == 9) {

                applicationEntity.setApplication_group(ltm);
                applicationEntity.setApplication_status(0);
                applicationEntity.setMember_srl(member_srl);
                applicationEntity.setStart_date("");
                applicationEntity.setEnd_date("");
                applicationEntity.setC_date(ltm);
                applicationEntity.setU_date(MDV.NONE);

                applicationDao.add(applicationEntity);
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return 0;
        }

        return ltm;
    }

    private boolean addSingleCaretherapy(long member_srl, int ltm, ApplicationEntity applicationEntity) {
        try {
            //String input_start_date = applicationEntity.getStart_date();

            //String start_date = "";
            //String end_date = "";
            //int start_date_count = 0;

            for(int i=0; i<applicationEntity.getAdvice_number(); i++) {
                ApplicationEntity addApplicationEntity = new ApplicationEntity();
                /*
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = dateFormat.parse(input_start_date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, (start_date_count * 7));

                start_date = dateFormat.format(new Date(cal.getTimeInMillis()));

                cal.add(Calendar.DATE, 6);

                end_date = dateFormat.format(new Date(cal.getTimeInMillis()));
                */
                addApplicationEntity = applicationEntity;
                addApplicationEntity.setApplication_group(ltm);
                addApplicationEntity.setApplication_number(i + 1);
                addApplicationEntity.setApplication_status(0);
                addApplicationEntity.setMember_srl(member_srl);
                //addApplicationEntity.setStart_date(start_date);
                //addApplicationEntity.setEnd_date(end_date);
                addApplicationEntity.setC_date(ltm);
                addApplicationEntity.setU_date(MDV.NONE);

                applicationDao.add(addApplicationEntity);

                //start_date_count++;
            }

            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setApplication_group(ltm);
            addressEntity.setReceive_name(applicationEntity.getReceive_name());
            addressEntity.setReceive_phone(applicationEntity.getReceive_phone());
            addressEntity.setReceive_address(applicationEntity.getReceive_address());
            addressEntity.setC_date(ltm);
            addressEntity.setU_date(MDV.NONE);

            applicationDao.addAddress(addressEntity);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return false;
        }

        return true;
    }

    private boolean addCoupleCaretherapy(long member_srl, int ltm, ApplicationEntity applicationEntity) {
        try {
            //String input_start_date = applicationEntity.getStart_date();

            //String start_date = "";
            //String end_date = "";
            //int start_date_count = 0;

            for (int y = 0; y < applicationEntity.getAdvice_number(); y++) {
                ApplicationEntity addApplicationEntity = new ApplicationEntity();

                /*
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = dateFormat.parse(input_start_date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, (start_date_count * 7));

                start_date = dateFormat.format(new Date(cal.getTimeInMillis()));

                cal.add(Calendar.DATE, 6);

                end_date = dateFormat.format(new Date(cal.getTimeInMillis()));
                */
                addApplicationEntity = applicationEntity;
                addApplicationEntity.setApplication_group(ltm);
                addApplicationEntity.setApplication_number(y + 1);
                addApplicationEntity.setApplication_status(0);
                addApplicationEntity.setMember_srl(member_srl);
                addApplicationEntity.setStart_date("");
                addApplicationEntity.setEnd_date("");
                addApplicationEntity.setC_date(ltm);
                addApplicationEntity.setU_date(MDV.NONE);

                applicationDao.add(addApplicationEntity);

                //start_date_count++;
            }

            for(int i=0; i<applicationEntity.getMember_srls().length; i++) {
                for (int y = 0; y < applicationEntity.getAdvice_number(); y++) {
                    ApplicationEntity addApplicationEntity = new ApplicationEntity();

                    /*
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Date date = dateFormat.parse(input_start_date);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, (start_date_count * 7));

                    start_date = dateFormat.format(new Date(cal.getTimeInMillis()));

                    cal.add(Calendar.DATE, 6);

                    end_date = dateFormat.format(new Date(cal.getTimeInMillis()));
                    */
                    addApplicationEntity = applicationEntity;
                    addApplicationEntity.setApplication_group(ltm);
                    addApplicationEntity.setApplication_number(y + 1);
                    addApplicationEntity.setApplication_status(0);
                    addApplicationEntity.setMember_srl(applicationEntity.getMember_srls()[i]);
                    addApplicationEntity.setStart_date("");
                    addApplicationEntity.setEnd_date("");
                    addApplicationEntity.setC_date(ltm);
                    addApplicationEntity.setU_date(MDV.NONE);

                    applicationDao.add(addApplicationEntity);

                    //start_date_count++;
                }
            }

            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setApplication_group(ltm);
            addressEntity.setReceive_name(applicationEntity.getReceive_name());
            addressEntity.setReceive_phone(applicationEntity.getReceive_phone());
            addressEntity.setReceive_address(applicationEntity.getReceive_address());
            addressEntity.setC_date(ltm);
            addressEntity.setU_date(MDV.NONE);

            applicationDao.addAddress(addressEntity);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return false;
        }

        return true;
    }

    private boolean addTSCaretherapy(long member_srl, int ltm, ApplicationEntity applicationEntity) {
        try {
            /* 상담 정보 저장 START */
            for(int i=0; i<applicationEntity.getAdvice_number(); i++) {
                ApplicationEntity inputApplicationEntity = new ApplicationEntity();

                inputApplicationEntity = applicationEntity;
                inputApplicationEntity.setApplication_group(ltm);
                inputApplicationEntity.setApplication_number(i+1);
                inputApplicationEntity.setApplication_status(0);
                inputApplicationEntity.setMember_srl(member_srl);
                inputApplicationEntity.setStart_date("");
                inputApplicationEntity.setEnd_date("");
                inputApplicationEntity.setC_date(ltm);
                inputApplicationEntity.setU_date(MDV.NONE);

                applicationDao.add(inputApplicationEntity);

            }
            /* 상담 정보 저장 END */

            List<ApplicationEntity> applicationList = applicationDao.getApplicationListByGroup(member_srl, ltm, null, MDV.NUSE, MDV.NUSE);

            AppointmentEntity appointmententity = new AppointmentEntity();

            appointmententity.setMember_srl(member_srl);
            appointmententity.setAdvisor_srl(applicationEntity.getAdvisor_srl());
            appointmententity.setApplication_srl(applicationList.get(0).getApplication_srl());
            appointmententity.setAppointment_date(applicationEntity.getAppointment_date());
            appointmententity.setAppointment_time(applicationEntity.getAppointment_time());
            appointmententity.setAppointment_status(0);
            appointmententity.setC_date(ltm);
            appointmententity.setU_date(MDV.NONE);

            appointmentDao.add(appointmententity);
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return false;
        }

        return true;
    }

    private boolean addContentCaretherapy(long member_srl, int ltm, ApplicationEntity applicationEntity) {
        try {
            String input_start_date = applicationEntity.getStart_date();
            String input_push_hour = applicationEntity.getPush_time().substring(0, 2);
            String input_push_minute = applicationEntity.getPush_time().substring(2);

            String push_date = applicationEntity.getStart_date();
            String push_time = "";
            int push_huor = 0;

            int push_time_count = 0;
            int push_date_count = 0;

            for(int i=0; i<applicationEntity.getAdvice_number(); i++) {
                ApplicationEntity addApplicationEntity = new ApplicationEntity();

                addApplicationEntity = applicationEntity;
                addApplicationEntity.setApplication_group(ltm);
                addApplicationEntity.setApplication_number(i+1);
                addApplicationEntity.setApplication_status(0);
                addApplicationEntity.setMember_srl(member_srl);
                addApplicationEntity.setC_date(ltm);
                addApplicationEntity.setU_date(MDV.NONE);

                // 토요일 / 일요일은 PUSH 발송 되지 않고 검사지로 상담을 대체한다.
                if( (i % 22) < 20) {
                    if(push_time_count == 4) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Date date = dateFormat.parse(input_start_date);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.DATE, (push_date_count + 1));

                        push_date = dateFormat.format(new Date(cal.getTimeInMillis()));

                        push_time_count = 0;
                        push_date_count++;
                    }

                    // PUSH 시간 설정
                    push_huor = Integer.parseInt(input_push_hour) + (push_time_count * 2);
                    if(push_huor < 10) {
                        push_time = "0" + push_huor + input_push_minute;
                    } else {
                        push_time = push_huor + input_push_minute;
                    }

                    addApplicationEntity.setPush_date(push_date);
                    addApplicationEntity.setPush_time(String.valueOf(push_time));

                    push_time_count++;
                } else {
                    addApplicationEntity.setPush_date("");
                    addApplicationEntity.setPush_time("");
                }

                applicationDao.add(addApplicationEntity);
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 필수항목 정보를 확인 한다.
     */
    @Transactional(readOnly = true)
    @Override
    public boolean userInfoCheck(long member_srl) {
        MemberEntity memberEntity = memberDao.getFullInfo(member_srl, null);

        if(memberEntity != null && memberEntity.getMemberExtraEntity().getBirthday() != null && memberEntity.getMemberExtraEntity().getBirthday() != "") {
            return true;
        }

        return false;
    }

    /**
     * 인테이크지 검사 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    @Override
    public boolean pretestingCheck(long member_srl) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -6);

        int c_date = (int)(cal.getTimeInMillis()/1000L);

        List<PretestingEntity> pretestingEntities = pretestingDao.info(member_srl, c_date);

        if(pretestingEntities.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * 컨텐츠 정보 리스트를 가져온다.
     */
    @Transactional(readOnly = true)
    @Override
    public List<AdvicedomainEntity> getAdvicedomainList(long member_srl, int advicedomain_type, int advice_domain) {
        List<AdvicedomainEntity> advicedomainEntities = productDao.getAdvicedomainList(member_srl, advicedomain_type, advice_domain);

        return advicedomainEntities;
    }
}
