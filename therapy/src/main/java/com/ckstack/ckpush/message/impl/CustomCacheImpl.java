package com.ckstack.ckpush.message.impl;

import com.ckstack.ckpush.data.cache.AccessTokenExtra;
import com.ckstack.ckpush.message.CustomCache;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by dhkim94 on 15. 8. 21..
 */
@Repository
public class CustomCacheImpl implements CustomCache {
    private final static Logger LOG = LoggerFactory.getLogger(CustomCacheImpl.class);

    @Resource(name = "redisCacheTemplate")
    private RedisTemplate<String, Object> cache;
    @Autowired
    protected Properties confCommon;


    @Override
    public void upsertMemberAccessToken(long memberSrl, int appSrl, String accessToken,
                                        AccessTokenExtra accessTokenExtra, int expire) {
        String key = confCommon.getProperty("redis_access_token_header") + "m" + memberSrl +
                ".a" + appSrl + "." + accessToken;

        cache.setKeySerializer(new StringRedisSerializer());
        cache.setValueSerializer(new JacksonJsonRedisSerializer<>(AccessTokenExtra.class));
        cache.opsForValue().set(key, accessTokenExtra);
        cache.expire(key, expire, TimeUnit.SECONDS);

        LOG.debug("upsert member's access_token to cache. key [" + key +
                "], value [" + accessTokenExtra.toString() + "]");
    }

    @Override
    public AccessTokenExtra getMemberAccessToken(long memberSrl, int appSrl) {
        String key = confCommon.getProperty("redis_access_token_header") + "m" + memberSrl +
                ".a" + appSrl + ".*";

        cache.setKeySerializer(new StringRedisSerializer());

        Set<String> keys = cache.keys(key);

        if(keys.size() <= 0) return null;

        cache.setValueSerializer(new JacksonJsonRedisSerializer<>(AccessTokenExtra.class));

        List<String> willRemoveKeys = new ArrayList<>();
        AccessTokenExtra accessTokenExtra = null;
        List<String> allKeys = new ArrayList<>(keys);

        for(String element : allKeys) {
            AccessTokenExtra info = (AccessTokenExtra) cache.opsForValue().get(element);

            if(accessTokenExtra == null) accessTokenExtra = info;
            else {
                if(accessTokenExtra.getToken_expire() < info.getToken_expire())
                    accessTokenExtra = info;
                else
                    willRemoveKeys.add(element);
            }
        }

        // 쓸데 없는 것은 삭제 한다.
        if(willRemoveKeys.size() > 0) {
            for(String element : willRemoveKeys) {
                cache.delete(element);
                LOG.debug("delete old access_token. access token key [" + element + "]");
            }
        }

        return accessTokenExtra;
    }

    @Override
    public AccessTokenExtra getMemberAccessToken(String accessToken) {
        String key = confCommon.getProperty("redis_access_token_header") + "*." + accessToken;
        cache.setKeySerializer(new StringRedisSerializer());

        Set<String> keys = cache.keys(key);
        if(keys.size() <= 0) return null;

        cache.setValueSerializer(new JacksonJsonRedisSerializer<>(AccessTokenExtra.class));

        List<String> willRemoveKeys = new ArrayList<>();
        AccessTokenExtra accessTokenExtra = null;
        List<String> allKeys = new ArrayList<>(keys);

        for(String element : allKeys) {
            AccessTokenExtra info = (AccessTokenExtra) cache.opsForValue().get(element);

            if(accessTokenExtra == null) accessTokenExtra = info;
            else {
                if(accessTokenExtra.getToken_expire() < info.getToken_expire())
                    accessTokenExtra = info;
                else
                    willRemoveKeys.add(element);
            }
        }

        // 쓸데 없는 것은 삭제 한다.
        if(willRemoveKeys.size() > 0) {
            for(String element : willRemoveKeys) {
                cache.delete(element);
                LOG.debug("delete old access_token. access token key [" + element + "]");
            }
        }

        return accessTokenExtra;
    }

    @Override
    public void modifyMemberEnabledOfMemberAccessToken(long memberSrl, int state) {
        String key = confCommon.getProperty("redis_access_token_header") + "m" + memberSrl + ".a*.*";
        cache.setKeySerializer(new StringRedisSerializer());

        Set<String> keys = cache.keys(key);
        if(keys.size() <= 0) {
            LOG.info("no key of modify member's enabled in access_token. key [" + key + "]");
            return;
        }

        cache.setValueSerializer(new JacksonJsonRedisSerializer<>(AccessTokenExtra.class));
        List<String> willRemoveKeys = new ArrayList<>();
        List<String> allKeys = new ArrayList<>(keys);
        int ltm = (int)(DateTime.now().getMillis() / 1000);

        for(String element : allKeys) {
            AccessTokenExtra info = (AccessTokenExtra) cache.opsForValue().get(element);

            if(ltm < info.getToken_expire()) {
                info.setEnabled(state);
                cache.opsForValue().set(element, info);
                cache.expire(element, info.getToken_expire() - ltm, TimeUnit.SECONDS);
                LOG.debug("modify member's enabled in access_token. key [" + element +
                        "], enabled [" + state + "]");
            } else
                willRemoveKeys.add(element);
        }

        // 쓸데 없는 것은 삭제 한다.
        if(willRemoveKeys.size() > 0) {
            for(String element : willRemoveKeys) {
                cache.delete(element);
                LOG.debug("delete old access_token. access token key [" + element + "]");
            }
        }
    }

    @Override
    public void deleteMemberAccessToken(long memberSrl, int appSrl) {
        String key = confCommon.getProperty("redis_access_token_header");

        if(memberSrl > 0)   key += "m" + memberSrl;
        else                key += "m*";

        if(appSrl > 0)      key += ".a" + appSrl + ".*";
        else                key += ".a*.*";

        cache.setKeySerializer(new StringRedisSerializer());

        Set<String> keys = cache.keys(key);
        if(keys.size() <= 0) {
            LOG.info("no key for delete member. key [" + key + "]");
            return;
        }

        List<String> allKeys = new ArrayList<>(keys);
        for(String element : allKeys) {
            cache.delete(element);
            LOG.debug("delete access_token. key [" + element + "]");
        }
    }

    @Override
    public void deleteMemberAccessToken(long memberSrl, int appSrl, String accessToken) {
        String key = confCommon.getProperty("redis_access_token_header") + "m" + memberSrl +
                ".a" + appSrl + "." + accessToken;

        cache.setKeySerializer(new StringRedisSerializer());
        cache.delete(key);
        LOG.debug("delete access_token. access token key [" + key + "]");
    }
}
