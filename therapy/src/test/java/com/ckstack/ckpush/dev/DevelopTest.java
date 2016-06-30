package com.ckstack.ckpush.dev;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.accessory.DeviceDao;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.app.AppDeviceDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.app.AppDeviceEntity;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.accessory.DeviceEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.service.user.MemberService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dhkim94 on 15. 3. 17..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = true)
public class DevelopTest {
    private final static Logger LOG = LoggerFactory.getLogger(DevelopTest.class);
    private final DateTimeFormatter fmtYYYYMM = DateTimeFormat.forPattern("yyyyMM");

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private AppDeviceDao appDeviceDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confAccessory;
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    private MemberService memberService;

    // 테스트용 AppEntity 를 만들기 위한 app_name 의 header
    private final String appNameHeader = "JUnit-Test-";
    // 생성된 테스트용 App 의 manager id
    private final String appManagerId = "dhkim@ckstack.com";

    @Before
    public void setUp() {

    }

    /**
     * milli-sec 시간이 어떻게 되는지 확인 해 보기 위한 main
     */
    @Test
    public void datePrint() {
        String sd201503 = "201503";
        int si201503 = (int) (fmtYYYYMM.parseDateTime(sd201503).getMillis() / 1000);
        LOG.info("----->si201503[" + si201503 + "]");

        String sd201504 = "201504";
        int si201504 = (int) (fmtYYYYMM.parseDateTime(sd201504).getMillis() / 1000);
        LOG.info("----->si201504[" + si201504 + "]");

        String sd201505 = "201505";
        int si201505 = (int) (fmtYYYYMM.parseDateTime(sd201505).getMillis() / 1000);
        LOG.info("----->si201505[" + si201505 + "]");

        String sd201506 = "201506";
        int si201506 = (int) (fmtYYYYMM.parseDateTime(sd201506).getMillis() / 1000);
        LOG.info("----->si201506[" + si201506 + "]");
    }

    /**
     * Collections.sort 기능 점검. desc, asc 맨날 해깔림.
     */
    @Test
    public void sortDescending() {
        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();

        Map<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("number", 1);

        Map<String, Integer> map2 = new HashMap<String, Integer>();
        map2.put("number", 2);

        Map<String, Integer> map3 = new HashMap<String, Integer>();
        map3.put("number", 3);

        list.add(map1);
        list.add(map2);
        list.add(map3);

        assertThat(list.get(0), is(map1));

        // sort descending
        Collections.sort(list, new Comparator<Map<String, Integer>>() {
            @Override
            public int compare(Map<String, Integer> o1, Map<String, Integer> o2) {
                int no1 = o1.get("number");
                int no2 = o2.get("number");
                return no1 < no2 ? 1 : (no1 == no2 ? 0 : -1);
            }
        });

        assertThat(list.get(0), is(map3));
    }

    /**
     * hash 된 패스워드가 어떻게 되는지 찍어 보기 위한 main
     */
    @Test
    public void bcrypPassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // e19d5cd5af0378da05f63f891c7467af : abcd1234 를 md5 한 값
        // 084e0343a0486ff05530df6c705c8bb4 : guest 를 md5 한 값
        // aa08769cdcb26674c6706093503ff0a3 : member 를 md5 한 값
        String hashedPassword = passwordEncoder.encode("aa08769cdcb26674c6706093503ff0a3");
        LOG.info("----->hashedPassword [" + hashedPassword + "]");
    }

    @Test
    public void createUUID() {
        LOG.info("uuid=["+UUID.randomUUID().toString().replaceAll("-", "")+"]");
    }

    /**
     * 테스트를 위해서 원하는 만큼의 회원을 가입 시킨다.
     * 평소에는 ignore 하고 필요 할때만 사용 한다.
     */
    @Ignore("using only manualy")
    @Test
    @Rollback(value = false)
    public void makeTestMemberData() {
        int userCount = 48;
        String userHeader = "junit";

        MemberEntity rootUser = memberDao.get(MDV.NUSE, appManagerId);
        assertThat(rootUser, is(notNullValue()));

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail_address("junit.robot@ckstack.com");
        memberEntity.setUser_password("1234");
        memberEntity.setMobile_phone_number("01012341234");

        for(int i=0 ; i<userCount; i++) {
            memberEntity.setNick_name(userHeader + "-nickname-" + i);
            memberEntity.setUser_name(userHeader+"-username-"+i);
            memberEntity.setUser_id(userHeader+"-"+webUtilService.getRandomAlphabetLower(8)+"-"+i);
            memberService.signUp(memberEntity, MDV.NUSE, null, MDV.NUSE, rootUser.getMember_srl(), "127.0.0.1");
        }
    }

    /**
     * makeTestMemberData 에서 가입 시킨 테스트용 회원을 삭제 시킨다.
     * 평소에는 ignore 하고 필요 할때만 사용 한다.
     */
    @Ignore("using only manualy")
    @Test
    @Rollback(value = false)
    public void deleteTestMemberData() {
        String userHeader = "junit";
        List<MemberEntity> memberEntities = memberService.getMemberList(userHeader, null, MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        List<Long> memberSrls = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntities) memberSrls.add(memberEntity.getMember_srl());

        memberDao.delete(MDV.NUSE, null, memberSrls);
    }

    /**
     * app 리스트 웹페이지 테스트를 위해 무작위 앱 데이터를 넣기 위한 main.
     * 평소에는 주석으로 막아 두고 필요 할때만 풀어서 사용 한다.
     * 삭제를 위해서는 바로 아래 있는 deleteTestAppEntityData 를 사용 한다.
     */
    @Ignore("using only manualy")
    @Test
    @Rollback(value = false)
    public void makeTestAppEntityData() {
        MemberEntity memberEntity = memberDao.get(MDV.NUSE, appManagerId);
        assertThat(memberEntity, is(notNullValue()));

        int insertDataAmount = 20;
        Random random = new Random();
        random.setSeed(DateTime.now().getMillis());

        // 지원 하는 단말기 타입을 random 으로 넣기 위해서
        String[] arrSupportTerminal = StringUtils.split(confCommon.getProperty("user_terminal_support"), ",");
        List<Integer> supportTerminal = new ArrayList<>();

        assert arrSupportTerminal != null;
        for(String element : arrSupportTerminal) {
            String[] values = StringUtils.split(element, ":");
            if(values.length < 2) continue;
            supportTerminal.add(Integer.parseInt(StringUtils.trim(values[0]), 10));
        }

        int currTime;
        AppEntity appEntity = new AppEntity();
        for(int i=0 ; i<insertDataAmount ; i++) {
            // AppEntity 추가
            appEntity.init();
            appEntity.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
            appEntity.setApp_name(appNameHeader + random.nextInt(20) + "-App(Auto) " + i);
            appEntity.setApi_key(UUID.randomUUID().toString().replaceAll("-", ""));
            appEntity.setApp_version(random.nextInt(10) + "." + random.nextInt(20) + "." + random.nextInt(100));
            appEntity.setApi_secret(UUID.randomUUID().toString().replaceAll("-", ""));
            appEntity.setEnabled(MDV.YES);
            appEntity.setReg_push_terminal(0);
            appEntity.setReg_terminal(0);
            appEntity.setPush_count(0);

            currTime = (int)(DateTime.now().getMillis() / 1000);
            appEntity.setC_date(currTime);
            appEntity.setU_date(currTime);

            appDao.add(appEntity);

        }

        LOG.info("create " + insertDataAmount + " app for testing");
    }

    /**
     * makeTestAppEntityData 에서 추가 한 app 데이터를 삭제 한다.
     * 평소에는 ignore 하고 필요 할때만 사용 한다.
     */
    @Ignore("using only manualy")
    @Test
    @Rollback(value = false)
    public void deleteTestAppEntityData() {
        List<AppEntity> appEntities = appDao.get(null, appNameHeader, null, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat(appEntities, is(notNullValue()));
        assertThat(appEntities.size() > 0, is(true));

        List<Integer> appSrls = new ArrayList<>();
        for(AppEntity appEntity : appEntities) appSrls.add(appEntity.getApp_srl());
        appDao.delete(MDV.NUSE, appSrls);

        LOG.info("delete " + appEntities.size() + " test app");
    }

    /**
     * 단말기를 추가하고, 추가한 단말기를 특정 앱에 할당 시킨다.
     */
    @Ignore("using only manualy")
    @Test
    @Rollback(value = false)
    public void addAndroidDevceInApp() {
        int addCount = 52;
        int appSrl = 20;       // 이건 할때 마다 수정 해야 한다. 테스트용 앱의 app_srl 로 설정
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        Random random = new Random();
        random.setSeed(DateTime.now().getMillis());

        int androidDevice = Integer.parseInt(confCommon.getProperty("user_terminal_android"), 10);
        int iOSDevice = Integer.parseInt(confCommon.getProperty("user_terminal_iphone"), 10);

        String[] androidVersion = {"5.1.0", "5.0", "4.4", "4.4.1", "4.4.2", "4.4.3", "4.4.4"};
        String[] iOSVersion = {"6.0", "6.1", "7.0", "7.1", "8.0", "8.1", "8.2", "8.3"};

        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setMobile_phone_number("");
        deviceEntity.setC_date(ltm);
        deviceEntity.setU_date(ltm);

        AppDeviceEntity appDeviceEntity = new AppDeviceEntity();
        appDeviceEntity.setApp_srl(appSrl);
        appDeviceEntity.setAllow_push(MDV.YES);
        appDeviceEntity.setEnabled(MDV.YES);
        appDeviceEntity.setC_date(ltm);
        appDeviceEntity.setU_date(ltm);

        for(int i=0 ; i<addCount ; i++) {
            // 신규 단말기 생성
            //deviceEntity.setDevice_id(UUID.randomUUID().toString().replaceAll("-", ""));
            deviceEntity.setDevice_id(UUID.randomUUID().toString());

            int deviceClass = random.nextInt(2)%2==0 ? androidDevice : iOSDevice;

            if(deviceClass == androidDevice) {
                deviceEntity.setDevice_type("Google Android");
                deviceEntity.setOs_version(androidVersion[random.nextInt(androidVersion.length - 1)]);
            } else {
                deviceEntity.setDevice_type("Apple iPhone");
                deviceEntity.setOs_version(iOSVersion[random.nextInt(iOSVersion.length - 1)]);
            }

            deviceEntity.setDevice_class(deviceClass);
            deviceDao.add(deviceEntity);

            // 신규 단말기 앱에 매핑
            int regPushId = random.nextInt(2)%2==0 ? MDV.YES : MDV.NO;

            appDeviceEntity.setDevice_srl(deviceEntity.getDevice_srl());
            appDeviceEntity.setPush_id("push id-" + DateTime.now().getMillis() + "-" + i);
            appDeviceEntity.setReg_push_id(regPushId);
            appDeviceDao.add(appDeviceEntity);

            appDao.increase(appSrl, null, true, regPushId==MDV.YES?true:false, false);
        }
    }

}
