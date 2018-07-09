package top.shanbing.conf.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.shanbing.conf.redis.service.RedisKeyService;
import top.shanbing.conf.redis.stringconvertor.GsonStringConverter;
import top.shanbing.conf.redis.stringconvertor.StringConverter;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
public class RedisKeyServiceImpl implements RedisKeyService {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;
    
    protected StringConverter converter = new GsonStringConverter();
    

    public boolean expired(String Key, long timeInSeconds) {
        return redisTemplate.expire(Key, timeInSeconds, TimeUnit.SECONDS);
    }


    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }


    public boolean delete(String key) {
        redisTemplate.delete(key);
        return true;
    }


    public boolean delete(Collection<String> keys) {
        redisTemplate.delete(keys);
        return true;
    }
}
