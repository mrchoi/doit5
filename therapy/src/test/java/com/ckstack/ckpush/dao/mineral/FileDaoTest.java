package com.ckstack.ckpush.dao.mineral;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.push.PushMessageDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.mineral.FileEntity;
import com.ckstack.ckpush.domain.mineral.MemberPicEntity;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.mineral.PushMessagePicEntity;
import com.ckstack.ckpush.domain.push.PushMessageEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 4. 1..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class FileDaoTest {
    @Autowired
    private FileDao fileDao;
    @Autowired
    private Properties confTblFile;
    @Autowired
    private MemberPicDao memberPicDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private PushMessageDao pushMessageDao;
    @Autowired
    private PushMessagePicDao pushMessagePicDao;
    @Autowired
    private Properties confCommon;

    private FileEntity fileEntity1;
    private FileEntity fileEntity2;
    private FileEntity fileEntity3;
    private FileEntity fileEntity4;
    private FileEntity fileEntity5;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;

    private MemberPicEntity memberPicEntity1;
    private MemberPicEntity memberPicEntity2;
    private MemberPicEntity memberPicEntity3;
    private MemberPicEntity memberPicEntity4;
    private MemberPicEntity memberPicEntity5;

    private AppEntity appEntity1;

    private PushMessageEntity pushMessageEntity1;
    private PushMessageEntity pushMessageEntity2;
    private PushMessageEntity pushMessageEntity3;

    private PushMessagePicEntity pushMessagePicEntity1;
    private PushMessagePicEntity pushMessagePicEntity2;
    private PushMessagePicEntity pushMessagePicEntity3;
    private PushMessagePicEntity pushMessagePicEntity4;
    private PushMessagePicEntity pushMessagePicEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        fileEntity1 = new FileEntity();
        fileEntity1.setOrig_name("");
        fileEntity1.setFile_size(1024);
        fileEntity1.setFile_path("/home/dhkim/a.png");
        fileEntity1.setFile_url("/a.png");
        fileEntity1.setWidth(100);
        fileEntity1.setHeight(100);
        fileEntity1.setThumb_path("/home/dhkim/b.png");
        fileEntity1.setThumb_url("/b.png");
        fileEntity1.setThumb_width(50);
        fileEntity1.setThumb_height(50);
        fileEntity1.setIpaddress("127.0.0.1");
        fileEntity1.setDeleted(MDV.NO);
        fileEntity1.setC_date(ltm);
        fileEntity1.setU_date(ltm);

        fileEntity2 = new FileEntity();
        fileEntity2.setOrig_name("");
        fileEntity2.setFile_size(MDV.NONE);
        fileEntity2.setFile_path("");
        fileEntity2.setFile_url("http://img.ckpush.com/sample.png");
        fileEntity2.setWidth(MDV.NONE);
        fileEntity2.setHeight(MDV.NONE);
        fileEntity2.setThumb_path("");
        fileEntity2.setThumb_url("");
        fileEntity2.setThumb_width(MDV.NONE);
        fileEntity2.setThumb_height(MDV.NONE);
        fileEntity2.setIpaddress("");
        fileEntity2.setDeleted(MDV.NO);
        fileEntity2.setC_date(ltm);
        fileEntity2.setU_date(ltm);

        fileEntity3 = new FileEntity();
        fileEntity3.setOrig_name("");
        fileEntity3.setFile_size(MDV.NONE);
        fileEntity3.setFile_path("");
        fileEntity3.setFile_url("http://img.ckpush.com/sample2.png");
        fileEntity3.setWidth(MDV.NONE);
        fileEntity3.setHeight(MDV.NONE);
        fileEntity3.setThumb_path("");
        fileEntity3.setThumb_url("");
        fileEntity3.setThumb_width(MDV.NONE);
        fileEntity3.setThumb_height(MDV.NONE);
        fileEntity3.setIpaddress("");
        fileEntity3.setDeleted(MDV.NO);
        fileEntity3.setC_date(ltm);
        fileEntity3.setU_date(ltm);

        fileEntity4 = new FileEntity();
        fileEntity4.setOrig_name("a33.png");
        fileEntity4.setFile_size(2048);
        fileEntity4.setFile_path("/home/dhkim/a2.png");
        fileEntity4.setFile_url("/a2.png");
        fileEntity4.setWidth(200);
        fileEntity4.setHeight(200);
        fileEntity4.setThumb_path("/home/dhkim/b2.png");
        fileEntity4.setThumb_url("/b2.png");
        fileEntity4.setThumb_width(50);
        fileEntity4.setThumb_height(50);
        fileEntity4.setIpaddress("127.0.0.1");
        fileEntity4.setDeleted(MDV.NO);
        fileEntity4.setC_date(ltm);
        fileEntity4.setU_date(ltm);

        fileEntity5 = new FileEntity();
        fileEntity5.setOrig_name("a3344.png");
        fileEntity5.setFile_size(567651328);
        fileEntity5.setFile_path("/home/dhkim/a3344dfdsaf.png");
        fileEntity5.setFile_url("/a3344dfdsaf.png");
        fileEntity5.setWidth(200);
        fileEntity5.setHeight(200);
        fileEntity5.setThumb_path("/home/dhkim/bdsfdad1232.png");
        fileEntity5.setThumb_url("/bdsfdad1232.png");
        fileEntity5.setThumb_width(50);
        fileEntity5.setThumb_height(50);
        fileEntity5.setIpaddress("127.0.0.1");
        fileEntity5.setDeleted(MDV.NO);
        fileEntity5.setC_date(ltm);
        fileEntity5.setU_date(ltm);

        memberEntity1 = new MemberEntity();
        memberEntity1.init();
        memberEntity1.setUser_id("junit-test-1");
        memberEntity1.setEmail_address("dhkim1@ckstack.com");
        memberEntity1.setUser_password("123456790");
        memberEntity1.setUser_name("임의 사용자1");
        memberEntity1.setNick_name("임의 사용자 별명1");
        memberEntity1.setMobile_phone_number("01011112222");
        memberEntity1.setAllow_mailing(MDV.YES);
        memberEntity1.setAllow_message(MDV.YES);
        memberEntity1.setDescription("임의 사용자 1 입니다.");
        memberEntity1.setLast_login_date(ltm);
        memberEntity1.setChange_password_date(ltm);
        memberEntity1.setEnabled(MDV.YES);
        memberEntity1.setEmail_confirm(MDV.NO);
        memberEntity1.setSign_out(MDV.NO);
        memberEntity1.setC_date(ltm);
        memberEntity1.setU_date(ltm);

        memberEntity2 = new MemberEntity();
        memberEntity2.init();
        memberEntity2.setUser_id("junit-test-2");
        memberEntity2.setEmail_address("dhkim2@ckstack.com");
        memberEntity2.setUser_password("123456790");
        memberEntity2.setUser_name("임의 사용자2");
        memberEntity2.setNick_name("임의 사용자 별명2");
        memberEntity2.setMobile_phone_number("01033334444");
        memberEntity2.setAllow_mailing(MDV.YES);
        memberEntity2.setAllow_message(MDV.YES);
        memberEntity2.setDescription("임의 사용자 2 입니다.");
        memberEntity2.setLast_login_date(ltm);
        memberEntity2.setChange_password_date(ltm);
        memberEntity2.setEnabled(MDV.YES);
        memberEntity2.setEmail_confirm(MDV.NO);
        memberEntity2.setSign_out(MDV.NO);
        memberEntity2.setC_date(ltm);
        memberEntity2.setU_date(ltm);

        memberEntity3 = new MemberEntity();
        memberEntity3.init();
        memberEntity3.setUser_id("junit-test-3");
        memberEntity3.setEmail_address("dhkim3@ckstack.com");
        memberEntity3.setUser_password("123456790");
        memberEntity3.setUser_name("임의 사용자3");
        memberEntity3.setNick_name("임의 사용자 별명3");
        memberEntity3.setMobile_phone_number("01055556666");
        memberEntity3.setAllow_mailing(MDV.YES);
        memberEntity3.setAllow_message(MDV.YES);
        memberEntity3.setDescription("임의 사용자 3 입니다.");
        memberEntity3.setLast_login_date(ltm);
        memberEntity3.setChange_password_date(ltm);
        memberEntity3.setEnabled(MDV.YES);
        memberEntity3.setEmail_confirm(MDV.NO);
        memberEntity3.setSign_out(MDV.NO);
        memberEntity3.setC_date(ltm);
        memberEntity3.setU_date(ltm);

        memberPicEntity1 = new MemberPicEntity();
        memberPicEntity1.setC_date(ltm);

        memberPicEntity2 = new MemberPicEntity();
        memberPicEntity2.setC_date(ltm);

        memberPicEntity3 = new MemberPicEntity();
        memberPicEntity3.setC_date(ltm);

        memberPicEntity4 = new MemberPicEntity();
        memberPicEntity4.setC_date(ltm);

        memberPicEntity5 = new MemberPicEntity();
        memberPicEntity5.setC_date(ltm);

        String api_key1 = UUID.randomUUID().toString().replaceAll("-", "");
        String api_secret1 = UUID.randomUUID().toString().replaceAll("-", "");

        appEntity1 = new AppEntity();
        appEntity1.init();
        appEntity1.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity1.setApp_name("junit test app9000");
        appEntity1.setApi_key(api_key1);
        appEntity1.setApp_version("1.0.0");
        appEntity1.setApi_secret(api_secret1);
        appEntity1.setEnabled(MDV.YES);
        appEntity1.setReg_terminal(0);
        appEntity1.setReg_push_terminal(0);
        appEntity1.setC_date(ltm);
        appEntity1.setU_date(ltm);

        pushMessageEntity1 = new PushMessageEntity();
        pushMessageEntity1.setUser_id("robot");
        pushMessageEntity1.setPush_title("junit test push1");
        pushMessageEntity1.setPush_text("junit test push body1");
        pushMessageEntity1.setCallback_url("pushapp://document/1");
        pushMessageEntity1.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessageEntity2 = new PushMessageEntity();
        pushMessageEntity2.setUser_id("robot");
        pushMessageEntity2.setPush_title("junit test push2");
        pushMessageEntity2.setPush_text("junit test push body2");
        pushMessageEntity2.setCallback_url("pushapp://document/2");
        pushMessageEntity2.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessageEntity3 = new PushMessageEntity();
        pushMessageEntity3.setUser_id("robot");
        pushMessageEntity3.setPush_title("junit test push3");
        pushMessageEntity3.setPush_text("junit test push body3");
        pushMessageEntity3.setCallback_url("pushapp://document/3");
        pushMessageEntity3.setPush_target(confCommon.getProperty("push_target_all_in_app"));

        pushMessagePicEntity1 = new PushMessagePicEntity();
        pushMessagePicEntity1.setC_date(ltm);

        pushMessagePicEntity2 = new PushMessagePicEntity();
        pushMessagePicEntity2.setC_date(ltm);

        pushMessagePicEntity3 = new PushMessagePicEntity();
        pushMessagePicEntity3.setC_date(ltm);

        pushMessagePicEntity4 = new PushMessagePicEntity();
        pushMessagePicEntity4.setC_date(ltm);

        pushMessagePicEntity5 = new PushMessagePicEntity();
        pushMessagePicEntity5.setC_date(ltm);
    }

    /**
     * 프로필 이미지 파일 테이블 기본 테스트
     */
    @Test
    @Rollback
    public void profilePicBasicTest() {
        // pre select list
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        long count1 = fileDao.count(tableName, null, null, MDV.NUSE, MDV.NUSE);
        List<FileEntity> list1 = fileDao.get(tableName, null, MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        // select list
        long count2 = fileDao.count(tableName, null, null, MDV.NUSE, MDV.NUSE);
        assertThat(count1+5, is(count2));
        List<FileEntity> list2 = fileDao.get(tableName, null, MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        FileEntity resultVo1 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(fileEntity1), is(true));

        FileEntity resultVo2 = fileDao.get(tableName, fileEntity2.getFile_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(fileEntity2), is(true));

        FileEntity resultVo3 = fileDao.get(tableName, fileEntity3.getFile_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(fileEntity3), is(true));

        FileEntity resultVo4 = fileDao.get(tableName, fileEntity4.getFile_srl(), MDV.NUSE);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(fileEntity4), is(true));

        FileEntity resultVo5 = fileDao.get(tableName, fileEntity5.getFile_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(fileEntity5), is(true));

        // modify
        int next_deleted = MDV.YES;
        String next_file_url = "/afdafdasfa12312fdsafsadfsdaf.png";
        FileEntity updateVo = new FileEntity();
        updateVo.init();
        updateVo.setDeleted(next_deleted);
        updateVo.setFile_url(next_file_url);
        fileDao.modify(tableName, updateVo, fileEntity1.getFile_srl(), null);

        // select
        FileEntity resultVo6 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(fileEntity1), is(false));
        assertThat(resultVo6.getDeleted(), is(next_deleted));
        assertThat(resultVo6.getFile_url(), is(next_file_url));

        // select one
        FileEntity resultVo7 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NO);
        assertThat(resultVo7, is(nullValue()));

        // delete
        fileDao.delete(tableName, fileEntity1.getFile_srl());

        FileEntity resultVo8 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo8, is(nullValue()));
    }

    /**
     * tbl_profile_pic one row select 시 조건이 유효하지 않을때 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneFileTest() {
        // pre select list
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        // insert
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        fileDao.get(tableName, MDV.NUSE, MDV.NUSE);
    }

    /**
     * tbl_profile_pic update 시 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateFileTest() {
        // pre select list
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        // insert
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        // modify
        int next_deleted = MDV.YES;
        String next_file_url = "/afdafdasfa12312fdsafsadfsdaf.png";
        FileEntity updateVo = new FileEntity();
        updateVo.init();
        updateVo.setDeleted(next_deleted);
        updateVo.setFile_url(next_file_url);
        fileDao.modify(tableName, updateVo, MDV.NUSE, null);
    }

    /**
     * tbl_profile_pic delete 시 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteFileTest() {
        // pre select list
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        // insert
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        // delete
        fileDao.delete(tableName, MDV.NUSE);
    }

    /**
     * 프로필 이미지 파일 리스트 select 테스트
     */
    @Test
    @Rollback
    public void fileListTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        // select using index indicator
        List<FileEntity> list1 = fileDao.get(tableName, null, MDV.NUSE, null, MDV.NUSE, null, 0, 3);
        assertThat(list1, is(notNullValue()));
        assertThat(list1.size(), is(3));

        long lastFileSrl = list1.get(2).getFile_srl();

        List<FileEntity> list2 = fileDao.get(tableName, null, MDV.NUSE, null, lastFileSrl, null, 0, 3);
        assertThat(list2, is(notNullValue()));
        assertThat(list2.size() >= 2, is(true));

        List<FileEntity> list3 = fileDao.get(tableName, null, MDV.NUSE, null, MDV.NUSE, null, 0, 5);
        assertThat(list3, is(notNullValue()));
        assertThat(list3.size(), is(5));
        assertThat(list3.get(3).equals(list2.get(0)), is(true));
        assertThat(list3.get(4).equals(list2.get(1)), is(true));

        // select file_srls
        List<Long> file_srls = new ArrayList<Long>();
        List<FileEntity> list4 = fileDao.get(tableName, file_srls, MDV.NUSE, null, MDV.NUSE, null, 0, 5);
        assertThat(list4, is(notNullValue()));
        assertThat(list4.size(), is(5));

        file_srls.add(fileEntity2.getFile_srl());
        file_srls.add(fileEntity5.getFile_srl());

        List<FileEntity> list5 = fileDao.get(tableName, file_srls, MDV.NUSE, null, MDV.NUSE, null, 0, 5);
        assertThat(list5, is(notNullValue()));
        assertThat(list5.size(), is(2));
        assertThat(list5.get(0).equals(fileEntity5), is(true));
        assertThat(list5.get(1).equals(fileEntity2), is(true));
    }

    /**
     * 사용자 프로필 사진 매핑 테이블 기본 테스트
     */
    @Test
    @Rollback
    public void memberPicBasicTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        // pre select list
        long count1 = memberPicDao.count(MDV.NUSE, MDV.NUSE);
        List<MemberPicEntity> list1 = memberPicDao.get(MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        memberPicEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberPicEntity1.setFile_srl(fileEntity1.getFile_srl());
        memberPicDao.add(memberPicEntity1);

        memberPicEntity2.setMember_srl(memberEntity2.getMember_srl());
        memberPicEntity2.setFile_srl(fileEntity2.getFile_srl());
        memberPicDao.add(memberPicEntity2);

        memberPicEntity3.setMember_srl(memberEntity3.getMember_srl());
        memberPicEntity3.setFile_srl(fileEntity3.getFile_srl());
        memberPicDao.add(memberPicEntity3);

        // select list
        long count2 = memberPicDao.count(MDV.NUSE, MDV.NUSE);
        assertThat(count1+3, is(count2));
        List<MemberPicEntity> list2 = memberPicDao.get(MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        MemberPicEntity resultVo1 = memberPicDao.get(memberPicEntity1.getMember_srl(), memberPicEntity1.getFile_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(memberPicEntity1), is(true));

        MemberPicEntity resultVo2 = memberPicDao.get(memberPicEntity2.getMember_srl(), memberPicEntity2.getFile_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(memberPicEntity2), is(true));

        MemberPicEntity resultVo3 = memberPicDao.get(memberPicEntity3.getMember_srl(), memberPicEntity3.getFile_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(memberPicEntity3), is(true));

        // delete memberPic
        memberPicDao.delete(memberPicEntity1.getMember_srl(), memberPicEntity1.getFile_srl());

        // select one
        MemberPicEntity resultVo4 = memberPicDao.get(memberPicEntity1.getMember_srl(), memberPicEntity1.getFile_srl());
        assertThat(resultVo4, is(nullValue()));

        // delete member
        memberDao.delete(memberEntity2.getMember_srl(), null, null);

        // select one
        MemberPicEntity resultVo5 = memberPicDao.get(memberPicEntity2.getMember_srl(), memberPicEntity2.getFile_srl());
        assertThat(resultVo5, is(nullValue()));

        // delete file
        fileDao.delete(tableName, fileEntity3.getFile_srl());

        // select one
        MemberPicEntity resultVo6 = memberPicDao.get(memberPicEntity3.getMember_srl(), memberPicEntity3.getFile_srl());
        assertThat(resultVo6, is(nullValue()));
    }

    /**
     * tbl_member_pic select one 조건이 유효하지 않을때 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void noneSelectOneMemberPicTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        // insert
        memberPicEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberPicEntity1.setFile_srl(fileEntity1.getFile_srl());
        memberPicDao.add(memberPicEntity1);

        memberPicEntity2.setMember_srl(memberEntity2.getMember_srl());
        memberPicEntity2.setFile_srl(fileEntity2.getFile_srl());
        memberPicDao.add(memberPicEntity2);

        memberPicEntity3.setMember_srl(memberEntity3.getMember_srl());
        memberPicEntity3.setFile_srl(fileEntity3.getFile_srl());
        memberPicDao.add(memberPicEntity3);

        memberPicDao.get(MDV.NUSE, MDV.NUSE);
    }

    /**
     * tbl_member_pic delete row 조건이 유효하지 않을때 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeletememberPicTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        // insert
        memberPicEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberPicEntity1.setFile_srl(fileEntity1.getFile_srl());
        memberPicDao.add(memberPicEntity1);

        memberPicEntity2.setMember_srl(memberEntity2.getMember_srl());
        memberPicEntity2.setFile_srl(fileEntity2.getFile_srl());
        memberPicDao.add(memberPicEntity2);

        memberPicEntity3.setMember_srl(memberEntity3.getMember_srl());
        memberPicEntity3.setFile_srl(fileEntity3.getFile_srl());
        memberPicDao.add(memberPicEntity3);

        memberPicDao.delete(MDV.NUSE, MDV.NUSE);
    }

    /**
     * 유저 프로필 매핑 테이블을 이용해서 유저의 프로필 파일 정보 가져오는 메소드 테스트
     */
    @Test
    @Rollback
    public void memberPicAndFileTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_profile_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_profile_file");

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));

        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        fileEntity1.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileEntity2.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileEntity3.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileEntity4.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileEntity5.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        List<Long> member_srls = new ArrayList<Long>();

        // select empty List
        List<MemberPicEntity> list1 = memberPicDao.getAndFile(member_srls, MDV.NUSE);
        assertThat(list1, is(notNullValue()));

        // select member1 and file. result nullValue
        MemberPicEntity resultVo1 = memberPicDao.getAndFile(memberEntity1.getMember_srl(), MDV.NUSE);
        assertThat(resultVo1, is(nullValue()));

        // insert member1 : file1
        memberPicEntity1.setMember_srl(memberEntity1.getMember_srl());
        memberPicEntity1.setFile_srl(fileEntity1.getFile_srl());
        memberPicDao.add(memberPicEntity1);

        // select member1 and files
        MemberPicEntity resultVo2 = memberPicDao.getAndFile(memberEntity1.getMember_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.getFileEntities().size(), is(1));
        assertThat(resultVo2.getFileEntities().get(0).equals(fileEntity1), is(true));

        // insert member1 : file2
        memberPicEntity2.setMember_srl(memberEntity1.getMember_srl());
        memberPicEntity2.setFile_srl(fileEntity2.getFile_srl());
        memberPicDao.add(memberPicEntity2);

        // select member1 and files
        MemberPicEntity resultVo3 = memberPicDao.getAndFile(memberEntity1.getMember_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.getFileEntities().size(), is(2));
        assertThat(resultVo3.getFileEntities().get(0).equals(fileEntity1), is(true));
        assertThat(resultVo3.getFileEntities().get(1).equals(fileEntity2), is(true));

        // select empty List
        List<MemberPicEntity> list2 = memberPicDao.getAndFile(member_srls, MDV.NUSE);
        assertThat(list2, is(notNullValue()));

        // select member2 files. result nullValue
        MemberPicEntity resultVo4 = memberPicDao.getAndFile(memberEntity2.getMember_srl(), MDV.NUSE);
        assertThat(resultVo4, is(nullValue()));

        // insert member2 : file3
        memberPicEntity3.setMember_srl(memberEntity2.getMember_srl());
        memberPicEntity3.setFile_srl(fileEntity3.getFile_srl());
        memberPicDao.add(memberPicEntity3);

        // select member2 and files
        MemberPicEntity resultVo5 = memberPicDao.getAndFile(memberEntity2.getMember_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.getFileEntities().size(), is(1));
        assertThat(resultVo5.getFileEntities().get(0).equals(fileEntity3), is(true));

        // select empty List
        List<MemberPicEntity> list3 = memberPicDao.getAndFile(member_srls, MDV.NUSE);
        assertThat(list3, is(notNullValue()));

        // select member3 files. result nullValue
        MemberPicEntity resultVo6 = memberPicDao.getAndFile(memberEntity3.getMember_srl(), MDV.NUSE);
        assertThat(resultVo6, is(nullValue()));

        // insert member3 : file4
        memberPicEntity4.setMember_srl(memberEntity3.getMember_srl());
        memberPicEntity4.setFile_srl(fileEntity4.getFile_srl());
        memberPicDao.add(memberPicEntity4);

        // insert member3 : file5
        memberPicEntity5.setMember_srl(memberEntity3.getMember_srl());
        memberPicEntity5.setFile_srl(fileEntity5.getFile_srl());
        memberPicDao.add(memberPicEntity5);

        // select member3 and files
        MemberPicEntity resultVo7 = memberPicDao.getAndFile(memberEntity3.getMember_srl(), MDV.NUSE);
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.getFileEntities().size(), is(2));
        assertThat(resultVo7.getFileEntities().get(0).equals(fileEntity4), is(true));
        assertThat(resultVo7.getFileEntities().get(1).equals(fileEntity5), is(true));

        // set delete flag
        FileEntity updateVo = new FileEntity();
        updateVo.init();
        updateVo.setDeleted(MDV.YES);
        fileDao.modify(tableName, updateVo, fileEntity4.getFile_srl(), null);

        // select member3 and files
        MemberPicEntity resultVo8 = memberPicDao.getAndFile(memberEntity3.getMember_srl(), MDV.NO);
        assertThat(resultVo8, is(notNullValue()));
        assertThat(resultVo8.getFileEntities().size(), is(1));
        assertThat(resultVo8.getFileEntities().get(0).equals(fileEntity5), is(true));

        // set delete flag
        updateVo.init();
        updateVo.setDeleted(MDV.YES);
        fileDao.modify(tableName, updateVo, fileEntity5.getFile_srl(), null);

        // select member3 and files
        MemberPicEntity resultVo9 = memberPicDao.getAndFile(memberEntity3.getMember_srl(), MDV.NO);
        assertThat(resultVo9, is(nullValue()));

        member_srls.add(memberEntity1.getMember_srl());

        // select list
        List<MemberPicEntity> list4 = memberPicDao.getAndFile(member_srls, MDV.NUSE);
        assertThat(list4, is(notNullValue()));
        assertThat(list4.size(), is(1));
        assertThat(list4.get(0).equals(resultVo3), is(true));

        member_srls.add(memberEntity2.getMember_srl());

        // select list
        List<MemberPicEntity> list5 = memberPicDao.getAndFile(member_srls, MDV.NUSE);
        assertThat(list5, is(notNullValue()));
        assertThat(list5.size(), is(2));
        assertThat(list5.get(0).equals(resultVo5), is(true));
        assertThat(list5.get(1).equals(resultVo3), is(true));

        // select member3 and files
        MemberPicEntity resultVo10 = memberPicDao.getAndFile(memberEntity3.getMember_srl(), MDV.NUSE);
        assertThat(resultVo10, is(notNullValue()));
        assertThat(resultVo10.getFileEntities().size(), is(2));

        member_srls.add(memberEntity3.getMember_srl());

        // select list
        List<MemberPicEntity> list6 = memberPicDao.getAndFile(member_srls, MDV.NUSE);
        assertThat(list6, is(notNullValue()));
        assertThat(list6.size(), is(3));
        assertThat(list6.get(0).equals(resultVo10), is(true));
        assertThat(list6.get(1).equals(resultVo5), is(true));
        assertThat(list6.get(2).equals(resultVo3), is(true));

        // select list
        List<MemberPicEntity> list7 = memberPicDao.getAndFile(member_srls, MDV.NO);
        assertThat(list7, is(notNullValue()));
        assertThat(list7.size(), is(2));
        assertThat(list7.get(0).equals(resultVo5), is(true));
        assertThat(list7.get(1).equals(resultVo3), is(true));
    }

    /**
     * push 메시지 이미지 파일 테이블 기본 테스트
     */
    @Test
    @Rollback
    public void pushPicBasicTest() {
        // pre select list
        assertThat(confTblFile.containsKey("tbl_name_push_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_push_file");

        long count1 = fileDao.count(tableName, null, null, MDV.NUSE, MDV.NUSE);
        List<FileEntity> list1 = fileDao.get(tableName, null, MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        // select list
        long count2 = fileDao.count(tableName, null, null, MDV.NUSE, MDV.NUSE);
        assertThat(count1+5, is(count2));
        List<FileEntity> list2 = fileDao.get(tableName, null, MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        FileEntity resultVo1 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(fileEntity1), is(true));

        FileEntity resultVo2 = fileDao.get(tableName, fileEntity2.getFile_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(fileEntity2), is(true));

        FileEntity resultVo3 = fileDao.get(tableName, fileEntity3.getFile_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(fileEntity3), is(true));

        FileEntity resultVo4 = fileDao.get(tableName, fileEntity4.getFile_srl(), MDV.NUSE);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(fileEntity4), is(true));

        FileEntity resultVo5 = fileDao.get(tableName, fileEntity5.getFile_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(fileEntity5), is(true));

        // modify
        int next_deleted = MDV.YES;
        String next_file_url = "/afdafdasfa12312fdsafsadfsdaf.png";
        FileEntity updateVo = new FileEntity();
        updateVo.init();
        updateVo.setDeleted(next_deleted);
        updateVo.setFile_url(next_file_url);
        fileDao.modify(tableName, updateVo, fileEntity1.getFile_srl(), null);

        // select
        FileEntity resultVo6 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(fileEntity1), is(false));
        assertThat(resultVo6.getDeleted(), is(next_deleted));
        assertThat(resultVo6.getFile_url(), is(next_file_url));

        // select one
        FileEntity resultVo7 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NO);
        assertThat(resultVo7, is(nullValue()));

        // delete
        fileDao.delete(tableName, fileEntity1.getFile_srl());

        FileEntity resultVo8 = fileDao.get(tableName, fileEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo8, is(nullValue()));
    }

    /**
     * push 메시지 사진 매핑 테이블 기본 테스트
     */
    @Test
    @Rollback
    public void pushMessagePicBasicTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_push_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_push_file");

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        pushMessageEntity1.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity1);
        assertThat(pushMessageEntity1.getPush_srl() > 0, is(true));

        pushMessageEntity2.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity2);
        assertThat(pushMessageEntity2.getPush_srl() > 0, is(true));

        pushMessageEntity3.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity3);
        assertThat(pushMessageEntity3.getPush_srl() > 0, is(true));

        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));
        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        // pre select list
        long count1 = pushMessagePicDao.count(MDV.NUSE, MDV.NUSE);
        List<PushMessagePicEntity> list1 = pushMessagePicDao.get(MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        pushMessagePicEntity1.setPush_srl(pushMessageEntity1.getPush_srl());
        pushMessagePicEntity1.setFile_srl(fileEntity1.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity1);

        pushMessagePicEntity2.setPush_srl(pushMessageEntity2.getPush_srl());
        pushMessagePicEntity2.setFile_srl(fileEntity2.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity2);

        pushMessagePicEntity3.setPush_srl(pushMessageEntity3.getPush_srl());
        pushMessagePicEntity3.setFile_srl(fileEntity3.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity3);

        // select list
        long count2 = pushMessagePicDao.count(MDV.NUSE, MDV.NUSE);
        assertThat(count1 + 3, is(count2));
        List<PushMessagePicEntity> list2 = pushMessagePicDao.get(MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        PushMessagePicEntity resultVo1 = pushMessagePicDao.get(pushMessageEntity1.getPush_srl(), memberPicEntity1.getFile_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(pushMessagePicEntity1), is(true));

        PushMessagePicEntity resultVo2 = pushMessagePicDao.get(pushMessageEntity2.getPush_srl(), memberPicEntity2.getFile_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(pushMessagePicEntity2), is(true));

        PushMessagePicEntity resultVo3 = pushMessagePicDao.get(pushMessageEntity3.getPush_srl(), memberPicEntity3.getFile_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(pushMessagePicEntity3), is(true));

        // delete pushMessagePic
        pushMessagePicDao.delete(pushMessagePicEntity1.getPush_srl(), pushMessagePicEntity1.getFile_srl());

        // select one
        PushMessagePicEntity resultVo4 = pushMessagePicDao.get(pushMessagePicEntity1.getPush_srl(),
                pushMessagePicEntity1.getFile_srl());
        assertThat(resultVo4, is(nullValue()));

        // delete push message
        pushMessageDao.delete(pushMessageEntity2.getPush_srl());

        // select one
        PushMessagePicEntity resultVo5 = pushMessagePicDao.get(pushMessagePicEntity2.getPush_srl(),
                pushMessagePicEntity2.getFile_srl());
        assertThat(resultVo5, is(nullValue()));

        // delete file
        fileDao.delete(tableName, fileEntity3.getFile_srl());

        // select one
        PushMessagePicEntity resultVo6 = pushMessagePicDao.get(pushMessagePicEntity3.getPush_srl(),
                pushMessagePicEntity3.getFile_srl());
        assertThat(resultVo6, is(nullValue()));
    }

    /**
     * push 메시지, 이미지 매핑 테이블을 이용해서 push 메시지의 파일 정보 가져오는 메소드 테스트
     */
    @Test
    @Rollback
    public void pushMessagePicAndFileTest() {
        // prepare data
        assertThat(confTblFile.containsKey("tbl_name_push_file"), is(true));
        String tableName = confTblFile.getProperty("tbl_name_push_file");

        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        pushMessageEntity1.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity1);
        assertThat(pushMessageEntity1.getPush_srl() > 0, is(true));

        pushMessageEntity2.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity2);
        assertThat(pushMessageEntity2.getPush_srl() > 0, is(true));

        pushMessageEntity3.setApp_srl(appEntity1.getApp_srl());
        pushMessageDao.add(pushMessageEntity3);
        assertThat(pushMessageEntity3.getPush_srl() > 0, is(true));

        fileEntity1.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity1);
        assertThat(fileEntity1.getFile_srl() > 0, is(true));

        fileEntity2.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity2);
        assertThat(fileEntity2.getFile_srl() > 0, is(true));

        fileEntity3.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity3);
        assertThat(fileEntity3.getFile_srl() > 0, is(true));

        fileEntity4.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity4);
        assertThat(fileEntity4.getFile_srl() > 0, is(true));

        fileEntity5.setDeleted(MDV.NO);
        fileDao.add(tableName, fileEntity5);
        assertThat(fileEntity5.getFile_srl() > 0, is(true));

        List<Long> push_srls = new ArrayList<Long>();

        // select empty List
        List<PushMessagePicEntity> list1 = pushMessagePicDao.getAndFile(push_srls, MDV.NUSE);
        assertThat(list1, is(notNullValue()));

        // select message1 and file. result nullValue
        PushMessagePicEntity resultVo1 = pushMessagePicDao.getAndFile(pushMessageEntity1.getPush_srl(), MDV.NUSE);
        assertThat(resultVo1, is(nullValue()));

        // insert message1 : file1
        pushMessagePicEntity1.setPush_srl(pushMessageEntity1.getPush_srl());
        pushMessagePicEntity1.setFile_srl(fileEntity1.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity1);

        // select message1 and files
        PushMessagePicEntity resultVo2 = pushMessagePicDao.getAndFile(pushMessageEntity1.getPush_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.getFileEntities().size(), is(1));
        assertThat(resultVo2.getFileEntities().get(0).equals(fileEntity1), is(true));

        // insert message1 : file2
        pushMessagePicEntity2.setPush_srl(pushMessageEntity1.getPush_srl());
        pushMessagePicEntity2.setFile_srl(fileEntity2.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity2);

        // select message1 and files
        PushMessagePicEntity resultVo3 = pushMessagePicDao.getAndFile(pushMessageEntity1.getPush_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.getFileEntities().size(), is(2));
        assertThat(resultVo3.getFileEntities().get(0).equals(fileEntity1), is(true));
        assertThat(resultVo3.getFileEntities().get(1).equals(fileEntity2), is(true));

        // select empty List
        List<PushMessagePicEntity> list2 = pushMessagePicDao.getAndFile(push_srls, MDV.NUSE);
        assertThat(list2, is(notNullValue()));

        // select message2 files. result nullValue
        PushMessagePicEntity resultVo4 = pushMessagePicDao.getAndFile(pushMessageEntity2.getPush_srl(), MDV.NUSE);
        assertThat(resultVo4, is(nullValue()));

        // insert message2 : file3
        pushMessagePicEntity3.setPush_srl(pushMessageEntity2.getPush_srl());
        pushMessagePicEntity3.setFile_srl(fileEntity3.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity3);

        // select message2 and files
        PushMessagePicEntity resultVo5 = pushMessagePicDao.getAndFile(pushMessageEntity2.getPush_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.getFileEntities().size(), is(1));
        assertThat(resultVo5.getFileEntities().get(0).equals(fileEntity3), is(true));

        // select empty List
        List<PushMessagePicEntity> list3 = pushMessagePicDao.getAndFile(push_srls, MDV.NUSE);
        assertThat(list3, is(notNullValue()));

        // select message3 files. result nullValue
        PushMessagePicEntity resultVo6 = pushMessagePicDao.getAndFile(pushMessageEntity3.getPush_srl(), MDV.NUSE);
        assertThat(resultVo6, is(nullValue()));

        // insert message3 : file4
        pushMessagePicEntity4.setPush_srl(pushMessageEntity3.getPush_srl());
        pushMessagePicEntity4.setFile_srl(fileEntity4.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity4);

        // insert message3 : file5
        pushMessagePicEntity5.setPush_srl(pushMessageEntity3.getPush_srl());
        pushMessagePicEntity5.setFile_srl(fileEntity5.getFile_srl());
        pushMessagePicDao.add(pushMessagePicEntity5);

        // select message3 and files
        PushMessagePicEntity resultVo7 = pushMessagePicDao.getAndFile(pushMessageEntity3.getPush_srl(), MDV.NUSE);
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.getFileEntities().size(), is(2));
        assertThat(resultVo7.getFileEntities().get(0).equals(fileEntity4), is(true));
        assertThat(resultVo7.getFileEntities().get(1).equals(fileEntity5), is(true));

        // set delete flag
        FileEntity updateVo = new FileEntity();
        updateVo.init();
        updateVo.setDeleted(MDV.YES);
        fileDao.modify(tableName, updateVo, fileEntity4.getFile_srl(), null);

        // select message3 and files
        PushMessagePicEntity resultVo8 = pushMessagePicDao.getAndFile(pushMessageEntity3.getPush_srl(), MDV.NO);
        assertThat(resultVo8, is(notNullValue()));
        assertThat(resultVo8.getFileEntities().size(), is(1));
        assertThat(resultVo8.getFileEntities().get(0).equals(fileEntity5), is(true));

        // set delete flag
        updateVo.init();
        updateVo.setDeleted(MDV.YES);
        fileDao.modify(tableName, updateVo, fileEntity5.getFile_srl(), null);

        // select message3 and files
        PushMessagePicEntity resultVo9 = pushMessagePicDao.getAndFile(pushMessageEntity3.getPush_srl(), MDV.NO);
        assertThat(resultVo9, is(nullValue()));

        push_srls.add(pushMessageEntity1.getPush_srl());

        // select list
        List<PushMessagePicEntity> list4 = pushMessagePicDao.getAndFile(push_srls, MDV.NUSE);
        assertThat(list4, is(notNullValue()));
        assertThat(list4.size(), is(1));
        assertThat(list4.get(0).equals(resultVo3), is(true));

        push_srls.add(pushMessageEntity2.getPush_srl());

        // select list
        List<PushMessagePicEntity> list5 = pushMessagePicDao.getAndFile(push_srls, MDV.NUSE);
        assertThat(list5, is(notNullValue()));
        assertThat(list5.size(), is(2));
        assertThat(list5.get(0).equals(resultVo5), is(true));
        assertThat(list5.get(1).equals(resultVo3), is(true));

        // select message3 and files
        PushMessagePicEntity resultVo10 = pushMessagePicDao.getAndFile(pushMessageEntity3.getPush_srl(), MDV.NUSE);
        assertThat(resultVo10, is(notNullValue()));
        assertThat(resultVo10.getFileEntities().size(), is(2));

        push_srls.add(pushMessageEntity3.getPush_srl());

        // select list
        List<PushMessagePicEntity> list6 = pushMessagePicDao.getAndFile(push_srls, MDV.NUSE);
        assertThat(list6, is(notNullValue()));
        assertThat(list6.size(), is(3));
        assertThat(list6.get(0).equals(resultVo10), is(true));
        assertThat(list6.get(1).equals(resultVo5), is(true));
        assertThat(list6.get(2).equals(resultVo3), is(true));

        // select list
        List<PushMessagePicEntity> list7 = pushMessagePicDao.getAndFile(push_srls, MDV.NO);
        assertThat(list7, is(notNullValue()));
        assertThat(list7.size(), is(2));
        assertThat(list7.get(0).equals(resultVo5), is(true));
        assertThat(list7.get(1).equals(resultVo3), is(true));
    }
}
