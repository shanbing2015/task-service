package top.shanbing.conf.redis.service;

import java.util.Collection;

/**  
 * Function: redis与键值相关的操作接口. <br/>
 */
public interface RedisKeyService {
     
    /** expired:设置某个key的时效时间.  */
    boolean expired(String Key, long timeInSeconds);
    
    /**  exists:判断指定key是否存在.   */
    boolean exists(String key);
    
    /**  delete:删除指定key.   */
     boolean delete(String key);
    
    /**  elete:批量删除key. * notice:redis 2.8.8 没有批量删除key的命令，所以这接口实际上一个一个地执行redis删除key的命令。 */
     boolean delete(Collection<String> keys);
    
}
