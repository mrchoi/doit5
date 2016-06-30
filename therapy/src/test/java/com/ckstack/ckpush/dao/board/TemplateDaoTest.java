package com.ckstack.ckpush.dao.board;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.dao.app.AppDao;
import com.ckstack.ckpush.domain.app.AppEntity;
import com.ckstack.ckpush.domain.board.AppTemplateEntity;
import com.ckstack.ckpush.domain.board.TemplateEntity;
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
import java.util.Map;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 7. 22..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class TemplateDaoTest {
    @Autowired
    private AppDao appDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private AppTemplateDao appTemplateDao;

    private AppEntity appEntity1;
    private AppEntity appEntity2;

    private TemplateEntity templateEntity1;
    private TemplateEntity templateEntity2;
    private TemplateEntity templateEntity3;
    private TemplateEntity templateEntity4;
    private TemplateEntity templateEntity5;

    private AppTemplateEntity appTemplateEntity1;
    private AppTemplateEntity appTemplateEntity2;
    private AppTemplateEntity appTemplateEntity3;
    private AppTemplateEntity appTemplateEntity4;
    private AppTemplateEntity appTemplateEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        templateEntity1 = new TemplateEntity();
        templateEntity1.setTemplate_title("template title1");
        templateEntity1.setTemplate_content("template content1");
        templateEntity1.setTemplate_description("template description1");
        templateEntity1.setC_date(ltm);
        templateEntity1.setU_date(ltm);

        templateEntity2 = new TemplateEntity();
        templateEntity2.setTemplate_title("template title2");
        templateEntity2.setTemplate_content("template content2");
        templateEntity2.setTemplate_description("template description2");
        templateEntity2.setC_date(ltm);
        templateEntity2.setU_date(ltm);

        templateEntity3 = new TemplateEntity();
        templateEntity3.setTemplate_title("template title3");
        templateEntity3.setTemplate_content("template content3");
        templateEntity3.setTemplate_description("template description3");
        templateEntity3.setC_date(ltm);
        templateEntity3.setU_date(ltm);

        templateEntity4 = new TemplateEntity();
        templateEntity4.setTemplate_title("template title4");
        templateEntity4.setTemplate_content("template content4");
        templateEntity4.setTemplate_description("template description4");
        templateEntity4.setC_date(ltm);
        templateEntity4.setU_date(ltm);

        templateEntity5 = new TemplateEntity();
        templateEntity5.setTemplate_title("template title5");
        templateEntity5.setTemplate_content("template content5");
        templateEntity5.setTemplate_description("template description5");
        templateEntity5.setC_date(ltm);
        templateEntity5.setU_date(ltm);

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

        appTemplateEntity1 = new AppTemplateEntity();
        appTemplateEntity1.setC_date(ltm);

        appTemplateEntity2 = new AppTemplateEntity();
        appTemplateEntity2.setC_date(ltm);

        appTemplateEntity3 = new AppTemplateEntity();
        appTemplateEntity3.setC_date(ltm);

        appTemplateEntity4 = new AppTemplateEntity();
        appTemplateEntity4.setC_date(ltm);

        appTemplateEntity5 = new AppTemplateEntity();
        appTemplateEntity5.setC_date(ltm);

    }

    /**
     * template basic test
     */
    @Test
    @Rollback
    public void templateBasicTest() {
        // pre select list
        long count1 = templateDao.count(MDV.NUSE, null);
        List<TemplateEntity> list1 = templateDao.get(null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        templateDao.add(templateEntity1);
        assertThat(templateEntity1.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity2);
        assertThat(templateEntity2.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity3);
        assertThat(templateEntity3.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity4);
        assertThat(templateEntity4.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity5);
        assertThat(templateEntity5.getTemplate_srl() > 0, is(true));

        // select one
        TemplateEntity resultVo1 = templateDao.get(templateEntity1.getTemplate_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(templateEntity1), is(true));

        TemplateEntity resultVo2 = templateDao.get(templateEntity2.getTemplate_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(templateEntity2), is(true));

        TemplateEntity resultVo3 = templateDao.get(templateEntity3.getTemplate_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(templateEntity3), is(true));

        TemplateEntity resultVo4 = templateDao.get(templateEntity4.getTemplate_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(templateEntity4), is(true));

        TemplateEntity resultVo5 = templateDao.get(templateEntity5.getTemplate_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(templateEntity5), is(true));

        // select list
        long count2 = templateDao.count(MDV.NUSE, null);
        assertThat(count1 + 5, is(count2));
        List<TemplateEntity> list2 = templateDao.get(null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat((long)list2.size(), is(count2));

        // modify
        String next_template_content = "{\"template\": \"content 111 222\"}";
        TemplateEntity updateVo = new TemplateEntity();
        updateVo.init();
        updateVo.setTemplate_content(next_template_content);
        templateDao.modify(updateVo, templateEntity1.getTemplate_srl());

        // select one
        TemplateEntity resultVo6 = templateDao.get(templateEntity1.getTemplate_srl());
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(templateEntity1), is(false));
        assertThat(resultVo6.getTemplate_content(), is(next_template_content));

        // delete
        templateDao.delete(templateEntity1.getTemplate_srl(), null);

        // select one
        TemplateEntity resultVo7 = templateDao.get(templateEntity1.getTemplate_srl());
        assertThat(resultVo7, is(nullValue()));
    }

    /**
     * basic app, template mapping test
     */
    @Test
    @Rollback
    public void appTemplateBasicTest() {
        // prepare data
        appDao.add(appEntity1);
        assertThat(appEntity1.getApp_srl() > 0, is(true));

        appDao.add(appEntity2);
        assertThat(appEntity2.getApp_srl() > 0, is(true));

        templateDao.add(templateEntity1);
        assertThat(templateEntity1.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity2);
        assertThat(templateEntity2.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity3);
        assertThat(templateEntity3.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity4);
        assertThat(templateEntity4.getTemplate_srl() > 0, is(true));

        templateDao.add(templateEntity5);
        assertThat(templateEntity5.getTemplate_srl() > 0, is(true));

        // prepare list
        long count1 = appTemplateDao.count(appEntity1.getApp_srl(), MDV.NUSE);
        assertThat(count1, is(0L));
        List<AppTemplateEntity> list1 = appTemplateDao.get(appEntity1.getApp_srl(), MDV.NUSE, null,
                MDV.NUSE, MDV.NUSE);
        assertThat((long)list1.size(), is(count1));

        // insert
        // app1, template1
        appTemplateEntity1.setApp_srl(appEntity1.getApp_srl());
        appTemplateEntity1.setTemplate_srl(templateEntity1.getTemplate_srl());
        appTemplateDao.add(appTemplateEntity1);

        // app1, template2
        appTemplateEntity2.setApp_srl(appEntity1.getApp_srl());
        appTemplateEntity2.setTemplate_srl(templateEntity2.getTemplate_srl());
        appTemplateDao.add(appTemplateEntity2);

        // app1, template3
        appTemplateEntity3.setApp_srl(appEntity1.getApp_srl());
        appTemplateEntity3.setTemplate_srl(templateEntity3.getTemplate_srl());
        appTemplateDao.add(appTemplateEntity3);

        // app2, template4
        appTemplateEntity4.setApp_srl(appEntity2.getApp_srl());
        appTemplateEntity4.setTemplate_srl(templateEntity4.getTemplate_srl());
        appTemplateDao.add(appTemplateEntity4);

        // app2, template5
        appTemplateEntity5.setApp_srl(appEntity2.getApp_srl());
        appTemplateEntity5.setTemplate_srl(templateEntity5.getTemplate_srl());
        appTemplateDao.add(appTemplateEntity5);

        // select one
        AppTemplateEntity resultVo1 = appTemplateDao.get(appEntity1.getApp_srl(), templateEntity1.getTemplate_srl());
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(appTemplateEntity1), is(true));

        AppTemplateEntity resultVo2 = appTemplateDao.get(appEntity1.getApp_srl(), templateEntity2.getTemplate_srl());
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(appTemplateEntity2), is(true));

        AppTemplateEntity resultVo3 = appTemplateDao.get(appEntity1.getApp_srl(), templateEntity3.getTemplate_srl());
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(appTemplateEntity3), is(true));

        AppTemplateEntity resultVo4 = appTemplateDao.get(appEntity2.getApp_srl(), templateEntity4.getTemplate_srl());
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(appTemplateEntity4), is(true));

        AppTemplateEntity resultVo5 = appTemplateDao.get(appEntity2.getApp_srl(), templateEntity5.getTemplate_srl());
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(appTemplateEntity5), is(true));

        // select list
        long count2 = appTemplateDao.count(appEntity1.getApp_srl(), MDV.NUSE);
        assertThat(count1 + 3, is(count2));
        List<AppTemplateEntity> list2 = appTemplateDao.get(appEntity1.getApp_srl(), MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        long count3 = appTemplateDao.count(appEntity2.getApp_srl(), MDV.NUSE);
        assertThat(2L, is(count3));
        List<AppTemplateEntity> list3 = appTemplateDao.get(appEntity2.getApp_srl(), MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list3.size(), is(count3));

        List<Integer> templateSrls = new ArrayList<>();
        templateSrls.add(templateEntity1.getTemplate_srl());
        templateSrls.add(templateEntity2.getTemplate_srl());
        templateSrls.add(templateEntity3.getTemplate_srl());
        templateSrls.add(templateEntity4.getTemplate_srl());
        templateSrls.add(templateEntity5.getTemplate_srl());

        List<Map<String, Object>> groupCounts = appTemplateDao.countTemplateUsingApp(templateSrls);

        assertThat(groupCounts.size(), is(5));
        for(Map<String, Object> map : groupCounts) {
            assertThat(map.containsKey("template_srl"), is(true));
            assertThat(map.containsKey("cnt"), is(true));
        }

        //System.out.println(groupCounts);

        // delete
        appTemplateDao.delete(appEntity1.getApp_srl(), null, MDV.NUSE);

        long count4 = appTemplateDao.count(appEntity1.getApp_srl(), MDV.NUSE);
        assertThat(0L, is(count4));
        List<AppTemplateEntity> list4 = appTemplateDao.get(appEntity1.getApp_srl(), MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list4.size(), is(count4));

        AppTemplateEntity resultVo6 = appTemplateDao.get(appEntity1.getApp_srl(), templateEntity2.getTemplate_srl());
        assertThat(resultVo6, is(nullValue()));
    }
}
