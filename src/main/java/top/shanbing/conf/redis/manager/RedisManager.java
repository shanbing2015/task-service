package top.shanbing.conf.redis.manager;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Type;

/**
 * redis操作类
 *
 * <pre>
 * * 封装一些redis操作
 * *  所有的key都必须有过期时间，因此不会单独提供set方法
 * *  注意：原始数据类型也要用对象的形式
 * *      非原始数据（javabean，集合类等），使用jackson做序列化
 * *          注意jackson的序列化的一些特性，例如对null值的处理，尤其是list，map里面的null值元素
 * *      原始数据类型，支持String,int,byte,short,long,float,double,boolean,char
 * </pre>
 * *
 * @version 0.1
 *
 * Created by KangKai on 2017/3/29.
 */
public interface RedisManager {
    /**
     * <pre>     * 将对象缓存到redis
     * * 注意!!! 如果采用异步操作，必须确保对象是不会被修改的，否则缓存到redis的数据可能会和预期的不一样
     * *      例如下面的代码
     * *
     * *          ......
     * *          data.setXXX(xxx1)
     * *          setex(key,ttl,data,false);
     * *          data.setXXX(xxx2);
     * *          ......
     * *
     * *      这样，预期保存到redis的data，其xxx=xxx1，但是实际保存到redis的很有可能xxx=xxx2
     * * </pre>
     * *
     * * @param key key
     * * @param ttl 过期时间
     * * @param data 要缓存的对象
     * * @param sync 是否同步处理，true=同步处理,false=非同步处理(异步)
     */
    <T> void setex(String key, int ttl, T data, boolean sync);

    /** 从redis里取出缓存的bean或原始类型*/
    <T> T get(String key, Class<T> clazz);
    <T> T get2(String key, Class<T> clazz);


    /** 将从 reids 获取的二进制数据反序列化成java 对象 */
    <T> T convert(byte[] value, Class<T> clazz);
   <T> T convert2(byte[] value,Class <T> c);

    /**
     * 取出 redis 里缓存的二进制数据
     * 注意，这个方法取出的值没有经过解压缩
     * 不推荐使用
     * *
     * * @param key
     * * @return
     */
    byte[] get(String key);

    /**
     * 保存二进制数据到redis
     * 注意，这个方法的值没有经过压缩
     * 不推荐使用
     * *
     * * @param key
     * * @param value
     */
    void setex(String key, int ttl, byte[] value);

    /**
     * 判断redis中是否存在
     * * @param key
     * * @return
     */
    boolean isExist(String key);

    /**
     * 删除redis
     * * @param key
     */
    void delete(String key);
}
