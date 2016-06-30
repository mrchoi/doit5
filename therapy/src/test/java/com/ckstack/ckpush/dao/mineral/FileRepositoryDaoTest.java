package com.ckstack.ckpush.dao.mineral;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.domain.mineral.FileRepositoryEntity;
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

/**
 * Created by dhkim94 on 15. 6. 29..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
@Transactional(value = "transactionManager")
@TransactionConfiguration(defaultRollback = false)
public class FileRepositoryDaoTest {
    @Autowired
    private FileRepositoryDao fileRepositoryDao;

    private FileRepositoryEntity fileRepositoryEntity1;
    private FileRepositoryEntity fileRepositoryEntity2;
    private FileRepositoryEntity fileRepositoryEntity3;
    private FileRepositoryEntity fileRepositoryEntity4;
    private FileRepositoryEntity fileRepositoryEntity5;

    @Before
    public void setUp() {
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        fileRepositoryEntity1 = new FileRepositoryEntity();
        fileRepositoryEntity2 = new FileRepositoryEntity();
        fileRepositoryEntity3 = new FileRepositoryEntity();
        fileRepositoryEntity4 = new FileRepositoryEntity();
        fileRepositoryEntity5 = new FileRepositoryEntity();

        fileRepositoryEntity1.setOrig_name("a1.jpg");
        fileRepositoryEntity1.setMime_type("application/json");
        fileRepositoryEntity1.setFile_size(100);
        fileRepositoryEntity1.setFile_path("/home/a/a1.jpg");
        fileRepositoryEntity1.setFile_url("/a1.jpg");
        fileRepositoryEntity1.setWidth(50);
        fileRepositoryEntity1.setHeight(50);
        fileRepositoryEntity1.setThumb_path("");
        fileRepositoryEntity1.setThumb_url("");
        fileRepositoryEntity1.setThumb_width(0);
        fileRepositoryEntity1.setThumb_height(0);
        fileRepositoryEntity1.setFile_comment("desc1");
        fileRepositoryEntity1.setUser_id("");
        fileRepositoryEntity1.setIpaddress("127.0.0.1");
        fileRepositoryEntity1.setDeleted(MDV.NO);
        fileRepositoryEntity1.setC_date(ltm);
        fileRepositoryEntity1.setU_date(ltm);

        fileRepositoryEntity2.setOrig_name("a2.jpg");
        fileRepositoryEntity2.setMime_type("application/json");
        fileRepositoryEntity2.setFile_size(200);
        fileRepositoryEntity2.setFile_path("/home/a/a2.jpg");
        fileRepositoryEntity2.setFile_url("/a2.jpg");
        fileRepositoryEntity2.setWidth(50);
        fileRepositoryEntity2.setHeight(50);
        fileRepositoryEntity2.setThumb_path("");
        fileRepositoryEntity2.setThumb_url("");
        fileRepositoryEntity2.setThumb_width(0);
        fileRepositoryEntity2.setThumb_height(0);
        fileRepositoryEntity2.setFile_comment("desc2");
        fileRepositoryEntity2.setUser_id("");
        fileRepositoryEntity2.setIpaddress("127.0.0.1");
        fileRepositoryEntity2.setDeleted(MDV.NO);
        fileRepositoryEntity2.setC_date(ltm);
        fileRepositoryEntity2.setU_date(ltm);

        fileRepositoryEntity3.setOrig_name("a3.jpg");
        fileRepositoryEntity3.setMime_type("application/json");
        fileRepositoryEntity3.setFile_size(300);
        fileRepositoryEntity3.setFile_path("/home/a/a3.jpg");
        fileRepositoryEntity3.setFile_url("/a3.jpg");
        fileRepositoryEntity3.setWidth(50);
        fileRepositoryEntity3.setHeight(50);
        fileRepositoryEntity3.setThumb_path("");
        fileRepositoryEntity3.setThumb_url("");
        fileRepositoryEntity3.setThumb_width(0);
        fileRepositoryEntity3.setThumb_height(0);
        fileRepositoryEntity3.setFile_comment("desc3");
        fileRepositoryEntity3.setUser_id("");
        fileRepositoryEntity3.setIpaddress("127.0.0.1");
        fileRepositoryEntity3.setDeleted(MDV.NO);
        fileRepositoryEntity3.setC_date(ltm);
        fileRepositoryEntity3.setU_date(ltm);

        fileRepositoryEntity4.setOrig_name("a4.jpg");
        fileRepositoryEntity4.setMime_type("application/json");
        fileRepositoryEntity4.setFile_size(400);
        fileRepositoryEntity4.setFile_path("/home/a/a4.jpg");
        fileRepositoryEntity4.setFile_url("/a4.jpg");
        fileRepositoryEntity4.setWidth(50);
        fileRepositoryEntity4.setHeight(50);
        fileRepositoryEntity4.setThumb_path("");
        fileRepositoryEntity4.setThumb_url("");
        fileRepositoryEntity4.setThumb_width(0);
        fileRepositoryEntity4.setThumb_height(0);
        fileRepositoryEntity4.setFile_comment("desc4");
        fileRepositoryEntity4.setUser_id("");
        fileRepositoryEntity4.setIpaddress("127.0.0.1");
        fileRepositoryEntity4.setDeleted(MDV.NO);
        fileRepositoryEntity4.setC_date(ltm);
        fileRepositoryEntity4.setU_date(ltm);

        fileRepositoryEntity5.setOrig_name("a5.jpg");
        fileRepositoryEntity5.setMime_type("application/json");
        fileRepositoryEntity5.setFile_size(500);
        fileRepositoryEntity5.setFile_path("/home/a/a5.jpg");
        fileRepositoryEntity5.setFile_url("/a5.jpg");
        fileRepositoryEntity5.setWidth(50);
        fileRepositoryEntity5.setHeight(50);
        fileRepositoryEntity5.setThumb_path("");
        fileRepositoryEntity5.setThumb_url("");
        fileRepositoryEntity5.setThumb_width(0);
        fileRepositoryEntity5.setThumb_height(0);
        fileRepositoryEntity5.setFile_comment("desc5");
        fileRepositoryEntity5.setUser_id("");
        fileRepositoryEntity5.setIpaddress("127.0.0.1");
        fileRepositoryEntity5.setDeleted(MDV.NO);
        fileRepositoryEntity5.setC_date(ltm);
        fileRepositoryEntity5.setU_date(ltm);

    }

    /**
     * file repository 기본 테스트
     */
    @Test
    @Rollback
    public void fileRepositoryBasicTest() {
        // pre select list
        long count1 = fileRepositoryDao.count(null, null, null, MDV.NUSE, MDV.NUSE);
        List<FileRepositoryEntity> list1 = fileRepositoryDao.get(null, MDV.NUSE, null,
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list1.size(), is(count1));

        // insert
        fileRepositoryDao.add(fileRepositoryEntity1);
        assertThat(fileRepositoryEntity1.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity2);
        assertThat(fileRepositoryEntity2.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity3);
        assertThat(fileRepositoryEntity3.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity4);
        assertThat(fileRepositoryEntity4.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity5);
        assertThat(fileRepositoryEntity5.getFile_srl() > 0, is(true));

        // select list
        long count2 = fileRepositoryDao.count(null, null, null, MDV.NUSE, MDV.NUSE);
        assertThat(count1+5, is(count2));
        List<FileRepositoryEntity> list2 = fileRepositoryDao.get(null, MDV.NUSE, null,
                null, MDV.NUSE, null, MDV.NUSE, MDV.NUSE);
        assertThat((long) list2.size(), is(count2));

        // select one
        FileRepositoryEntity resultVo1 = fileRepositoryDao.get(fileRepositoryEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo1, is(notNullValue()));
        assertThat(resultVo1.equals(fileRepositoryEntity1), is(true));

        FileRepositoryEntity resultVo2 = fileRepositoryDao.get(fileRepositoryEntity2.getFile_srl(), MDV.NUSE);
        assertThat(resultVo2, is(notNullValue()));
        assertThat(resultVo2.equals(fileRepositoryEntity2), is(true));

        FileRepositoryEntity resultVo3 = fileRepositoryDao.get(fileRepositoryEntity3.getFile_srl(), MDV.NUSE);
        assertThat(resultVo3, is(notNullValue()));
        assertThat(resultVo3.equals(fileRepositoryEntity3), is(true));

        FileRepositoryEntity resultVo4 = fileRepositoryDao.get(fileRepositoryEntity4.getFile_srl(), MDV.NUSE);
        assertThat(resultVo4, is(notNullValue()));
        assertThat(resultVo4.equals(fileRepositoryEntity4), is(true));

        FileRepositoryEntity resultVo5 = fileRepositoryDao.get(fileRepositoryEntity5.getFile_srl(), MDV.NUSE);
        assertThat(resultVo5, is(notNullValue()));
        assertThat(resultVo5.equals(fileRepositoryEntity5), is(true));

        // modify
        int next_deleted = MDV.YES;
        String next_file_url = "/afdafdasfa12312fdsafsadfsdaf.png";
        FileRepositoryEntity updateVo = new FileRepositoryEntity();
        updateVo.init();
        updateVo.setDeleted(next_deleted);
        updateVo.setFile_url(next_file_url);
        fileRepositoryDao.modify(updateVo, fileRepositoryEntity1.getFile_srl());

        // select
        FileRepositoryEntity resultVo6 = fileRepositoryDao.get(fileRepositoryEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo6, is(notNullValue()));
        assertThat(resultVo6.equals(fileRepositoryEntity1), is(false));
        assertThat(resultVo6.getDeleted(), is(next_deleted));
        assertThat(resultVo6.getFile_url(), is(next_file_url));

        // select one
        FileRepositoryEntity resultVo7 = fileRepositoryDao.get(fileRepositoryEntity1.getFile_srl(), MDV.NO);
        assertThat(resultVo7, is(nullValue()));

        // delete
        fileRepositoryDao.delete(fileRepositoryEntity1.getFile_srl());

        FileRepositoryEntity resultVo8 = fileRepositoryDao.get(fileRepositoryEntity1.getFile_srl(), MDV.NUSE);
        assertThat(resultVo8, is(nullValue()));
    }

    /**
     * file repository listing 테스트
     */
    @Test
    @Rollback
    public void fileRepositoryListTest() {
        // prepare data
        fileRepositoryDao.add(fileRepositoryEntity1);
        assertThat(fileRepositoryEntity1.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity2);
        assertThat(fileRepositoryEntity2.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity3);
        assertThat(fileRepositoryEntity3.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity4);
        assertThat(fileRepositoryEntity4.getFile_srl() > 0, is(true));

        fileRepositoryDao.add(fileRepositoryEntity5);
        assertThat(fileRepositoryEntity5.getFile_srl() > 0, is(true));

        // select using index indicator
        List<FileRepositoryEntity> list1 = fileRepositoryDao.get(null, MDV.NUSE, null, null, MDV.NUSE, null, 0, 3);
        assertThat(list1, is(notNullValue()));
        assertThat(list1.size(), is(3));

        long lastFileSrl = list1.get(2).getFile_srl();

        List<FileRepositoryEntity> list2 = fileRepositoryDao.get(null, MDV.NUSE, null, null, lastFileSrl, null, 0, 3);
        assertThat(list2, is(notNullValue()));
        assertThat(list2.size() >= 2, is(true));

        List<FileRepositoryEntity> list3 = fileRepositoryDao.get(null, MDV.NUSE, null, null, MDV.NUSE, null, 0, 5);
        assertThat(list3, is(notNullValue()));
        assertThat(list3.size(), is(5));
        assertThat(list3.get(3).equals(list2.get(0)), is(true));
        assertThat(list3.get(4).equals(list2.get(1)), is(true));

        // select file_srls
        List<Long> file_srls = new ArrayList<>();
        List<FileRepositoryEntity> list4 = fileRepositoryDao.get(file_srls, MDV.NUSE, null, null, MDV.NUSE, null, 0, 5);
        assertThat(list4, is(notNullValue()));
        assertThat(list4.size(), is(5));

        file_srls.add(fileRepositoryEntity2.getFile_srl());
        file_srls.add(fileRepositoryEntity5.getFile_srl());

        List<FileRepositoryEntity> list5 = fileRepositoryDao.get(file_srls, MDV.NUSE, null, null, MDV.NUSE, null, 0, 5);
        assertThat(list5, is(notNullValue()));
        assertThat(list5.size(), is(2));
        assertThat(list5.get(0).equals(fileRepositoryEntity5), is(true));
        assertThat(list5.get(1).equals(fileRepositoryEntity2), is(true));
    }

}
