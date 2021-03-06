package top.shanbing.conf.redis.service.impl;

import org.springframework.stereotype.Service;
import top.shanbing.conf.redis.service.RedisListService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisListServiceImpl extends RedisKeyServiceImpl implements RedisListService {


    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }


    public List<String> getListValues(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    public <T> List<T> getListValues(String key, long start, long end, Class<T> clazz) {
        return getListValues(key, start, end, (Type)clazz);
    }
    

    public <T> List<T> getListValues(String key, long start, long end, Type type) {
        List<T> objList = new ArrayList<T>();
        List<String> values = getListValues(key, start, end);

        for (String value : values) {
            T t = converter.fromString(value, type);
            objList.add(t);
        }
        return objList;
    }



    public long pushListValueFromRight(String key, Object value) {
        String sValue = converter.toString(value); 
        return redisTemplate.opsForList().rightPush(key, sValue);
    }


    public long pushListValueFromLeft(String key, Object value) {
        String sValue = converter.toString(value); 
        return redisTemplate.opsForList().leftPush(key, sValue);
    }


    public String popListValueFromLeft(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }


    public <T> T popListValueFromLeft(String key, Class<T> clazz) {
        return popListValueFromLeft(key, (Type)clazz);
    }
    

    public <T> T popListValueFromLeft(String key, Type type) {
        return converter.fromString(popListValueFromLeft(key), type);
    }


    public String popListValueFromRight(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }


    public <T> T popListValueFromRight(String key, Class<T> clazz) {
        return popListValueFromRight(key, (Type)clazz);
    }


    public <T> T popListValueFromRight(String key, Type type) {
        return converter.fromString(popListValueFromRight(key), type);
    }
    

    /**
     * 安全移除元素
     * @return
     */
    public <T> T rightPopAndLeftPush(String sourceKey,String destinationKey,long timeout,Class<T> clazz){
    	String resutStr=redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey,timeout,TimeUnit.SECONDS);
    	T result=converter.fromString(resutStr, clazz);
    	return result;
    }
}
