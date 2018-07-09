package top.shanbing.conf.redis.annotation;

import top.shanbing.conf.redis.constant.RedisAction;

import java.lang.annotation.*;

/**
 *
 * 自定义Redis注解
 *
 * 这个自定义注解用来对业务方法进行redis服务注入
 *
 * 在方法体添加这个注解，会按需自动实现redis相关操作
 *
 * 业务代码与redis代码解耦
 *
 * @author KangKai
 * @date 2017/3/31.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Redis {

    /**
     * 非null值的默认时间，暂定4小时+10分钟，后面根据业务需要调整
     */
    int DEFAULT_TTL = 4 * 60 * 60 + 10 * 60;

    /**
     * null值的缓存时间，暂定半小时
     */
    int NULL_TTL = (int) (0.5 * 60 * 60);

    /**
     * 若某个key的value为这个值，表示该key缓存的是null值
     */
    String NULL_VALUE = "_NULL_";

    /**
     * 数据存储进redsi的key，见 {@link RedisKeys}
     * @return
     */
    String value();

    /**
     * <pre>
     *  指示方法的哪些参数用来构造key，及其顺序(编号由0开始)
     *
     *   示例
     *       keyArgs = {1,0,2}，表示用方法的第2，第1，第3个参数，按顺序来构造key
     *
     *   默认值的意思是方法的前 n 个参数来构造key，n 最大为10
     *
     *   ！！如果构造 key 的参数不多于 10 个且顺序也和方法参数一致，则可以用默认值
     *
     *  </pre>
     */
    int[] keyArgs() default { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    /** 执行何种操作，默认是先访问 redis **/
    RedisAction action() default RedisAction.REDIS_FIRST;

    /** 过期时间，默认250分钟 **/
    int ttl() default DEFAULT_TTL;

    /** 是否以同步的方式操作redis，默认是false **/
    boolean sync() default false;

    /** 是否要缓存 null 值，默认为true **/
    boolean cacheNull() default true;

    /** 如果要缓存null值，过期时间是多少，默认5分钟 **/
    int nullTtl() default NULL_TTL;

    /** redis 不能保存 null值，就用这个值表示缓存的是 null值 */
    String nullValue() default NULL_VALUE;

}
