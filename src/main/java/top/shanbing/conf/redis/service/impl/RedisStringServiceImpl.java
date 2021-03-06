package top.shanbing.conf.redis.service.impl;


import org.springframework.stereotype.Service;
import top.shanbing.conf.redis.service.RedisStringService;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisStringServiceImpl extends RedisKeyServiceImpl implements RedisStringService {


    public boolean set(String key, int cacheTimeSeconds, Object obj) {
        String stringValue = converter.toString(obj);
        redisTemplate.opsForValue().set(key, stringValue, cacheTimeSeconds, TimeUnit.SECONDS);

        return true;
    }


    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    public boolean set(String key, Object obj) {
        String stringValue = converter.toString(obj);
        redisTemplate.opsForValue().set(key, stringValue);
        return true;
    }


    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }


    public Long incrBy(String key, final long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }


    public <T> T get(String key, Class<T> clazz) {
        return this.get(key, (Type) clazz);
    }


    public <T> T get(String key, Type type) {
        return converter.fromString(this.get(key), type);
    }


    public void mSet(Map<String, Object> mSetKeyValueMap) {
        Map<String, String> stringMap = new HashMap<String, String>();

        for (Map.Entry<String, Object> entry : mSetKeyValueMap.entrySet()) {
            String string = converter.toString(entry.getValue());
            stringMap.put(entry.getKey(), string);
        }

        redisTemplate.opsForValue().multiSet(stringMap);
    }


    public List<String> mGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }


    public <T> List<T> mGet(Collection<String> keys, Class<T> clazz) {
        return this.mGet(keys, (Type) clazz);
    }


    public <T> List<T> mGet(Collection<String> keys, Type type) {
        List<T> list = new ArrayList<T>();
        List<String> valueList = this.mGet(keys);

        for (String key : valueList) {
            T t = converter.fromString(key, type);
            list.add(t);
        }
        return list;
    }

}
