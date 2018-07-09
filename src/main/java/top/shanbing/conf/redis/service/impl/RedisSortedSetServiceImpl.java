package top.shanbing.conf.redis.service.impl;


import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import top.shanbing.conf.redis.service.RedisSortedSetService;
import top.shanbing.util.EntityConvertUtil;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class RedisSortedSetServiceImpl extends RedisKeyServiceImpl implements RedisSortedSetService {

    public Long size(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }


    public Boolean addValuetoSortedSet(String key, Object value, double score) {
        String json = converter.toString(value);
        return redisTemplate.opsForZSet().add(key, json, score);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Long addValueListToSortedSet(String key, List valueList,Class rboClazz) {

        if(valueList == null || valueList.size() == 0){
            return 0L;
        }

        //把list转成redisBOList
        List rboList = EntityConvertUtil.entityListToBOList(valueList,rboClazz);
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<ZSetOperations.TypedTuple<String>>(rboList);
        return redisTemplate.opsForZSet().add(key,tuples);
    }


    public Long removeValuesfromSortedSet(String key, Object... values) {
        String[] jsons = objsToStrings(values);
        return redisTemplate.opsForZSet().remove(key, (Object[])jsons);
    }


    public Set<String> getValuesfromSortedSet(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    public <T> Set<T> getValuesfromSortedSet(String key, long start, long end, Class<T> clazz) {     
        Set<String> sMemberSet = getValuesfromSortedSet(key, start, end);
        return stringSetToObjectSet(sMemberSet, (Type)clazz);
    }
    

    public <T> Set<T> getValuesfromSortedSet(String key, long start, long end, Type type) {     
        Set<String> sMemberSet = getValuesfromSortedSet(key, start, end);
        return stringSetToObjectSet(sMemberSet, type);
 
    }

    @Override
    public <T> Set<T> getValuesfromSortedSetByScore(String key, double min, double max, int offset, int count, Class<T> clazz) {
        Set<String> sMemberSet =  redisTemplate.opsForZSet().rangeByScore(key, min, max,offset,count);
        return stringSetToObjectSet(sMemberSet, (Type)clazz);

    }

    @Override
    public <T> Set<T> getValuesfromSortedSetByScore(String key, double min, double max, int offset, int count, Type type) {

        Set<String> sMemberSet =  redisTemplate.opsForZSet().rangeByScore(key, min, max,offset,count);
        return stringSetToObjectSet(sMemberSet, type);
    }

    private String[] objsToStrings(Object... object) {
        String[] jsons = new String[object.length];
        for (int i = 0; i < jsons.length; i++) {
            jsons[i] = converter.toString(object[i]);
        }
        return jsons;
    }

    private <T> Set<T> stringSetToObjectSet(Set<String> stringSet, Type type) {
        Set<T> objSet = new LinkedHashSet<T>();
        for (String member : stringSet) {
            T t = converter.fromString(member, type);
            objSet.add(t);
        }
        return objSet;
    }

}
