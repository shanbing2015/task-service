package top.shanbing.conf.redis.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**  
 * ClassName: RedisSortedSetService <br/>  
 * Function: redis有序集合操作接口. <br/>
 * date: 2015年6月26日 上午10:36:49 <br/>  
 *  
 * @author seven.zhao  
 * @version   
 * @since JDK 1.6  
 */
public interface RedisSortedSetService extends RedisKeyService {
    /**  
     * size:获取有序集合的元素个数. <br/>  
     * @author seven.zhao  
     * @param key
     * @return  
     * @since JDK 1.6  
     */
    public Long size(String key);
    /**  
     * addValuetoSortedSet:将一个对象添加到有序集合中. <br/>  
     * @author seven.zhao  
     * @param key 有序集合的key
     * @param value 要添加的对象
     * @param score 对象对应的分数（用来排序）
     * @return  
     * @since JDK 1.6  
     */
    public Boolean addValuetoSortedSet(String key, Object value, double score);

    /**
     * addValuetoSortedSet:将对象列表添加到有序集合中. <br/>
     * @author seven.zhao
     * @param key 有序集合的key
     * @param valueList 要添加的对象列表
     * @param rboClass 对象在redis中的实体类，需要实现ZSetOperations.TypedTuple<String>接口来实现插入时排序
     * @return
     * @since JDK 1.6
     */
    @SuppressWarnings("rawtypes")
	public Long addValueListToSortedSet(String key, List valueList, Class rboClass);
    
    /**  
     * removeValuesfromSortedSet:从有序集合中删除对象. <br/>   
     * @author seven.zhao  
     * @param key
     * @param values
     * @return  
     * @since JDK 1.6  
     */
    public Long removeValuesfromSortedSet(String key, Object... values);
    
    /**  
     * getValuesfromSortedSet:从有序集合中获取排名start到end的对象. <br/>   
     * @author seven.zhao  
     * @param key 有序集合的key
     * @param start 
     * @param end
     * @return  linkedHashSet 顺序与对象的排序一致，元素对象被序列化后的字符串
     * @since JDK 1.6  
     */
    public Set<String> getValuesfromSortedSet(String key, long start, long end);
    
    /**  
     * getValuesfromSortedSet:从有序集合中获取排名start到end的对象. <br/>   
     * @author seven.zhao  
     * @param key 有序集合的key
     * @param start 
     * @param end
     * @param clazz 对象的类型
     * @return  linkedHashSet 顺序与对象的排序一致，元素为对象
     * @since JDK 1.6  
     */
    public <T> Set<T> getValuesfromSortedSet(String key, long start, long end, Class<T> clazz);

    /**
     * getValuesfromSortedSet:从有序集合中获取排名start到end的对象. <br/>
     * @author seven.zhao
     * @param key 有序集合的key
     * @param start
     * @param end
     * @param type 对象的类型
     * @return  linkedHashSet 顺序与对象的排序一致，元素为对象
     * @since JDK 1.6
     */
    public <T> Set<T> getValuesfromSortedSet(String key, long start, long end, Type type);


    /**
     * getValuesfromSortedSet:从有序集合中获取排名start到end的对象. <br/>
     * @author seven.zhao
     * @param key 有序集合的key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @param clazz 对象的类型
     * @return  linkedHashSet 顺序与对象的排序一致，元素为对象
     * @since JDK 1.6
     */

    public <T> Set<T> getValuesfromSortedSetByScore(String key, double min, double max, int offset, int count, Class<T> clazz);


    /**
     * getValuesfromSortedSet:从有序集合中获取排名start到end的对象. <br/>
     * @author seven.zhao
     * @param key 有序集合的key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @param type 对象的类型
     * @return  linkedHashSet 顺序与对象的排序一致，元素为对象
     * @since JDK 1.6
     */

    public <T> Set<T> getValuesfromSortedSetByScore(String key, double min, double max, int offset, int count, Type type);



}
