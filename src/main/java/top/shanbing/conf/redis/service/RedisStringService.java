package top.shanbing.conf.redis.service;


import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**  
 * ClassName: RedisStringService <br/>  
 * Function: redis 字符串类型数据操作接口. <br/>
 */
public interface RedisStringService extends RedisKeyService{

     boolean set(String key, Object obj);
     boolean set(String key, int cacheTimeSeconds, Object obj);

    /**  incrBy:将指定key的值增加delta， delta可为负数. */
     Long incrBy(String key, final long delta);

    /** get:读取指定key的值并转换为指定类型 */
     <T> T get(String key, Class<T> clazz);
    /** get:读取指定key的值并转换为指定类型 .*/
     <T> T get(String key, Type type);
    String get(String key);

    /** mSet:批量设置键值对 */
     void mSet(Map<String, Object> mSetKeyValueMap);
    /** mGet:批量获取指定key的值，值为字符串未做数据转换. */
     List<String> mGet(Collection<String> keys);
    /** mGet:批量获取指定key的值，并转换为指定类型.*/
     <T> List<T> mGet(Collection<String> keys, Class<T> clazz) ;
    /** mGet:批量获取指定key的值，并转换为指定类型，使用于泛型类型. */
     <T> List<T> mGet(Collection<String> keys, Type type) ;
}
