package com.ckstack.ckpush.message.impl;

import com.ckstack.ckpush.data.ras.RasEventBean;
import com.ckstack.ckpush.message.GcmApns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 6. 12..
 */
@Repository
public class GcmApnsImpl implements GcmApns {
    private final static Logger LOG = LoggerFactory.getLogger(GcmApnsImpl.class);

    @Resource(name = "redisGcmApnsTemplate")
    private RedisTemplate<String, Object> gcmApnsEventQueue;
    @Autowired
    protected Properties confCommon;

    @Override
    public void send(RasEventBean rasEventBean) {
        gcmApnsEventQueue.setKeySerializer(new StringRedisSerializer());
        gcmApnsEventQueue.setValueSerializer(new JacksonJsonRedisSerializer<>(RasEventBean.class));
        gcmApnsEventQueue.opsForList().rightPush(confCommon.getProperty("ras_event_queue"), rasEventBean);

        LOG.info("push gcm apn send message event to ras event queue");
    }
}
