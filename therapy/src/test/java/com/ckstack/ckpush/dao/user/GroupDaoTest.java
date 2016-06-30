package com.ckstack.ckpush.dao.user;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.user.GroupMemberEntity;
import com.ckstack.ckpush.domain.user.GroupEntity;
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

/**
 * Created by dhkim94 on 15. 3. 26..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class GroupDaoTest {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupMemberDao groupMemberDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private Properties confCommon;

    private GroupEntity groupEntity1;
    private GroupEntity groupEntity2;
    private GroupEntity groupEntity3;
    private GroupEntity groupEntity4;
    private GroupEntity groupEntity5;

    private MemberEntity memberEntity1;
    private MemberEntity memberEntity2;
    private MemberEntity memberEntity3;

    private GroupMemberEntity groupMemberEntity1;
    private GroupMemberEntity groupMemberEntity2;
    private GroupMemberEntity groupMemberEntity3;
    private GroupMemberEntity groupMemberEntity4;

    @Before
    public void setUp() {
        int ltm = (int) (DateTime.now().getMillis() / 1000);

        groupEntity1 = new GroupEntity();
        groupEntity1.setGroup_name("junit test group1");
        groupEntity1.setAllow_default(MDV.NO);
        groupEntity1.setAuthority(confCommon.getProperty("group_role_root"));
        groupEntity1.setDescription("junit test group1 description");
        groupEntity1.setC_date(ltm);
        groupEntity1.setU_date(ltm);

        groupEntity2 = new GroupEntity();
        groupEntity2.setGroup_name("junit test group2");
        groupEntity2.setAllow_default(MDV.NO);
        groupEntity2.setAuthority(confCommon.getProperty("group_role_user"));
        groupEntity2.setDescription("junit test group2 description");
        groupEntity2.setC_date(ltm);
        groupEntity2.setU_date(ltm);

        groupEntity3 = new GroupEntity();
        groupEntity3.setGroup_name("junit test group3");
        groupEntity3.setAllow_default(MDV.NO);
        groupEntity3.setAuthority(confCommon.getProperty("group_role_user"));
        groupEntity3.setDescription("junit test group3 description");
        groupEntity3.setC_date(ltm);
        groupEntity3.setU_date(ltm);

        groupEntity4 = new GroupEntity();
        groupEntity4.setGroup_name("junit test group4");
        groupEntity4.setAllow_default(MDV.NO);
        groupEntity4.setAuthority(confCommon.getProperty("group_role_user"));
        groupEntity4.setDescription("junit test group4 description");
        groupEntity4.setC_date(ltm);
        groupEntity4.setU_date(ltm);

        groupEntity5 = new GroupEntity();
        groupEntity5.setGroup_name("junit test group5");
        groupEntity5.setAllow_default(MDV.NO);
        groupEntity5.setAuthority(confCommon.getProperty("group_role_visitor"));
        groupEntity5.setDescription("junit test group5 description");
        groupEntity5.setC_date(ltm);
        groupEntity5.setU_date(ltm);

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

        groupMemberEntity1 = new GroupMemberEntity();
        groupMemberEntity1.setC_date(ltm);

        groupMemberEntity2 = new GroupMemberEntity();
        groupMemberEntity2.setC_date(ltm);

        groupMemberEntity3 = new GroupMemberEntity();
        groupMemberEntity3.setC_date(ltm);

        groupMemberEntity4 = new GroupMemberEntity();
        groupMemberEntity4.setC_date(ltm);
    }

    /**
     * tbl_group 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void groupBasicTest() {
        // pre select list
        long count1 = groupDao.count(MDV.NUSE, null, MDV.NUSE);
        List<GroupEntity> list1 = groupDao.get(null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity2);
        assertThat(groupEntity2.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity3);
        assertThat(groupEntity3.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity4);
        assertThat(groupEntity4.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity5);
        assertThat(groupEntity5.getGroup_srl() > 0, is(true));

        // select list
        long count2 = groupDao.count(MDV.NUSE, null, MDV.NUSE);
        assertThat(count1+5, is(count2));
        List<GroupEntity> list2 = groupDao.get(null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // select one
        GroupEntity resultVo1 = groupDao.get(groupEntity1.getGroup_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(groupEntity1), is(true));

        System.out.println(resultVo1.toString());
        System.out.println(groupEntity1);

        GroupEntity resultVo2 = groupDao.get(groupEntity2.getGroup_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(groupEntity2), is(true));

        GroupEntity resultVo3 = groupDao.get(groupEntity3.getGroup_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(groupEntity3), is(true));

        GroupEntity resultVo4 = groupDao.get(groupEntity4.getGroup_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(groupEntity4), is(true));

        GroupEntity resultVo5 = groupDao.get(groupEntity5.getGroup_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(groupEntity5), is(true));

        // update
        String next_group_name = "update group name 123";
        int next_allow_default = MDV.YES;
        int next_u_date = (int)(DateTime.now().getMillis() / 1000);
        GroupEntity updateVo = new GroupEntity();
        updateVo.init();
        updateVo.setGroup_name(next_group_name);
        updateVo.setAllow_default(next_allow_default);
        updateVo.setU_date(next_u_date);
        groupDao.modify(updateVo, groupEntity1.getGroup_srl());

        // select one
        GroupEntity resultVo6 = groupDao.get(groupEntity1.getGroup_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(groupEntity1), is(false));
        assertThat(resultVo6.getGroup_name(), is(next_group_name));
        assertThat(resultVo6.getAllow_default(), is(next_allow_default));
        assertThat(resultVo6.getU_date(), is(next_u_date));

        // delete
        groupDao.delete(groupEntity2.getGroup_srl());

        // select one
        GroupEntity resultVo7 = groupDao.get(groupEntity2.getGroup_srl());
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * tbl_update select one row 했을때 조건이 유효하지 않아서 list 로 검색될때 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneGroupTest() {
        // insert
        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity2);
        assertThat(groupEntity2.getGroup_srl() > 0, is(true));

        groupDao.get(MDV.NUSE);
    }

    /**
     * tbl_group row 업데이트시 조건이 유효한 값이 아닐 경우 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionUpdateGroupTest() {
        // insert
        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        // update
        String next_group_name = "update group name 123";
        int next_allow_default = MDV.YES;
        int next_u_date = (int)(DateTime.now().getMillis() / 1000);
        GroupEntity updateVo = new GroupEntity();
        updateVo.init();
        updateVo.setGroup_name(next_group_name);
        updateVo.setAllow_default(next_allow_default);
        updateVo.setU_date(next_u_date);
        groupDao.modify(updateVo, MDV.NUSE);
    }

    /**
     * tbl_group row 삭제시 조건이 유효한 값이 아닐 경우 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteGroupTest() {
        // insert
        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        // delete
        groupDao.delete(MDV.NUSE);
    }

    /**
     * tbl_group select list 테스트
     */
    @Test
    @Rollback
    public void groupListTest() {
        // prepare data
        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity2);
        assertThat(groupEntity2.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity3);
        assertThat(groupEntity3.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity4);
        assertThat(groupEntity4.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity5);
        assertThat(groupEntity5.getGroup_srl() > 0, is(true));

        // select using index indicator
        List<GroupEntity> list1 = groupDao.get(null, MDV.NUSE, null, MDV.NUSE, 0, 3);
        assertThat(list1, is(notNullValue()));
        assertThat(list1.size(), is(3));

        int lastGroupSrl = list1.get(2).getGroup_srl();

        List<GroupEntity> list2 = groupDao.get(null, MDV.NUSE, null, lastGroupSrl, 0, 3);
        assertThat(list2, is(notNullValue()));
        assertThat(list2.size() >= 2, is(true));

        List<GroupEntity> list3 = groupDao.get(null, MDV.NUSE, null, MDV.NUSE, 0, 5);
        assertThat(list3, is(notNullValue()));
        assertThat(list3.size(), is(5));
        assertThat(list3.get(3).equals(list2.get(0)), is(true));
        assertThat(list3.get(4).equals(list2.get(1)), is(true));

        // empty group_srls
        List<Integer> group_srls = new ArrayList<Integer>();
        List<GroupEntity> list4 = groupDao.get(group_srls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list4, is(notNullValue()));
        assertThat(list4.size() > 0, is(true));

        group_srls.add(groupEntity1.getGroup_srl());
        group_srls.add(groupEntity2.getGroup_srl());
        group_srls.add(groupEntity3.getGroup_srl());
        group_srls.add(groupEntity4.getGroup_srl());
        group_srls.add(groupEntity5.getGroup_srl());

        List<GroupEntity> list5 = groupDao.get(group_srls, MDV.NUSE, null, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat(list5.size(), is(5));

        // sorting 이 DESC 이기 때문에 꺼꾸로 되어 있음
        assertThat(list5.get(0).equals(groupEntity5), is(true));
        assertThat(list5.get(1).equals(groupEntity4), is(true));
        assertThat(list5.get(2).equals(groupEntity3), is(true));
        assertThat(list5.get(3).equals(groupEntity2), is(true));
        assertThat(list5.get(4).equals(groupEntity1), is(true));
    }

    /**
     * tbl_group_member 기본 CRUD 테스트
     */
    @Test
    @Rollback
    public void groupMemberBasicTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity2);
        assertThat(groupEntity2.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity3);
        assertThat(groupEntity3.getGroup_srl() > 0, is(true));

        // pre select list
        long count1 = groupMemberDao.count(MDV.NUSE, MDV.NUSE);
        List<GroupMemberEntity> list1 = groupMemberDao.get(MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        // member1 : group1
        // member1 : group2
        // member2 : grpup3
        // member3 : group3
        groupMemberEntity1.setMember_srl(memberEntity1.getMember_srl());
        groupMemberEntity1.setGroup_srl(groupEntity1.getGroup_srl());
        groupMemberDao.add(groupMemberEntity1);

        groupMemberEntity2.setMember_srl(memberEntity1.getMember_srl());
        groupMemberEntity2.setGroup_srl(groupEntity2.getGroup_srl());
        groupMemberDao.add(groupMemberEntity2);

        groupMemberEntity3.setMember_srl(memberEntity2.getMember_srl());
        groupMemberEntity3.setGroup_srl(groupEntity3.getGroup_srl());
        groupMemberDao.add(groupMemberEntity3);

        groupMemberEntity4.setMember_srl(memberEntity3.getMember_srl());
        groupMemberEntity4.setGroup_srl(groupEntity3.getGroup_srl());
        groupMemberDao.add(groupMemberEntity4);

        // select list
        long count2 = groupMemberDao.count(MDV.NUSE, MDV.NUSE);
        assertThat(count1 + 4, is(count2));
        List<GroupMemberEntity> list2 = groupMemberDao.get(MDV.NUSE, MDV.NUSE, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        GroupMemberEntity resultVo1 = groupMemberDao.get(groupMemberEntity1.getGroup_srl(), groupMemberEntity1.getMember_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(groupMemberEntity1), is(true));

        GroupMemberEntity resultVo2 = groupMemberDao.get(groupMemberEntity2.getGroup_srl(), groupMemberEntity2.getMember_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(groupMemberEntity2), is(true));

        GroupMemberEntity resultVo3 = groupMemberDao.get(groupMemberEntity3.getGroup_srl(), groupMemberEntity3.getMember_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(groupMemberEntity3), is(true));

        GroupMemberEntity resultVo4 = groupMemberDao.get(groupMemberEntity4.getGroup_srl(), groupMemberEntity4.getMember_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(groupMemberEntity4), is(true));

        // delete
        groupMemberDao.delete(groupMemberEntity1.getGroup_srl(), groupMemberEntity1.getMember_srl());

        // select one
        GroupMemberEntity resultVo5 = groupMemberDao.get(groupMemberEntity1.getGroup_srl(), groupMemberEntity1.getMember_srl());
        assertThat(resultVo5, is(nullValue()));

        // delete member
        GroupMemberEntity resultVo6 = groupMemberDao.get(groupMemberEntity2.getGroup_srl(), groupMemberEntity2.getMember_srl());
        assertThat(resultVo6, is(notNullValue()));

        memberDao.delete(memberEntity1.getMember_srl(), null, null);

        GroupMemberEntity resultVo7 = groupMemberDao.get(groupMemberEntity2.getGroup_srl(), groupMemberEntity2.getMember_srl());
        assertThat(resultVo7, is(nullValue()));

        // delete group
        GroupMemberEntity resultVo8 = groupMemberDao.get(groupMemberEntity3.getGroup_srl(), groupMemberEntity3.getMember_srl());
        assertThat(resultVo8, is(notNullValue()));

        GroupMemberEntity resultVo9 = groupMemberDao.get(groupMemberEntity4.getGroup_srl(), groupMemberEntity4.getMember_srl());
        assertThat(resultVo9, is(notNullValue()));

        groupDao.delete(groupEntity3.getGroup_srl());

        GroupMemberEntity resultVo10 = groupMemberDao.get(groupMemberEntity3.getGroup_srl(), groupMemberEntity3.getMember_srl());
        assertThat(resultVo10, is(nullValue()));

        GroupMemberEntity resultVo11 = groupMemberDao.get(groupMemberEntity4.getGroup_srl(), groupMemberEntity4.getMember_srl());
        assertThat(resultVo11, is(nullValue()));
    }

    /**
     * tbl_group_member select one 시에 조건이 유효하지 않은 경우 테스트
     */
    @Test(expected = MyBatisSystemException.class)
    @Rollback
    public void nonSelectOneGroupMemberTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity2);
        assertThat(groupEntity2.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity3);
        assertThat(groupEntity3.getGroup_srl() > 0, is(true));

        groupMemberEntity1.setMember_srl(memberEntity1.getMember_srl());
        groupMemberEntity1.setGroup_srl(groupEntity1.getGroup_srl());
        groupMemberDao.add(groupMemberEntity1);

        groupMemberEntity2.setMember_srl(memberEntity1.getMember_srl());
        groupMemberEntity2.setGroup_srl(groupEntity2.getGroup_srl());
        groupMemberDao.add(groupMemberEntity2);

        groupMemberEntity3.setMember_srl(memberEntity2.getMember_srl());
        groupMemberEntity3.setGroup_srl(groupEntity3.getGroup_srl());
        groupMemberDao.add(groupMemberEntity3);

        groupMemberEntity4.setMember_srl(memberEntity3.getMember_srl());
        groupMemberEntity4.setGroup_srl(groupEntity3.getGroup_srl());
        groupMemberDao.add(groupMemberEntity4);

        groupMemberDao.get(MDV.NUSE, MDV.NUSE);
    }

    /**
     * tbl_group_member row delete 시에 조건이 유효하지 않은 경우 테스트
     */
    @Test(expected = BadSqlGrammarException.class)
    @Rollback
    public void nonConditionDeleteGroupMemberTest() {
        // prepare data
        memberDao.add(memberEntity1);
        assertThat(memberEntity1.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity2);
        assertThat(memberEntity2.getMember_srl() > 0, is(true));
        memberDao.add(memberEntity3);
        assertThat(memberEntity3.getMember_srl() > 0, is(true));

        groupDao.add(groupEntity1);
        assertThat(groupEntity1.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity2);
        assertThat(groupEntity2.getGroup_srl() > 0, is(true));

        groupDao.add(groupEntity3);
        assertThat(groupEntity3.getGroup_srl() > 0, is(true));

        groupMemberEntity1.setMember_srl(memberEntity1.getMember_srl());
        groupMemberEntity1.setGroup_srl(groupEntity1.getGroup_srl());
        groupMemberDao.add(groupMemberEntity1);

        groupMemberEntity2.setMember_srl(memberEntity1.getMember_srl());
        groupMemberEntity2.setGroup_srl(groupEntity2.getGroup_srl());
        groupMemberDao.add(groupMemberEntity2);

        groupMemberEntity3.setMember_srl(memberEntity2.getMember_srl());
        groupMemberEntity3.setGroup_srl(groupEntity3.getGroup_srl());
        groupMemberDao.add(groupMemberEntity3);

        groupMemberEntity4.setMember_srl(memberEntity3.getMember_srl());
        groupMemberEntity4.setGroup_srl(groupEntity3.getGroup_srl());
        groupMemberDao.add(groupMemberEntity4);

        groupMemberDao.delete(MDV.NUSE, MDV.NUSE);
    }
}
