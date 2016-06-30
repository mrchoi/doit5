package com.ckstack.ckpush.dao.board;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.dao.mineral.DocumentAttachDao;
import com.ckstack.ckpush.dao.mineral.DocumentFileDao;
import com.ckstack.ckpush.dao.user.MemberDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.*;
import com.ckstack.ckpush.domain.mineral.DocumentAttachEntity;
import com.ckstack.ckpush.domain.mineral.DocumentFileEntity;
import com.ckstack.ckpush.domain.user.MemberEntity;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 7. 29..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class DocumentBoardDaoTest {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private DocumentTagDao documentTagDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private AppDao appDao;
    @Autowired
    private DocumentBoardDao documentBoardDao;
    @Autowired
    private DocumentCategoryDao documentCategoryDao;
    @Autowired
    private DocumentAttachDao documentAttachDao;
    @Autowired
    private DocumentFileDao documentFileDao;
    @Autowired
    private DocumentLinkDao documentLinkDao;

    private AppEntity appEntity1;
    private AppEntity appEntity2;

    private DocumentBoardEntity documentBoardEntity1;
    private DocumentBoardEntity documentBoardEntity2;
    private DocumentBoardEntity documentBoardEntity3;
    private DocumentBoardEntity documentBoardEntity4;
    private DocumentBoardEntity documentBoardEntity5;

    private DocumentCategoryEntity documentCategoryEntity1;
    private DocumentCategoryEntity documentCategoryEntity2;
    private DocumentCategoryEntity documentCategoryEntity3;
    private DocumentCategoryEntity documentCategoryEntity4;
    private DocumentCategoryEntity documentCategoryEntity5;

    private DocumentEntity documentEntity1;
    private DocumentEntity documentEntity2;
    private DocumentEntity documentEntity3;
    private DocumentEntity documentEntity4;
    private DocumentEntity documentEntity5;

    private MemberEntity memberEntity1;

    private TagEntity tagEntity1;
    private TagEntity tagEntity2;
    private TagEntity tagEntity3;
    private TagEntity tagEntity4;
    private TagEntity tagEntity5;

    private DocumentTagEntity documentTagEntity1;
    private DocumentTagEntity documentTagEntity2;
    private DocumentTagEntity documentTagEntity3;
    private DocumentTagEntity documentTagEntity4;
    private DocumentTagEntity documentTagEntity5;

    private DocumentAttachEntity documentAttachEntity1;
    private DocumentAttachEntity documentAttachEntity2;
    private DocumentAttachEntity documentAttachEntity3;
    private DocumentAttachEntity documentAttachEntity4;
    private DocumentAttachEntity documentAttachEntity5;

    private DocumentFileEntity documentFileEntity1;
    private DocumentFileEntity documentFileEntity2;
    private DocumentFileEntity documentFileEntity3;
    private DocumentFileEntity documentFileEntity4;
    private DocumentFileEntity documentFileEntity5;

    private DocumentLinkEntity documentLinkEntity1;
    private DocumentLinkEntity documentLinkEntity2;
    private DocumentLinkEntity documentLinkEntity3;
    private DocumentLinkEntity documentLinkEntity4;
    private DocumentLinkEntity documentLinkEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

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

        appEntity2 = new AppEntity();
        appEntity2.init();
        appEntity2.setApp_id(UUID.randomUUID().toString().replaceAll("-", ""));
        appEntity2.setApp_name("junit test app2");
        appEntity2.setApi_key(api_key1);
        appEntity2.setApp_version("1.0.1");
        appEntity2.setApi_secret(api_secret1);
        appEntity2.setEnabled(MDV.YES);
        appEntity2.setReg_terminal(0);
        appEntity2.setReg_push_terminal(0);
        appEntity2.setC_date(ltm);
        appEntity2.setU_date(ltm);

        documentBoardEntity1 = new DocumentBoardEntity();
        documentBoardEntity1.setBoard_name("test board1");
        documentBoardEntity1.setBoard_description("test board desc1");
        documentBoardEntity1.setEnabled(MDV.YES);
        documentBoardEntity1.setOpen_type(MDV.PUBLIC);
        documentBoardEntity1.setC_date(ltm);
        documentBoardEntity1.setU_date(ltm);

        documentBoardEntity2 = new DocumentBoardEntity();
        documentBoardEntity2.setBoard_name("test board2");
        documentBoardEntity2.setBoard_description("test board desc2");
        documentBoardEntity2.setEnabled(MDV.YES);
        documentBoardEntity2.setOpen_type(MDV.PUBLIC);
        documentBoardEntity2.setC_date(ltm);
        documentBoardEntity2.setU_date(ltm);

        documentBoardEntity3 = new DocumentBoardEntity();
        documentBoardEntity3.setBoard_name("test board3");
        documentBoardEntity3.setBoard_description("test board desc3");
        documentBoardEntity3.setEnabled(MDV.YES);
        documentBoardEntity3.setOpen_type(MDV.PUBLIC);
        documentBoardEntity3.setC_date(ltm);
        documentBoardEntity3.setU_date(ltm);

        documentBoardEntity4 = new DocumentBoardEntity();
        documentBoardEntity4.setBoard_name("test board4");
        documentBoardEntity4.setBoard_description("test board desc4");
        documentBoardEntity4.setEnabled(MDV.YES);
        documentBoardEntity4.setOpen_type(MDV.PUBLIC);
        documentBoardEntity4.setC_date(ltm);
        documentBoardEntity4.setU_date(ltm);

        documentBoardEntity5 = new DocumentBoardEntity();
        documentBoardEntity5.setBoard_name("test board5");
        documentBoardEntity5.setBoard_description("test board desc5");
        documentBoardEntity5.setEnabled(MDV.YES);
        documentBoardEntity5.setOpen_type(MDV.PRIVATE);
        documentBoardEntity5.setC_date(ltm);
        documentBoardEntity5.setU_date(ltm);

        documentCategoryEntity1 = new DocumentCategoryEntity();
        documentCategoryEntity1.setCategory_name("category test name1");
        documentCategoryEntity1.setCategory_description("category test desc1");
        documentCategoryEntity1.setCategory_type(MDV.NORMAL_CATEGORY);
        documentCategoryEntity1.setEnabled(MDV.YES);
        documentCategoryEntity1.setOpen_type(MDV.PUBLIC);
        documentCategoryEntity1.setC_date(ltm);
        documentCategoryEntity1.setU_date(ltm);

        documentCategoryEntity2 = new DocumentCategoryEntity();
        documentCategoryEntity2.setCategory_name("category test name2");
        documentCategoryEntity2.setCategory_description("category test desc2");
        documentCategoryEntity2.setCategory_type(MDV.NORMAL_CATEGORY);
        documentCategoryEntity2.setEnabled(MDV.YES);
        documentCategoryEntity2.setOpen_type(MDV.PUBLIC);
        documentCategoryEntity2.setC_date(ltm);
        documentCategoryEntity2.setU_date(ltm);

        documentCategoryEntity3 = new DocumentCategoryEntity();
        documentCategoryEntity3.setCategory_name("category test name3");
        documentCategoryEntity3.setCategory_description("category test desc3");
        documentCategoryEntity3.setCategory_type(MDV.NORMAL_CATEGORY);
        documentCategoryEntity3.setEnabled(MDV.YES);
        documentCategoryEntity3.setOpen_type(MDV.PUBLIC);
        documentCategoryEntity3.setC_date(ltm);
        documentCategoryEntity3.setU_date(ltm);

        documentCategoryEntity4 = new DocumentCategoryEntity();
        documentCategoryEntity4.setCategory_name("category test name4");
        documentCategoryEntity4.setCategory_description("category test desc4");
        documentCategoryEntity4.setCategory_type(MDV.NORMAL_CATEGORY);
        documentCategoryEntity4.setEnabled(MDV.YES);
        documentCategoryEntity4.setOpen_type(MDV.PUBLIC);
        documentCategoryEntity4.setC_date(ltm);
        documentCategoryEntity4.setU_date(ltm);

        documentCategoryEntity5 = new DocumentCategoryEntity();
        documentCategoryEntity5.setCategory_name("category test name5");
        documentCategoryEntity5.setCategory_description("category test desc5");
        documentCategoryEntity5.setCategory_type(MDV.NORMAL_CATEGORY);
        documentCategoryEntity5.setEnabled(MDV.YES);
        documentCategoryEntity5.setOpen_type(MDV.PUBLIC);
        documentCategoryEntity5.setC_date(ltm);
        documentCategoryEntity5.setU_date(ltm);

        documentEntity1 = new DocumentEntity();
        documentEntity1.setDocument_title("test title1");
        documentEntity1.setDocument_content("test document body1");
        documentEntity1.setRead_count(0);
        documentEntity1.setLike_count(0);
        documentEntity1.setBlame_count(0);
        documentEntity1.setComment_count(0);
        documentEntity1.setFile_count(0);
        documentEntity1.setOuter_link("http://m.daum1.net");
        documentEntity1.setSecret(MDV.NO);
        documentEntity1.setBlock(MDV.NO);
        documentEntity1.setAllow_comment(MDV.YES);
        documentEntity1.setAllow_notice(MDV.NO);
        documentEntity1.setTemplate_srl(MDV.NO);
        documentEntity1.setTemplate_extra("");
        documentEntity1.setMember_srl(1);
        documentEntity1.setUser_id("dhkim@ckstack.com");
        documentEntity1.setUser_name("dhkim name");
        documentEntity1.setNick_name("dhkim nick");
        documentEntity1.setEmail_address("dhkim@ckstack.com");
        documentEntity1.setDocument_password("");
        documentEntity1.setIpaddress("127.0.0.1");
        documentEntity1.setC_date(ltm);
        documentEntity1.setU_date(ltm);

        documentEntity2 = new DocumentEntity();
        documentEntity2.setDocument_title("test title2");
        documentEntity2.setDocument_content("test document body2");
        documentEntity2.setRead_count(0);
        documentEntity2.setLike_count(0);
        documentEntity2.setBlame_count(0);
        documentEntity2.setComment_count(0);
        documentEntity2.setFile_count(0);
        documentEntity2.setOuter_link("");
        documentEntity2.setSecret(MDV.NO);
        documentEntity2.setBlock(MDV.NO);
        documentEntity2.setAllow_comment(MDV.YES);
        documentEntity2.setAllow_notice(MDV.NO);
        documentEntity2.setTemplate_srl(MDV.NO);
        documentEntity2.setTemplate_extra("");
        documentEntity2.setMember_srl(1);
        documentEntity2.setUser_id("dhkim@ckstack.com");
        documentEntity2.setUser_name("dhkim name");
        documentEntity2.setNick_name("dhkim nick");
        documentEntity2.setEmail_address("dhkim@ckstack.com");
        documentEntity2.setDocument_password("");
        documentEntity2.setIpaddress("127.0.0.1");
        documentEntity2.setC_date(ltm);
        documentEntity2.setU_date(ltm);

        documentEntity3 = new DocumentEntity();
        documentEntity3.setDocument_title("test title3");
        documentEntity3.setDocument_content("test document body3");
        documentEntity3.setRead_count(0);
        documentEntity3.setLike_count(0);
        documentEntity3.setBlame_count(0);
        documentEntity3.setComment_count(0);
        documentEntity3.setFile_count(0);
        documentEntity3.setOuter_link("http://m.daum3.net");
        documentEntity3.setSecret(MDV.NO);
        documentEntity3.setBlock(MDV.NO);
        documentEntity3.setAllow_comment(MDV.YES);
        documentEntity3.setAllow_notice(MDV.NO);
        documentEntity3.setTemplate_srl(MDV.NO);
        documentEntity3.setTemplate_extra("");
        documentEntity3.setMember_srl(1);
        documentEntity3.setUser_id("dhkim@ckstack.com");
        documentEntity3.setUser_name("dhkim name");
        documentEntity3.setNick_name("dhkim nick");
        documentEntity3.setEmail_address("dhkim@ckstack.com");
        documentEntity3.setDocument_password("");
        documentEntity3.setIpaddress("127.0.0.1");
        documentEntity3.setC_date(ltm);
        documentEntity3.setU_date(ltm);

        documentEntity4 = new DocumentEntity();
        documentEntity4.setDocument_title("test title4");
        documentEntity4.setDocument_content("test document body4");
        documentEntity4.setRead_count(0);
        documentEntity4.setLike_count(0);
        documentEntity4.setBlame_count(0);
        documentEntity4.setComment_count(0);
        documentEntity4.setFile_count(0);
        documentEntity4.setOuter_link("http://m.daum4.net");
        documentEntity4.setSecret(MDV.NO);
        documentEntity4.setBlock(MDV.NO);
        documentEntity4.setAllow_comment(MDV.YES);
        documentEntity4.setAllow_notice(MDV.NO);
        documentEntity4.setTemplate_srl(MDV.NO);
        documentEntity4.setTemplate_extra("");
        documentEntity4.setMember_srl(1);
        documentEntity4.setUser_id("dhkim@ckstack.com");
        documentEntity4.setUser_name("dhkim name");
        documentEntity4.setNick_name("dhkim nick");
        documentEntity4.setEmail_address("dhkim@ckstack.com");
        documentEntity4.setDocument_password("");
        documentEntity4.setIpaddress("127.0.0.1");
        documentEntity4.setC_date(ltm);
        documentEntity4.setU_date(ltm);

        documentEntity5 = new DocumentEntity();
        documentEntity5.setDocument_title("test title5");
        documentEntity5.setDocument_content("test document body5");
        documentEntity5.setRead_count(0);
        documentEntity5.setLike_count(0);
        documentEntity5.setBlame_count(0);
        documentEntity5.setComment_count(0);
        documentEntity5.setFile_count(0);
        documentEntity5.setOuter_link("");
        documentEntity5.setSecret(MDV.NO);
        documentEntity5.setBlock(MDV.NO);
        documentEntity5.setAllow_comment(MDV.YES);
        documentEntity5.setAllow_notice(MDV.NO);
        documentEntity5.setTemplate_srl(MDV.NO);
        documentEntity5.setTemplate_extra("");
        documentEntity5.setMember_srl(1);
        documentEntity5.setUser_id("dhkim@ckstack.com");
        documentEntity5.setUser_name("dhkim name");
        documentEntity5.setNick_name("dhkim nick");
        documentEntity5.setEmail_address("dhkim@ckstack.com");
        documentEntity5.setDocument_password("");
        documentEntity5.setIpaddress("127.0.0.1");
        documentEntity5.setC_date(ltm);
        documentEntity5.setU_date(ltm);

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

        tagEntity1 = new TagEntity();
        tagEntity1.setTag_name("태그1");
        tagEntity1.setAdmin_tag(MDV.NO);
        tagEntity1.setC_date(ltm);
        tagEntity1.setU_date(ltm);

        tagEntity2 = new TagEntity();
        tagEntity2.setTag_name("태그2");
        tagEntity2.setAdmin_tag(MDV.NO);
        tagEntity2.setC_date(ltm);
        tagEntity2.setU_date(ltm);

        tagEntity3 = new TagEntity();
        tagEntity3.setTag_name("태그3");
        tagEntity3.setAdmin_tag(MDV.NO);
        tagEntity3.setC_date(ltm);
        tagEntity3.setU_date(ltm);

        tagEntity4 = new TagEntity();
        tagEntity4.setTag_name("어드민 태그1");
        tagEntity4.setAdmin_tag(MDV.YES);
        tagEntity4.setC_date(ltm);
        tagEntity4.setU_date(ltm);

        tagEntity5 = new TagEntity();
        tagEntity5.setTag_name("어드민 태그2");
        tagEntity5.setAdmin_tag(MDV.YES);
        tagEntity5.setC_date(ltm);
        tagEntity5.setU_date(ltm);

        documentTagEntity1 = new DocumentTagEntity();
        documentTagEntity1.setC_date(ltm);

        documentTagEntity2 = new DocumentTagEntity();
        documentTagEntity2.setC_date(ltm);

        documentTagEntity3 = new DocumentTagEntity();
        documentTagEntity3.setC_date(ltm);

        documentTagEntity4 = new DocumentTagEntity();
        documentTagEntity4.setC_date(ltm);

        documentTagEntity5 = new DocumentTagEntity();
        documentTagEntity5.setC_date(ltm);

        documentAttachEntity1 = new DocumentAttachEntity();
        documentAttachEntity1.setOrig_name("a1.jpg");
        documentAttachEntity1.setMime_type("application/json");
        documentAttachEntity1.setFile_size(100);
        documentAttachEntity1.setFile_path("/home/a/a1.jpg");
        documentAttachEntity1.setFile_url("/a1.jpg");
        documentAttachEntity1.setWidth(50);
        documentAttachEntity1.setHeight(50);
        documentAttachEntity1.setThumb_path("");
        documentAttachEntity1.setThumb_url("");
        documentAttachEntity1.setThumb_width(0);
        documentAttachEntity1.setThumb_height(0);
        documentAttachEntity1.setFile_comment("desc1");
        documentAttachEntity1.setMember_srl(MDV.NONE);
        documentAttachEntity1.setIpaddress("127.0.0.1");
        documentAttachEntity1.setDeleted(MDV.NO);
        documentAttachEntity1.setC_date(ltm);
        documentAttachEntity1.setU_date(ltm);

        documentAttachEntity2 = new DocumentAttachEntity();
        documentAttachEntity2.setOrig_name("a2.jpg");
        documentAttachEntity2.setMime_type("application/json");
        documentAttachEntity2.setFile_size(100);
        documentAttachEntity2.setFile_path("/home/a/a2.jpg");
        documentAttachEntity2.setFile_url("/a2.jpg");
        documentAttachEntity2.setWidth(50);
        documentAttachEntity2.setHeight(50);
        documentAttachEntity2.setThumb_path("");
        documentAttachEntity2.setThumb_url("");
        documentAttachEntity2.setThumb_width(0);
        documentAttachEntity2.setThumb_height(0);
        documentAttachEntity2.setFile_comment("desc2");
        documentAttachEntity2.setMember_srl(MDV.NONE);
        documentAttachEntity2.setIpaddress("127.0.0.1");
        documentAttachEntity2.setDeleted(MDV.NO);
        documentAttachEntity2.setC_date(ltm);
        documentAttachEntity2.setU_date(ltm);

        documentAttachEntity3 = new DocumentAttachEntity();
        documentAttachEntity3.setOrig_name("a3.jpg");
        documentAttachEntity3.setMime_type("application/json");
        documentAttachEntity3.setFile_size(100);
        documentAttachEntity3.setFile_path("/home/a/a3.jpg");
        documentAttachEntity3.setFile_url("/a3.jpg");
        documentAttachEntity3.setWidth(50);
        documentAttachEntity3.setHeight(50);
        documentAttachEntity3.setThumb_path("");
        documentAttachEntity3.setThumb_url("");
        documentAttachEntity3.setThumb_width(0);
        documentAttachEntity3.setThumb_height(0);
        documentAttachEntity3.setFile_comment("desc3");
        documentAttachEntity3.setMember_srl(MDV.NONE);
        documentAttachEntity3.setIpaddress("127.0.0.1");
        documentAttachEntity3.setDeleted(MDV.NO);
        documentAttachEntity3.setC_date(ltm);
        documentAttachEntity3.setU_date(ltm);

        documentAttachEntity4 = new DocumentAttachEntity();
        documentAttachEntity4.setOrig_name("a4.jpg");
        documentAttachEntity4.setMime_type("application/json");
        documentAttachEntity4.setFile_size(100);
        documentAttachEntity4.setFile_path("/home/a/a4.jpg");
        documentAttachEntity4.setFile_url("/a4.jpg");
        documentAttachEntity4.setWidth(50);
        documentAttachEntity4.setHeight(50);
        documentAttachEntity4.setThumb_path("");
        documentAttachEntity4.setThumb_url("");
        documentAttachEntity4.setThumb_width(0);
        documentAttachEntity4.setThumb_height(0);
        documentAttachEntity4.setFile_comment("desc4");
        documentAttachEntity4.setMember_srl(MDV.NONE);
        documentAttachEntity4.setIpaddress("127.0.0.1");
        documentAttachEntity4.setDeleted(MDV.NO);
        documentAttachEntity4.setC_date(ltm);
        documentAttachEntity4.setU_date(ltm);

        documentAttachEntity5 = new DocumentAttachEntity();
        documentAttachEntity5.setOrig_name("a5.jpg");
        documentAttachEntity5.setMime_type("application/json");
        documentAttachEntity5.setFile_size(100);
        documentAttachEntity5.setFile_path("/home/a/a5.jpg");
        documentAttachEntity5.setFile_url("/a5.jpg");
        documentAttachEntity5.setWidth(50);
        documentAttachEntity5.setHeight(50);
        documentAttachEntity5.setThumb_path("");
        documentAttachEntity5.setThumb_url("");
        documentAttachEntity5.setThumb_width(0);
        documentAttachEntity5.setThumb_height(0);
        documentAttachEntity5.setFile_comment("desc5");
        documentAttachEntity5.setMember_srl(MDV.NONE);
        documentAttachEntity5.setIpaddress("127.0.0.1");
        documentAttachEntity5.setDeleted(MDV.NO);
        documentAttachEntity5.setC_date(ltm);
        documentAttachEntity5.setU_date(ltm);

        documentFileEntity1 = new DocumentFileEntity();
        documentFileEntity1.setC_date(ltm);

        documentFileEntity2 = new DocumentFileEntity();
        documentFileEntity2.setC_date(ltm);

        documentFileEntity3 = new DocumentFileEntity();
        documentFileEntity3.setC_date(ltm);

        documentFileEntity4 = new DocumentFileEntity();
        documentFileEntity4.setC_date(ltm);

        documentFileEntity5 = new DocumentFileEntity();
        documentFileEntity5.setC_date(ltm);

        documentLinkEntity1 = new DocumentLinkEntity();
        documentLinkEntity1.setList_order(MDV.NONE);
        documentLinkEntity1.setC_date(ltm);

        documentLinkEntity2 = new DocumentLinkEntity();
        documentLinkEntity2.setList_order(MDV.NONE);
        documentLinkEntity2.setC_date(ltm);

        documentLinkEntity3 = new DocumentLinkEntity();
        documentLinkEntity3.setList_order(MDV.NONE);
        documentLinkEntity3.setC_date(ltm);

        documentLinkEntity4 = new DocumentLinkEntity();
        documentLinkEntity4.setList_order(MDV.NONE);
        documentLinkEntity4.setC_date(ltm);

        documentLinkEntity5 = new DocumentLinkEntity();
        documentLinkEntity5.setList_order(MDV.NONE);
        documentLinkEntity5.setC_date(ltm);

    }

    @Test
    @Rollback
    public void documentFileBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);
        assertThat(documentBoardEntity1.getBoard_srl() > 0, is(true));

        documentCategoryEntity1.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity1);
        assertThat(documentCategoryEntity1.getCategory_srl() > 0, is(true));

        documentEntity1.setApp_srl(appEntity1.getApp_srl());
        documentEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity1.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity1);
        assertThat(documentEntity1.getDocument_srl() > 0, is(true));

        documentEntity2.setApp_srl(appEntity1.getApp_srl());
        documentEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity2.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity2);
        assertThat(documentEntity2.getDocument_srl() > 0, is(true));

        documentEntity3.setApp_srl(appEntity1.getApp_srl());
        documentEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity3.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity3);
        assertThat(documentEntity3.getDocument_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity1);
        assertThat(documentAttachEntity1.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity2);
        assertThat(documentAttachEntity2.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity3);
        assertThat(documentAttachEntity3.getFile_srl() > 0, is(true));

        // pre select list
        long count1 = documentFileDao.count(MDV.NUSE, MDV.NUSE);
        List<DocumentFileEntity> list1 = documentFileDao.get(MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        documentFileEntity1.setDocument_srl(documentEntity1.getDocument_srl());
        documentFileEntity1.setFile_srl(documentAttachEntity1.getFile_srl());
        documentFileDao.add(documentFileEntity1);

        documentFileEntity2.setDocument_srl(documentEntity2.getDocument_srl());
        documentFileEntity2.setFile_srl(documentAttachEntity2.getFile_srl());
        documentFileDao.add(documentFileEntity2);

        documentFileEntity3.setDocument_srl(documentEntity3.getDocument_srl());
        documentFileEntity3.setFile_srl(documentAttachEntity3.getFile_srl());
        documentFileDao.add(documentFileEntity3);

        // select list
        long count2 = documentFileDao.count(MDV.NUSE, MDV.NUSE);
        assertThat(count1 + 3, is(count2));
        List<DocumentFileEntity> list2 = documentFileDao.get(MDV.NUSE, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        DocumentFileEntity resultVo1 = documentFileDao.get(documentFileEntity1.getDocument_srl(),
                documentFileEntity1.getFile_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentFileEntity1), is(true));

        DocumentFileEntity resultVo2 = documentFileDao.get(documentFileEntity2.getDocument_srl(),
                documentFileEntity2.getFile_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentFileEntity2), is(true));

        DocumentFileEntity resultVo3 = documentFileDao.get(documentFileEntity3.getDocument_srl(),
                documentFileEntity3.getFile_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentFileEntity3), is(true));

        // delete documentFile
        documentFileDao.delete(documentFileEntity1.getDocument_srl(), documentFileEntity1.getFile_srl(), null);

        // select one
        DocumentFileEntity resultVo4 = documentFileDao.get(documentFileEntity1.getDocument_srl(),
                documentFileEntity1.getFile_srl());
        assertThat(resultVo4, is(nullValue()));

        // delete document
        documentDao.delete(documentEntity2.getDocument_srl(), null);

        // select one
        DocumentFileEntity resultVo5 = documentFileDao.get(documentFileEntity2.getDocument_srl(),
                documentFileEntity2.getFile_srl());
        assertThat(resultVo5, is(nullValue()));

        // delete file
        documentAttachDao.delete(documentAttachEntity3.getFile_srl());

        // select one
        DocumentFileEntity resultVo6 = documentFileDao.get(documentFileEntity3.getDocument_srl(), documentFileEntity3.getFile_srl());
        assertThat(resultVo6, is(nullValue()));
    }

    /**
     * 게시물과 첨부파일의 매핑테이블에서 게시물 첨부 파일 정보를 구한다.(join 테스트)
     *
     */
    @Test
    @Rollback
    public void documentFileAndFileTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);
        assertThat(documentBoardEntity1.getBoard_srl() > 0, is(true));

        documentCategoryEntity1.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity1);
        assertThat(documentCategoryEntity1.getCategory_srl() > 0, is(true));

        documentEntity1.setApp_srl(appEntity1.getApp_srl());
        documentEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity1.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity1);
        assertThat(documentEntity1.getDocument_srl() > 0, is(true));

        documentEntity2.setApp_srl(appEntity1.getApp_srl());
        documentEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity2.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity2);
        assertThat(documentEntity2.getDocument_srl() > 0, is(true));

        documentEntity3.setApp_srl(appEntity1.getApp_srl());
        documentEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity3.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity3);
        assertThat(documentEntity3.getDocument_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity1);
        assertThat(documentAttachEntity1.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity2);
        assertThat(documentAttachEntity2.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity3);
        assertThat(documentAttachEntity3.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity4);
        assertThat(documentAttachEntity4.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity5);
        assertThat(documentAttachEntity5.getFile_srl() > 0, is(true));

        List<Long> document_srls = new ArrayList<>();

        // select empty list
        List<DocumentFileEntity> list1 = documentFileDao.getAndFile(document_srls, MDV.NUSE);
        assertThat(list1, is(notNullValue()));

        // select document1 and file. result nullValue
        DocumentFileEntity resultVo1 = documentFileDao.getAndFile(documentEntity1.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo1, is(nullValue()));

        // insert document1 : file1
        documentFileEntity1.setDocument_srl(documentEntity1.getDocument_srl());
        documentFileEntity1.setFile_srl(documentAttachEntity1.getFile_srl());
        documentFileDao.add(documentFileEntity1);

        // select document1 and files
        DocumentFileEntity resultVo2 = documentFileDao.getAndFile(documentEntity1.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.getDocumentAttachEntities().size(), is(1));
        assertThat(resultVo2.getDocumentAttachEntities().get(0).equals(documentAttachEntity1), is(true));

        // insert document1 : file2
        documentFileEntity2.setDocument_srl(documentEntity1.getDocument_srl());
        documentFileEntity2.setFile_srl(documentAttachEntity2.getFile_srl());
        documentFileDao.add(documentFileEntity2);

        // select document1 and files
        DocumentFileEntity resultVo3 = documentFileDao.getAndFile(documentEntity1.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.getDocumentAttachEntities().size(), is(2));
        assertThat(resultVo3.getDocumentAttachEntities().get(0).equals(documentAttachEntity1), is(true));
        assertThat(resultVo3.getDocumentAttachEntities().get(1).equals(documentAttachEntity2), is(true));

        // select empty list
        List<DocumentFileEntity> list2 = documentFileDao.getAndFile(document_srls, MDV.NUSE);
        assertThat(list2, is(notNullValue()));

        // select document2 files. result nullValue
        DocumentFileEntity resultVo4 = documentFileDao.getAndFile(documentEntity2.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo4, is(nullValue()));

        // insert member2 : file3
        documentFileEntity3.setDocument_srl(documentEntity2.getDocument_srl());
        documentFileEntity3.setFile_srl(documentAttachEntity3.getFile_srl());
        documentFileDao.add(documentFileEntity3);

        // select document2 and files
        DocumentFileEntity resultVo5 = documentFileDao.getAndFile(documentEntity2.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.getDocumentAttachEntities().size(), is(1));
        assertThat(resultVo5.getDocumentAttachEntities().get(0).equals(documentAttachEntity3), is(true));

        // select empty List
        List<DocumentFileEntity> list3 = documentFileDao.getAndFile(document_srls, MDV.NUSE);
        assertThat(list3, is(notNullValue()));

        DocumentFileEntity resultVo6 = documentFileDao.getAndFile(documentEntity3.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo6, is(nullValue()));

        // insert document3 : file4
        documentFileEntity4.setDocument_srl(documentEntity3.getDocument_srl());
        documentFileEntity4.setFile_srl(documentAttachEntity4.getFile_srl());
        documentFileDao.add(documentFileEntity4);

        // insert document3 : file5
        documentFileEntity5.setDocument_srl(documentEntity3.getDocument_srl());
        documentFileEntity5.setFile_srl(documentAttachEntity5.getFile_srl());
        documentFileDao.add(documentFileEntity5);

        // select document3 and files
        DocumentFileEntity resultVo7 = documentFileDao.getAndFile(documentEntity3.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.getDocumentAttachEntities().size(), is(2));
        assertThat(resultVo7.getDocumentAttachEntities().get(0).equals(documentAttachEntity4), is(true));
        assertThat(resultVo7.getDocumentAttachEntities().get(1).equals(documentAttachEntity5), is(true));

        // set delete flag
        DocumentAttachEntity updateVo = new DocumentAttachEntity();
        updateVo.init();
        updateVo.setDeleted(MDV.YES);
        documentAttachDao.modify(updateVo, documentAttachEntity4.getFile_srl(), null);

        // select document3 and files
        DocumentFileEntity resultVo8 = documentFileDao.getAndFile(documentEntity3.getDocument_srl(), MDV.NO);
        assertThat(resultVo8, is(notNullValue()));
        assertThat(resultVo8.getDocumentAttachEntities().size(), is(1));
        assertThat(resultVo8.getDocumentAttachEntities().get(0).equals(documentAttachEntity5), is(true));

        // set delete flag
        updateVo.init();
        updateVo.setDeleted(MDV.YES);
        documentAttachDao.modify(updateVo, documentAttachEntity5.getFile_srl(), null);

        // select document3 and files
        DocumentFileEntity resultVo9 = documentFileDao.getAndFile(documentEntity3.getDocument_srl(), MDV.NO);
        assertThat(resultVo9, is(nullValue()));

        document_srls.add(documentEntity1.getDocument_srl());

        // select list
        List<DocumentFileEntity> list4 = documentFileDao.getAndFile(document_srls, MDV.NUSE);
        assertThat(list4, is(notNullValue()));
        assertThat(list4.size(), is(1));
        assertThat(list4.get(0).equals(resultVo3), is(true));

        document_srls.add(documentEntity2.getDocument_srl());

        // select list
        List<DocumentFileEntity> list5 = documentFileDao.getAndFile(document_srls, MDV.NUSE);
        assertThat(list5, is(notNullValue()));
        assertThat(list5.size(), is(2));
        assertThat(list5.get(0).equals(resultVo5), is(true));
        assertThat(list5.get(1).equals(resultVo3), is(true));

        // select document3 and files
        DocumentFileEntity resultVo10 = documentFileDao.getAndFile(documentEntity3.getDocument_srl(), MDV.NUSE);
        assertThat(resultVo10, is(notNullValue()));
        assertThat(resultVo10.getDocumentAttachEntities().size(), is(2));

        document_srls.add(documentEntity3.getDocument_srl());

        // select list
        List<DocumentFileEntity> list6 = documentFileDao.getAndFile(document_srls, MDV.NUSE);
        assertThat(list6, is(notNullValue()));
        assertThat(list6.size(), is(3));
        assertThat(list6.get(0).equals(resultVo10), is(true));
        assertThat(list6.get(1).equals(resultVo5), is(true));
        assertThat(list6.get(2).equals(resultVo3), is(true));

        // select list
        List<DocumentFileEntity> list7 = documentFileDao.getAndFile(document_srls, MDV.NO);
        assertThat(list7, is(notNullValue()));
        assertThat(list7.size(), is(2));
        assertThat(list7.get(0).equals(resultVo5), is(true));
        assertThat(list7.get(1).equals(resultVo3), is(true));
    }

    /**
     * 게시물 파일 테이블 기본 테스트
     */
    @Test
    @Rollback
    public void documentAttachBasicTest() {
        // pre select list
        long count1 = documentAttachDao.count(null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        List<DocumentAttachEntity> list1 = documentAttachDao.get(null, MDV.NUSE, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        documentAttachDao.add(documentAttachEntity1);
        assertThat(documentAttachEntity1.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity2);
        assertThat(documentAttachEntity2.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity3);
        assertThat(documentAttachEntity3.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity4);
        assertThat(documentAttachEntity4.getFile_srl() > 0, is(true));

        documentAttachDao.add(documentAttachEntity5);
        assertThat(documentAttachEntity5.getFile_srl() > 0, is(true));

        // select list
        long count2 = documentAttachDao.count(null, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count1+5, is(count2));
        List<DocumentAttachEntity> list2 = documentAttachDao.get(null, MDV.NUSE, null, MDV.NUSE,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // select one
        DocumentAttachEntity resultVo1 = documentAttachDao.get(documentAttachEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentAttachEntity1), is(true));

        DocumentAttachEntity resultVo2 = documentAttachDao.get(documentAttachEntity2.getFile_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentAttachEntity2), is(true));

        DocumentAttachEntity resultVo3 = documentAttachDao.get(documentAttachEntity3.getFile_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentAttachEntity3), is(true));

        DocumentAttachEntity resultVo4 = documentAttachDao.get(documentAttachEntity4.getFile_srl(), MDV.NUSE);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(documentAttachEntity4), is(true));

        DocumentAttachEntity resultVo5 = documentAttachDao.get(documentAttachEntity5.getFile_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(documentAttachEntity5), is(true));

        // modify
        int next_deleted = MDV.YES;
        String next_file_url = "/afdafdasfa12312fdsafsadfsdaf.png";
        DocumentAttachEntity updateVo1 = new DocumentAttachEntity();
        updateVo1.init();
        updateVo1.setDeleted(next_deleted);
        updateVo1.setFile_url(next_file_url);
        documentAttachDao.modify(updateVo1, documentAttachEntity1.getFile_srl(), null);

        // select
        DocumentAttachEntity resultVo6 = documentAttachDao.get(documentAttachEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(documentAttachEntity1), is(false));
        assertThat(resultVo6.getDeleted(), is(next_deleted));
        assertThat(resultVo6.getFile_url(), is(next_file_url));

        // select one
        DocumentAttachEntity resultVo7 = documentAttachDao.get(documentAttachEntity1.getFile_srl(), MDV.NO);
        assertThat(resultVo7, is(nullValue()));

        // delete
        documentAttachDao.delete(documentAttachEntity1.getFile_srl());

        DocumentAttachEntity resultVo8 = documentAttachDao.get(documentAttachEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo8, is(nullValue()));
    }

    /**
     * document 와 tag mapping 테스트
     */
    @Test
    @Rollback
    public void documentTagBasicTest() {
        // prepare
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);
        assertThat(documentBoardEntity1.getBoard_srl() > 0, is(true));

        documentCategoryEntity1.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity1);
        assertThat(documentCategoryEntity1.getCategory_srl() > 0, is(true));

        documentEntity1.setApp_srl(appEntity1.getApp_srl());
        documentEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity1.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity1);
        assertThat(documentEntity1.getDocument_srl() > 0, is(true));

        documentEntity2.setApp_srl(appEntity1.getApp_srl());
        documentEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity2.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity2);
        assertThat(documentEntity2.getDocument_srl() > 0, is(true));

        tagEntity1.setApp_srl(appEntity1.getApp_srl());
        tagEntity1.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity1);
        assertThat(tagEntity1.getTag_srl() > 0, is(true));

        tagEntity2.setApp_srl(appEntity1.getApp_srl());
        tagEntity2.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity2);
        assertThat(tagEntity2.getTag_srl() > 0, is(true));

        tagEntity3.setApp_srl(appEntity1.getApp_srl());
        tagEntity3.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity3);
        assertThat(tagEntity3.getTag_srl() > 0, is(true));

        tagEntity4.setApp_srl(appEntity1.getApp_srl());
        tagEntity4.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity4);
        assertThat(tagEntity4.getTag_srl() > 0, is(true));

        tagEntity5.setApp_srl(appEntity1.getApp_srl());
        tagEntity5.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity5);
        assertThat(tagEntity5.getTag_srl() > 0, is(true));

        // prepare list
        List<DocumentTagEntity> list1 = documentTagDao.get(documentEntity1.getDocument_srl(), null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        assertThat(list1.size(), is(0));

        // insert document1, tag1
        documentTagEntity1.setDocument_srl(documentEntity1.getDocument_srl());
        documentTagEntity1.setTag_srl(tagEntity1.getTag_srl());
        documentTagDao.add(documentTagEntity1);

        // insert document1, tag2
        documentTagEntity2.setDocument_srl(documentEntity1.getDocument_srl());
        documentTagEntity2.setTag_srl(tagEntity2.getTag_srl());
        documentTagDao.add(documentTagEntity2);

        // insert document1, tag4
        documentTagEntity3.setDocument_srl(documentEntity1.getDocument_srl());
        documentTagEntity3.setTag_srl(tagEntity4.getTag_srl());
        documentTagDao.add(documentTagEntity3);

        // insert document2, tag3
        documentTagEntity4.setDocument_srl(documentEntity2.getDocument_srl());
        documentTagEntity4.setTag_srl(tagEntity3.getTag_srl());
        documentTagDao.add(documentTagEntity4);

        // insert document2, tag5
        documentTagEntity5.setDocument_srl(documentEntity2.getDocument_srl());
        documentTagEntity5.setTag_srl(tagEntity5.getTag_srl());
        documentTagDao.add(documentTagEntity5);

        // select list
        List<DocumentTagEntity> list2 = documentTagDao.get(documentEntity1.getDocument_srl(), null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        assertThat(list2.size(), is(3));

        List<DocumentTagEntity> list3 = documentTagDao.get(documentEntity2.getDocument_srl(), null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        assertThat(list3.size(), is(2));

        // select one
        DocumentTagEntity resultVo1 = documentTagDao.get(documentEntity1.getDocument_srl(), tagEntity1.getTag_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentTagEntity1), is(true));

        DocumentTagEntity resultVo2 = documentTagDao.get(documentEntity1.getDocument_srl(), tagEntity2.getTag_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentTagEntity2), is(true));

        DocumentTagEntity resultVo3 = documentTagDao.get(documentEntity1.getDocument_srl(), tagEntity4.getTag_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentTagEntity3), is(true));

        DocumentTagEntity resultVo4 = documentTagDao.get(documentEntity2.getDocument_srl(), tagEntity3.getTag_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(documentTagEntity4), is(true));

        DocumentTagEntity resultVo5 = documentTagDao.get(documentEntity2.getDocument_srl(), tagEntity5.getTag_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(documentTagEntity5), is(true));

        // delete
        documentTagDao.delete(documentEntity1.getDocument_srl(), tagEntity2.getTag_srl(), null);

        // select one
        DocumentTagEntity resultVo6 = documentTagDao.get(documentEntity1.getDocument_srl(), tagEntity2.getTag_srl());
        assertThat(resultVo6, is(nullValue()));

        // select list
        List<DocumentTagEntity> list4 = documentTagDao.get(documentEntity1.getDocument_srl(), null, MDV.NUSE,
                null, MDV.NUSE, MDV.NUSE);
        assertThat(list4.size(), is(2));

    }

    /**
     * 태그 기본 테스트
     */
    @Test
    @Rollback
    public void tagBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));

        // prepare list
        long count1 = tagDao.count(appEntity1.getApp_srl(), MDV.NUSE, null, MDV.NUSE);
        assertThat(count1, is(0L));
        List<TagEntity> list1 = tagDao.get(null, appEntity1.getApp_srl(), MDV.NUSE, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        tagEntity1.setApp_srl(appEntity1.getApp_srl());
        tagEntity1.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity1);
        assertThat(tagEntity1.getTag_srl() > 0, is(true));

        tagEntity2.setApp_srl(appEntity1.getApp_srl());
        tagEntity2.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity2);
        assertThat(tagEntity2.getTag_srl() > 0, is(true));

        tagEntity3.setApp_srl(appEntity1.getApp_srl());
        tagEntity3.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity3);
        assertThat(tagEntity3.getTag_srl() > 0, is(true));

        tagEntity4.setApp_srl(appEntity1.getApp_srl());
        tagEntity4.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity4);
        assertThat(tagEntity4.getTag_srl() > 0, is(true));

        tagEntity5.setApp_srl(appEntity1.getApp_srl());
        tagEntity5.setMember_srl(memberEntity1.getMember_srl());
        tagDao.add(tagEntity5);
        assertThat(tagEntity5.getTag_srl() > 0, is(true));

        // select list
        long count2 = tagDao.count(appEntity1.getApp_srl(), MDV.NUSE, null, MDV.NUSE);
        assertThat(count2, is(5L));
        List<TagEntity> list2 = tagDao.get(null, appEntity1.getApp_srl(), MDV.NUSE, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        long count3 = tagDao.count(appEntity1.getApp_srl(), MDV.NUSE, null, MDV.YES);
        assertThat(count3, is(2L));
        List<TagEntity> list3 = tagDao.get(null, appEntity1.getApp_srl(), MDV.NUSE, null, MDV.YES,
                null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));

        // select one
        TagEntity resultVo1 = tagDao.get(tagEntity1.getTag_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(tagEntity1), is(true));

        TagEntity resultVo2 = tagDao.get(tagEntity2.getTag_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(tagEntity2), is(true));

        TagEntity resultVo3 = tagDao.get(tagEntity3.getTag_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(tagEntity3), is(true));

        TagEntity resultVo4 = tagDao.get(tagEntity4.getTag_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(tagEntity4), is(true));

        TagEntity resultVo5 = tagDao.get(tagEntity5.getTag_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(tagEntity5), is(true));

        // update
        String next_tag_name = "가나다라 태그";
        TagEntity updateVo1 = new TagEntity();
        updateVo1.init();
        updateVo1.setTag_name(next_tag_name);
        tagDao.modify(updateVo1, tagEntity1.getTag_srl(), MDV.NUSE);

        // select one
        TagEntity resultVo6 = tagDao.get(tagEntity1.getTag_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(tagEntity1), is(false));
        assertThat(resultVo6.getTag_name(), is(next_tag_name));

        // delete
        tagDao.delete(tagEntity1.getTag_srl(), null);

        // select one
        TagEntity resultVo7 = tagDao.get(tagEntity1.getTag_srl());
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * 게시물 기본 테스트
     */
    @Test
    @Rollback
    public void documentBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);
        assertThat(documentBoardEntity1.getBoard_srl() > 0, is(true));

        documentCategoryEntity1.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity1);
        assertThat(documentCategoryEntity1.getCategory_srl() > 0, is(true));

        documentCategoryEntity2.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity2);
        assertThat(documentCategoryEntity2.getCategory_srl() > 0, is(true));

        // prepare list
        long count1 = documentDao.count(MDV.NUSE, MDV.NUSE, documentCategoryEntity1.getCategory_srl(),
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count1, is(0L));
        List<DocumentEntity> list1 = documentDao.get(null, MDV.NUSE, MDV.NUSE, null,
                documentCategoryEntity1.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        long count2 = documentDao.count(MDV.NUSE, MDV.NUSE, documentCategoryEntity2.getCategory_srl(),
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count2, is(0L));
        List<DocumentEntity> list2 = documentDao.get(null, MDV.NUSE, MDV.NUSE, null,
                documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // insert category1, document1
        documentEntity1.setApp_srl(appEntity1.getApp_srl());
        documentEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity1.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity1);
        assertThat(documentEntity1.getDocument_srl() > 0, is(true));

        // insert category1, document2
        documentEntity2.setApp_srl(appEntity1.getApp_srl());
        documentEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity2.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity2);
        assertThat(documentEntity2.getDocument_srl() > 0, is(true));

        // insert category1, document3
        documentEntity3.setApp_srl(appEntity1.getApp_srl());
        documentEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity3.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity3);
        assertThat(documentEntity3.getDocument_srl() > 0, is(true));

        // insert category2, document4
        documentEntity4.setApp_srl(appEntity1.getApp_srl());
        documentEntity4.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity4.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentDao.add(documentEntity4);
        assertThat(documentEntity4.getDocument_srl() > 0, is(true));

        // insert category2, document5
        documentEntity5.setApp_srl(appEntity1.getApp_srl());
        documentEntity5.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity5.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentDao.add(documentEntity5);
        assertThat(documentEntity5.getDocument_srl() > 0, is(true));

        // select one
        DocumentEntity resultVo1 = documentDao.get(documentEntity1.getDocument_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentEntity1), is(true));

        DocumentEntity resultVo2 = documentDao.get(documentEntity2.getDocument_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentEntity2), is(true));

        DocumentEntity resultVo3 = documentDao.get(documentEntity3.getDocument_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentEntity3), is(true));

        DocumentEntity resultVo4 = documentDao.get(documentEntity4.getDocument_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(documentEntity4), is(true));

        DocumentEntity resultVo5 = documentDao.get(documentEntity5.getDocument_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(documentEntity5), is(true));

        // select list
        long count3 = documentDao.count(MDV.NUSE, MDV.NUSE, documentCategoryEntity1.getCategory_srl(),
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count3, is(3L));
        List<DocumentEntity> list3 = documentDao.get(null, MDV.NUSE, MDV.NUSE, null,
                documentCategoryEntity1.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list3.size(), is(count3));

        long count4 = documentDao.count(MDV.NUSE, MDV.NUSE, documentCategoryEntity2.getCategory_srl(),
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count4, is(2L));
        List<DocumentEntity> list4 = documentDao.get(null, MDV.NUSE, MDV.NUSE, null,
                documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list4.size(), is(count4));

        // modify
        String next_title = "next title123";
        DocumentEntity updateVo1 = new DocumentEntity();
        updateVo1.init();
        updateVo1.setDocument_title(next_title);
        updateVo1.setBlock(MDV.YES);
        updateVo1.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentDao.modify(updateVo1, documentEntity1.getDocument_srl(), MDV.NUSE, MDV.NUSE);

        // select one
        DocumentEntity resultVo6 = documentDao.get(documentEntity1.getDocument_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(documentEntity1), is(false));
        assertThat(resultVo6.getDocument_title(), is(next_title));
        assertThat(resultVo6.getBlock(), is(MDV.YES));
        assertThat(resultVo6.getCategory_srl(), is(documentCategoryEntity2.getCategory_srl()));

        // select list
        long count5 = documentDao.count(MDV.NUSE, MDV.NUSE, documentCategoryEntity1.getCategory_srl(),
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count5, is(2L));
        List<DocumentEntity> list5 = documentDao.get(null, MDV.NUSE, MDV.NUSE, null,
                documentCategoryEntity1.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list5.size(), is(count5));

        long count6 = documentDao.count(MDV.NUSE, MDV.NUSE, documentCategoryEntity2.getCategory_srl(),
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count6, is(3L));
        List<DocumentEntity> list6 = documentDao.get(null, MDV.NUSE, MDV.NUSE, null,
                documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list6.size(), is(count6));

        // increase
        assertThat(documentEntity2.getRead_count(), is(0L));
        assertThat(documentEntity2.getLike_count(), is(0L));
        assertThat(documentEntity2.getBlame_count(), is(0L));
        assertThat(documentEntity2.getComment_count(), is(0));
        assertThat(documentEntity2.getFile_count(), is(0));

        documentDao.increase(documentEntity2.getDocument_srl(), true, true, true, true, true);
        DocumentEntity resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(1L));
        assertThat(resultVo7.getLike_count(), is(1L));
        assertThat(resultVo7.getBlame_count(), is(1L));
        assertThat(resultVo7.getComment_count(), is(1));
        assertThat(resultVo7.getFile_count(), is(1));

        documentDao.increase(documentEntity2.getDocument_srl(), true, false, false, false, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(1L));
        assertThat(resultVo7.getBlame_count(), is(1L));
        assertThat(resultVo7.getComment_count(), is(1));
        assertThat(resultVo7.getFile_count(), is(1));

        documentDao.increase(documentEntity2.getDocument_srl(), false, true, false, false, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(1L));
        assertThat(resultVo7.getComment_count(), is(1));
        assertThat(resultVo7.getFile_count(), is(1));

        documentDao.increase(documentEntity2.getDocument_srl(), false, false, true, false, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(2L));
        assertThat(resultVo7.getComment_count(), is(1));
        assertThat(resultVo7.getFile_count(), is(1));

        documentDao.increase(documentEntity2.getDocument_srl(), false, false, false, true, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(2L));
        assertThat(resultVo7.getComment_count(), is(2));
        assertThat(resultVo7.getFile_count(), is(1));

        documentDao.increase(documentEntity2.getDocument_srl(), false, false, false, false, true);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(2L));
        assertThat(resultVo7.getComment_count(), is(2));
        assertThat(resultVo7.getFile_count(), is(2));

        documentDao.increase(documentEntity2.getDocument_srl(), true, true, true, true, true);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(3L));
        assertThat(resultVo7.getLike_count(), is(3L));
        assertThat(resultVo7.getBlame_count(), is(3L));
        assertThat(resultVo7.getComment_count(), is(3));
        assertThat(resultVo7.getFile_count(), is(3));

        // decrease
        documentDao.decrease(documentEntity2.getDocument_srl(), true, false, false, false, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(3L));
        assertThat(resultVo7.getBlame_count(), is(3L));
        assertThat(resultVo7.getComment_count(), is(3));
        assertThat(resultVo7.getFile_count(), is(3));

        documentDao.decrease(documentEntity2.getDocument_srl(), false, true, false, false, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(3L));
        assertThat(resultVo7.getComment_count(), is(3));
        assertThat(resultVo7.getFile_count(), is(3));

        documentDao.decrease(documentEntity2.getDocument_srl(), false, false, true, false, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(2L));
        assertThat(resultVo7.getComment_count(), is(3));
        assertThat(resultVo7.getFile_count(), is(3));

        documentDao.decrease(documentEntity2.getDocument_srl(), false, false, false, true, false);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(2L));
        assertThat(resultVo7.getComment_count(), is(2));
        assertThat(resultVo7.getFile_count(), is(3));

        documentDao.decrease(documentEntity2.getDocument_srl(), false, false, false, false, true);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(2L));
        assertThat(resultVo7.getLike_count(), is(2L));
        assertThat(resultVo7.getBlame_count(), is(2L));
        assertThat(resultVo7.getComment_count(), is(2));
        assertThat(resultVo7.getFile_count(), is(2));

        documentDao.decrease(documentEntity2.getDocument_srl(), true, true, true, true, true);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(1L));
        assertThat(resultVo7.getLike_count(), is(1L));
        assertThat(resultVo7.getBlame_count(), is(1L));
        assertThat(resultVo7.getComment_count(), is(1));
        assertThat(resultVo7.getFile_count(), is(1));

        documentDao.decrease(documentEntity2.getDocument_srl(), true, true, true, true, true);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(0L));
        assertThat(resultVo7.getLike_count(), is(0L));
        assertThat(resultVo7.getBlame_count(), is(0L));
        assertThat(resultVo7.getComment_count(), is(0));
        assertThat(resultVo7.getFile_count(), is(0));

        documentDao.decrease(documentEntity2.getDocument_srl(), true, true, true, true, true);
        resultVo7 = documentDao.get(documentEntity2.getDocument_srl());

        assertThat(resultVo7.getRead_count(), is(0L));
        assertThat(resultVo7.getLike_count(), is(0L));
        assertThat(resultVo7.getBlame_count(), is(0L));
        assertThat(resultVo7.getComment_count(), is(0));
        assertThat(resultVo7.getFile_count(), is(0));

        // delete
        documentDao.delete(documentEntity1.getDocument_srl(), null);

        // select one
        DocumentEntity resultVo8 = documentDao.get(documentEntity1.getDocument_srl());
        assertThat(resultVo8, is(nullValue()));
    }

    /**
     * document board 기본 테스트
     */
    @Test
    @Rollback
    public void documentBoardBasicTest() {
        // parepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        // parepare list
        long count1 = documentBoardDao.count(appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE);
        assertThat(count1, is(0L));
        List<DocumentBoardEntity> list1 = documentBoardDao.get(null, appEntity1.getApp_srl(), null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        long count2 = documentBoardDao.count(appEntity2.getApp_srl(), null, MDV.NUSE, MDV.NUSE);
        assertThat(count2, is(0L));
        List<DocumentBoardEntity> list2 = documentBoardDao.get(null, appEntity2.getApp_srl(), null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // insert( to app1 )
        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);
        assertThat(documentBoardEntity1.getBoard_srl() > 0, is(true));

        // insert ( to app1 )
        documentBoardEntity2.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity2);
        assertThat(documentBoardEntity2.getBoard_srl() > 0, is(true));

        // insert ( to app1 )
        documentBoardEntity3.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity3);
        assertThat(documentBoardEntity3.getBoard_srl() > 0, is(true));

        // insert ( to app2 )
        documentBoardEntity4.setApp_srl(appEntity2.getApp_srl());
        documentBoardDao.add(documentBoardEntity4);
        assertThat(documentBoardEntity4.getBoard_srl() > 0, is(true));

        // insert ( to app2 )
        documentBoardEntity5.setApp_srl(appEntity2.getApp_srl());
        documentBoardDao.add(documentBoardEntity5);
        assertThat(documentBoardEntity5.getBoard_srl() > 0, is(true));

        // select one
        DocumentBoardEntity resultVo1 = documentBoardDao.get(documentBoardEntity1.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentBoardEntity1), is(true));

        DocumentBoardEntity resultVo2 = documentBoardDao.get(documentBoardEntity2.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentBoardEntity2), is(true));

        DocumentBoardEntity resultVo3 = documentBoardDao.get(documentBoardEntity3.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentBoardEntity3), is(true));

        DocumentBoardEntity resultVo4 = documentBoardDao.get(documentBoardEntity4.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(documentBoardEntity4), is(true));

        DocumentBoardEntity resultVo5 = documentBoardDao.get(documentBoardEntity5.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(documentBoardEntity5), is(true));

        // select list
        long count3 = documentBoardDao.count(appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE);
        assertThat(count3, is(3L));
        List<DocumentBoardEntity> list3 = documentBoardDao.get(null, appEntity1.getApp_srl(), null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list3.size(), is(count3));

        long count4 = documentBoardDao.count(appEntity2.getApp_srl(), null, MDV.NUSE, MDV.NUSE);
        assertThat(count4, is(2L));
        List<DocumentBoardEntity> list4 = documentBoardDao.get(null, appEntity2.getApp_srl(), null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list4.size(), is(count4));

        // modify
        DocumentBoardEntity updateVo1 = new DocumentBoardEntity();
        updateVo1.init();
        updateVo1.setApp_srl(appEntity2.getApp_srl());
        documentBoardDao.modify(updateVo1, documentBoardEntity1.getBoard_srl());

        // select one
        DocumentBoardEntity resultVo6 = documentBoardDao.get(documentBoardEntity1.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(documentBoardEntity1), is(false));
        assertThat(resultVo6.getApp_srl(), is(appEntity2.getApp_srl()));

        // select list
        long count5 = documentBoardDao.count(appEntity1.getApp_srl(), null, MDV.NUSE, MDV.NUSE);
        assertThat(count5, is(2L));
        List<DocumentBoardEntity> list5 = documentBoardDao.get(null, appEntity1.getApp_srl(), null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list5.size(), is(count5));

        long count6 = documentBoardDao.count(appEntity2.getApp_srl(), null, MDV.NUSE, MDV.NUSE);
        assertThat(count6, is(3L));
        List<DocumentBoardEntity> list6 = documentBoardDao.get(null, appEntity2.getApp_srl(), null,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list6.size(), is(count6));

        // delete
        documentBoardDao.delete(documentBoardEntity1.getBoard_srl(), null);

        // select one
        DocumentBoardEntity resultVo7 = documentBoardDao.get(documentBoardEntity1.getBoard_srl(), MDV.NUSE);
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * document category basic test
     */
    @Test
    @Rollback
    public void documentCategoryBasicTest() {
        // parepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);

        documentBoardEntity2.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity2);

        // prepare list
        long count1 = documentCategoryDao.count(MDV.NUSE, documentBoardEntity1.getBoard_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count1, is(0L));
        List<DocumentCategoryEntity> list1 = documentCategoryDao.get(null, MDV.NUSE,
                documentBoardEntity1.getBoard_srl(), null, null, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        long count2 = documentCategoryDao.count(MDV.NUSE, documentBoardEntity2.getBoard_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count2, is(0L));
        List<DocumentCategoryEntity> list2 = documentCategoryDao.get(null, MDV.NUSE,
                documentBoardEntity2.getBoard_srl(), null, null, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // insert app1, board1
        documentCategoryEntity1.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity1);
        assertThat(documentCategoryEntity1.getCategory_srl() > 0, is(true));

        // app1, board1
        documentCategoryEntity2.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity2);
        assertThat(documentCategoryEntity2.getCategory_srl() > 0, is(true));

        // app1, board1
        documentCategoryEntity3.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity3);
        assertThat(documentCategoryEntity3.getCategory_srl() > 0, is(true));

        // app1, board2
        documentCategoryEntity4.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity4.setBoard_srl(documentBoardEntity2.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity4);
        assertThat(documentCategoryEntity4.getCategory_srl() > 0, is(true));

        // app1, board2
        documentCategoryEntity5.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity5.setBoard_srl(documentBoardEntity2.getBoard_srl());
        documentCategoryDao.add(documentCategoryEntity5);
        assertThat(documentCategoryEntity5.getCategory_srl() > 0, is(true));

        // select one
        DocumentCategoryEntity resultVo1 = documentCategoryDao.get(documentCategoryEntity1.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentCategoryEntity1), is(true));

        DocumentCategoryEntity resultVo2 = documentCategoryDao.get(documentCategoryEntity2.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentCategoryEntity2), is(true));

        DocumentCategoryEntity resultVo3 = documentCategoryDao.get(documentCategoryEntity3.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentCategoryEntity3), is(true));

        DocumentCategoryEntity resultVo4 = documentCategoryDao.get(documentCategoryEntity4.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(documentCategoryEntity4), is(true));

        DocumentCategoryEntity resultVo5 = documentCategoryDao.get(documentCategoryEntity5.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(documentCategoryEntity5), is(true));

        // select list
        long count3 = documentCategoryDao.count(MDV.NUSE, documentBoardEntity1.getBoard_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count3, is(3L));
        List<DocumentCategoryEntity> list3 = documentCategoryDao.get(null, MDV.NUSE,
                documentCategoryEntity1.getBoard_srl(), null, null, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list3.size(), is(count3));

        long count4 = documentCategoryDao.count(MDV.NUSE, documentBoardEntity2.getBoard_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count4, is(2L));
        List<DocumentCategoryEntity> list4 = documentCategoryDao.get(null, MDV.NUSE,
                documentBoardEntity2.getBoard_srl(), null, null, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list4.size(), is(count4));

        // modify
        String next_category_name = "hey ho!";
        DocumentCategoryEntity updateVo = new DocumentCategoryEntity();
        updateVo.init();
        updateVo.setCategory_name(next_category_name);
        updateVo.setBoard_srl(documentBoardEntity2.getBoard_srl());
        documentCategoryDao.modify(updateVo, documentCategoryEntity1.getCategory_srl(), MDV.NUSE);

        // select one
        DocumentCategoryEntity resultVo6 = documentCategoryDao.get(documentCategoryEntity1.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(documentCategoryEntity1), is(false));
        assertThat(resultVo6.getCategory_name(), is(next_category_name));
        assertThat(resultVo6.getBoard_srl(), is(documentBoardEntity2.getBoard_srl()));

        // select list
        long count5 = documentCategoryDao.count(MDV.NUSE, documentBoardEntity1.getBoard_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count5, is(2L));
        List<DocumentCategoryEntity> list5 = documentCategoryDao.get(null, MDV.NUSE,
                documentCategoryEntity1.getBoard_srl(), null, null, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list5.size(), is(count5));

        long count6 = documentCategoryDao.count(MDV.NUSE, documentBoardEntity2.getBoard_srl(), null,
                MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(count6, is(3L));
        List<DocumentCategoryEntity> list6 = documentCategoryDao.get(null, MDV.NUSE,
                documentBoardEntity2.getBoard_srl(), null, null, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list6.size(), is(count6));

        // delete
        documentCategoryDao.delete(documentCategoryEntity1.getCategory_srl(), null);

        // select one
        DocumentCategoryEntity resultVo7 = documentCategoryDao.get(documentCategoryEntity1.getCategory_srl(), MDV.NUSE);
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * 게시물 링크 기본 테스트
     */
    @Test
    @Rollback
    public void documentLinkBasicTest() {
        // insert prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        documentBoardEntity1.setApp_srl(appEntity1.getApp_srl());
        documentBoardDao.add(documentBoardEntity1);
        assertThat(documentBoardEntity1.getBoard_srl() > 0, is(true));

        documentCategoryEntity1.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryEntity1.setCategory_type(MDV.NORMAL_CATEGORY);
        documentCategoryDao.add(documentCategoryEntity1);
        assertThat(documentCategoryEntity1.getCategory_srl() > 0, is(true));

        documentCategoryEntity2.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryEntity2.setCategory_type(MDV.LINK_CATEGORY);
        documentCategoryDao.add(documentCategoryEntity2);
        assertThat(documentCategoryEntity2.getCategory_srl() > 0, is(true));

        documentCategoryEntity3.setApp_srl(appEntity1.getApp_srl());
        documentCategoryEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentCategoryEntity3.setCategory_type(MDV.LINK_CATEGORY);
        documentCategoryDao.add(documentCategoryEntity3);
        assertThat(documentCategoryEntity3.getCategory_srl() > 0, is(true));

        documentEntity1.setApp_srl(appEntity1.getApp_srl());
        documentEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity1.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity1);
        assertThat(documentEntity1.getDocument_srl() > 0, is(true));

        documentEntity2.setApp_srl(appEntity1.getApp_srl());
        documentEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity2.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity2);
        assertThat(documentEntity2.getDocument_srl() > 0, is(true));

        documentEntity3.setApp_srl(appEntity1.getApp_srl());
        documentEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity3.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity3);
        assertThat(documentEntity3.getDocument_srl() > 0, is(true));

        documentEntity4.setApp_srl(appEntity1.getApp_srl());
        documentEntity4.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity4.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity4);
        assertThat(documentEntity4.getDocument_srl() > 0, is(true));

        documentEntity5.setApp_srl(appEntity1.getApp_srl());
        documentEntity5.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentEntity5.setCategory_srl(documentCategoryEntity1.getCategory_srl());
        documentDao.add(documentEntity5);
        assertThat(documentEntity5.getDocument_srl() > 0, is(true));

        // select list
        long count1 = documentLinkDao.count(appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), documentCategoryEntity2.getCategory_srl(), MDV.NUSE);
        assertThat(count1, is(0L));
        List<DocumentLinkEntity> list1 = documentLinkDao.get(null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null,
                MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        long count2 = documentLinkDao.countFullInfo(MDV.NUSE, null, appEntity1.getApp_srl(), documentBoardEntity1.getBoard_srl(),
                null, documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count2, is(0L));
        List<DocumentLinkEntity> list2 = documentLinkDao.getFullInfo(null, MDV.NUSE, null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // insert
        // link1 : document1
        documentLinkEntity1.setApp_srl(appEntity1.getApp_srl());
        documentLinkEntity1.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentLinkEntity1.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentLinkEntity1.setDocument_srl(documentEntity1.getDocument_srl());
        documentLinkDao.add(documentLinkEntity1);

        // link2 : document2
        documentLinkEntity2.setApp_srl(appEntity1.getApp_srl());
        documentLinkEntity2.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentLinkEntity2.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentLinkEntity2.setDocument_srl(documentEntity2.getDocument_srl());
        documentLinkDao.add(documentLinkEntity2);

        // link3 : document3
        documentLinkEntity3.setApp_srl(appEntity1.getApp_srl());
        documentLinkEntity3.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentLinkEntity3.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentLinkEntity3.setDocument_srl(documentEntity3.getDocument_srl());
        documentLinkDao.add(documentLinkEntity3);

        // link4 : document1
        documentLinkEntity4.setApp_srl(appEntity1.getApp_srl());
        documentLinkEntity4.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentLinkEntity4.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentLinkEntity4.setDocument_srl(documentEntity1.getDocument_srl());
        documentLinkDao.add(documentLinkEntity4);

        // link5 : document1
        documentLinkEntity5.setApp_srl(appEntity1.getApp_srl());
        documentLinkEntity5.setBoard_srl(documentBoardEntity1.getBoard_srl());
        documentLinkEntity5.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentLinkEntity5.setDocument_srl(documentEntity1.getDocument_srl());
        documentLinkDao.add(documentLinkEntity5);

        // select list
        long count3 = documentLinkDao.count(appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), documentCategoryEntity2.getCategory_srl(), MDV.NUSE);
        assertThat(count3, is(5L));
        List<DocumentLinkEntity> list3 = documentLinkDao.get(null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null,
                MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list3.size(), is(count3));

        for(DocumentLinkEntity documentLinkEntity : list3)
            assertThat(documentLinkEntity.getDocumentEntity(), is(nullValue()));

        long count4 = documentLinkDao.countFullInfo(MDV.NUSE, null, appEntity1.getApp_srl(), documentBoardEntity1.getBoard_srl(),
                null, documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count4, is(5L));
        List<DocumentLinkEntity> list4 = documentLinkDao.getFullInfo(null, MDV.NUSE, null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list4.size(), is(count4));

        for(int i=0 ; i<list4.size() ; i++) {
            assertThat(list4.get(i).getDocumentEntity(), is(notNullValue()));

            if(i == 0)      assertThat(list4.get(i).getDocumentEntity().equals(documentEntity1), is(true));
            else if(i == 1) assertThat(list4.get(i).getDocumentEntity().equals(documentEntity1), is(true));
            else if(i == 2) assertThat(list4.get(i).getDocumentEntity().equals(documentEntity3), is(true));
            else if(i == 3) assertThat(list4.get(i).getDocumentEntity().equals(documentEntity2), is(true));
            else if(i == 4) assertThat(list4.get(i).getDocumentEntity().equals(documentEntity1), is(true));
        }

        // select one
        DocumentLinkEntity resultVo1 = documentLinkDao.get(documentLinkEntity1.getDocument_link_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(documentLinkEntity1), is(true));
        assertThat(resultVo1.getDocumentEntity(), is(nullValue()));

        DocumentLinkEntity resultVo2 = documentLinkDao.get(documentLinkEntity2.getDocument_link_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(documentLinkEntity2), is(true));
        assertThat(resultVo2.getDocumentEntity(), is(nullValue()));

        DocumentLinkEntity resultVo3 = documentLinkDao.get(documentLinkEntity3.getDocument_link_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(documentLinkEntity3), is(true));
        assertThat(resultVo3.getDocumentEntity(), is(nullValue()));

        DocumentLinkEntity resultVo4 = documentLinkDao.get(documentLinkEntity4.getDocument_link_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(documentLinkEntity4), is(true));
        assertThat(resultVo4.getDocumentEntity(), is(nullValue()));

        DocumentLinkEntity resultVo5 = documentLinkDao.get(documentLinkEntity5.getDocument_link_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(documentLinkEntity5), is(true));
        assertThat(resultVo5.getDocumentEntity(), is(nullValue()));

        // select one ( full info )
        DocumentLinkEntity resultVo6 = documentLinkDao.getFullInfo(documentLinkEntity1.getDocument_link_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(documentLinkEntity1), is(false));
        assertThat(resultVo6.getDocumentEntity(), is(notNullValue()));
        assertThat(resultVo6.getDocumentEntity().equals(documentEntity1), is(true));

        DocumentLinkEntity resultVo7 = documentLinkDao.getFullInfo(documentLinkEntity2.getDocument_link_srl());
        assertThat(resultVo7, is(notNullValue()));
        assertThat(resultVo7.equals(documentLinkEntity2), is(false));
        assertThat(resultVo7.getDocumentEntity(), is(notNullValue()));
        assertThat(resultVo7.getDocumentEntity().equals(documentEntity2), is(true));

        DocumentLinkEntity resultVo8 = documentLinkDao.getFullInfo(documentLinkEntity3.getDocument_link_srl());
        assertThat(resultVo8, is(notNullValue()));
        assertThat(resultVo8.equals(documentLinkEntity3), is(false));
        assertThat(resultVo8.getDocumentEntity(), is(notNullValue()));
        assertThat(resultVo8.getDocumentEntity().equals(documentEntity3), is(true));

        DocumentLinkEntity resultVo9 = documentLinkDao.getFullInfo(documentLinkEntity4.getDocument_link_srl());
        assertThat(resultVo9, is(notNullValue()));
        assertThat(resultVo9.equals(documentLinkEntity4), is(false));
        assertThat(resultVo9.getDocumentEntity(), is(notNullValue()));
        assertThat(resultVo9.getDocumentEntity().equals(documentEntity1), is(true));

        DocumentLinkEntity resultVo10 = documentLinkDao.getFullInfo(documentLinkEntity5.getDocument_link_srl());
        assertThat(resultVo10, is(notNullValue()));
        assertThat(resultVo10.equals(documentLinkEntity5), is(false));
        assertThat(resultVo10.getDocumentEntity(), is(notNullValue()));
        assertThat(resultVo10.getDocumentEntity().equals(documentEntity1), is(true));

        // update
        DocumentLinkEntity updateVo = new DocumentLinkEntity();
        updateVo.init();
        updateVo.setCategory_srl(documentCategoryEntity3.getCategory_srl());
        documentLinkDao.modify(updateVo, documentLinkEntity3.getDocument_link_srl(), MDV.NUSE, MDV.NUSE);

        DocumentLinkEntity resultVo3_1 = documentLinkDao.get(documentLinkEntity3.getDocument_link_srl());
        assertThat(resultVo3_1, is(notNullValue()));
        assertThat(resultVo3_1.equals(documentLinkEntity3), is(false));
        assertThat(resultVo3_1.getDocumentEntity(), is(nullValue()));
        assertThat(resultVo3_1.getCategory_srl(), is(documentCategoryEntity3.getCategory_srl()));

        DocumentLinkEntity resultVo8_1 = documentLinkDao.getFullInfo(documentLinkEntity3.getDocument_link_srl());
        assertThat(resultVo8_1, is(notNullValue()));
        assertThat(resultVo8_1.equals(documentLinkEntity3), is(false));
        assertThat(resultVo8_1.getDocumentEntity(), is(notNullValue()));
        assertThat(resultVo8_1.getDocumentEntity().equals(documentEntity3), is(true));
        assertThat(resultVo8_1.getCategory_srl(), is(documentCategoryEntity3.getCategory_srl()));

        // select list
        long count5 = documentLinkDao.count(appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), documentCategoryEntity2.getCategory_srl(), MDV.NUSE);
        assertThat(count5, is(4L));
        List<DocumentLinkEntity> list5 = documentLinkDao.get(null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null,
                MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list5.size(), is(count5));

        for(DocumentLinkEntity documentLinkEntity : list5)
            assertThat(documentLinkEntity.getDocumentEntity(), is(nullValue()));

        long count6 = documentLinkDao.countFullInfo(MDV.NUSE, null, appEntity1.getApp_srl(), documentBoardEntity1.getBoard_srl(),
                null, documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count6, is(4L));
        List<DocumentLinkEntity> list6 = documentLinkDao.getFullInfo(null, MDV.NUSE, null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null, null,
                MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list6.size(), is(count6));

        for(int i=0 ; i<list6.size() ; i++) {
            assertThat(list6.get(i).getDocumentEntity(), is(notNullValue()));

            if(i == 0)      assertThat(list6.get(i).getDocumentEntity().equals(documentEntity1), is(true));
            else if(i == 1) assertThat(list6.get(i).getDocumentEntity().equals(documentEntity1), is(true));
            else if(i == 2) assertThat(list6.get(i).getDocumentEntity().equals(documentEntity2), is(true));
            else if(i == 3) assertThat(list6.get(i).getDocumentEntity().equals(documentEntity1), is(true));
        }

        // update. 다시 원복
        updateVo.init();
        updateVo.setCategory_srl(documentCategoryEntity2.getCategory_srl());
        documentLinkDao.modify(updateVo, documentLinkEntity3.getDocument_link_srl(), MDV.NUSE, MDV.NUSE);

        // document block
        DocumentEntity updateVo2 = new DocumentEntity();
        updateVo2.init();
        updateVo2.setBlock(MDV.YES);
        documentDao.modify(updateVo2, documentEntity1.getDocument_srl(), MDV.NUSE, MDV.NUSE);

        // select document link list. 게시물이 block 되었지만 링크만 보기 때문에 block 체크 안됨
        long count7 = documentLinkDao.count(appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), documentCategoryEntity2.getCategory_srl(), MDV.NUSE);
        assertThat(count7, is(5L));
        List<DocumentLinkEntity> list7 = documentLinkDao.get(null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null,
                MDV.NUSE, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list7.size(), is(count7));

        // 게시물이 block 되었기 때문에 block 체크 됨
        long count8 = documentLinkDao.countFullInfo(MDV.NUSE, null, appEntity1.getApp_srl(), documentBoardEntity1.getBoard_srl(),
                null, documentCategoryEntity2.getCategory_srl(), null, null, MDV.NUSE, null, MDV.NO, MDV.NUSE,
                MDV.NUSE, MDV.NUSE, null, null, null);
        assertThat(count8, is(2L));
        List<DocumentLinkEntity> list8 = documentLinkDao.getFullInfo(null, MDV.NUSE, null, appEntity1.getApp_srl(),
                documentBoardEntity1.getBoard_srl(), null, documentCategoryEntity2.getCategory_srl(), null, null,
                MDV.NUSE, null, MDV.NO, MDV.NUSE, MDV.NUSE, MDV.NUSE, null, null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list8.size(), is(count8));

        for(int i=0 ; i<list8.size() ; i++) {
            assertThat(list8.get(i).getDocumentEntity(), is(notNullValue()));

            if(i == 0)      assertThat(list8.get(i).getDocumentEntity().equals(documentEntity3), is(true));
            else if(i == 1) assertThat(list8.get(i).getDocumentEntity().equals(documentEntity2), is(true));
        }

        // delete
        documentLinkDao.delete(documentLinkEntity1.getDocument_link_srl(), null);

        // select one
        DocumentLinkEntity resultVo11 = documentLinkDao.get(documentLinkEntity1.getDocument_link_srl());
        assertThat(resultVo11, is(nullValue()));

        DocumentLinkEntity resultVo12 = documentLinkDao.getFullInfo(documentLinkEntity1.getDocument_link_srl());
        assertThat(resultVo12, is(nullValue()));
    }
}
