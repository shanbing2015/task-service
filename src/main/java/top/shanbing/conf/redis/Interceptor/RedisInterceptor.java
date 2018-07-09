package top.shanbing.conf.redis.Interceptor;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.shanbing.bean.TestBean;
import top.shanbing.conf.redis.annotation.Redis;
import top.shanbing.conf.redis.constant.RedisAction;
import top.shanbing.conf.redis.manager.RedisManager;
import top.shanbing.conf.redis.manager.impl.RedisManagerImpl;
import top.shanbing.util.json.JsonHelper;
import top.shanbing.util.zip.ZipUtil;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截使用了@Redis注解的业务方法
 * <p>
 * 根据@Redis的配置来确定从该方法的redis存取逻辑
 *
 * @author KangKai
 * @version v0.1
 * @date 2017/3/31.
 */
@Component
@Aspect
public class RedisInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RedisInterceptor.class);

    @Resource
    private RedisManager redisManager;

    static {
        System.out.println("实例化redis-AOP");
    }

    /**
     * 这里处理了进入业务方法之前 和 业务方法返回之后的逻辑
     * ->1 首先先判断这个方法是否要直接穿透redis到db
     * -->1.1 如果要，直接走业务方法，并把业务方法返回值缓存到db，最后返回给客户端，下次缓存可以命中
     * -->1.2 如果不要，根据redisKey到redis获取数据
     * --->1.2.1 若数据为空，走1.1的流程
     * --->1.2.2 若数据不为空，直接返回数据给客户端
     *
     * @param pjp
     * @param redis
     * @return
     */
    @Around("@annotation(redis)")
    public Object doAround(ProceedingJoinPoint pjp, Redis redis) throws Throwable {


        //是否要直接穿透redis，到db获取
        boolean stab = (redis.action() == RedisAction.STAB_REDIS);

        //根据参数构建redisKey
        String redisKey = buildRedisKey(pjp, redis);

        //获取返回值的类型
        Class <?> returnType = getReturnClass(pjp);

        Object resultFromRedis = null;
        if (!stab) {
            //不穿透，从redis中获取数据
            if(returnType == java.util.List.class){
                Type type = getReturnType(pjp);

                new TypeReference<Type>(){};

                resultFromRedis = getDataFromRedis(redisKey, redis, returnType);
            }else{
                resultFromRedis = getDataFromRedis(redisKey, redis, returnType);
            }


        } else {
            //穿透，从业务层代码（db）中获取数据，并缓存起来，便于下次命中
            return getDataFromDbAndDoCache(pjp, redis, redisKey);
        }

        //如果redis中获取的数据不为null，直接返回，不需要走业务方法
        if (resultFromRedis != null) {
            //判断是否空值，空值返回null
            if (resultFromRedis.toString().equals(Redis.NULL_VALUE)) {
                return null;
            } else {
                return resultFromRedis;
            }
        } else {
            //穿透，从业务层代码（db）中获取数据，并缓存起来，便于下次命中
            return getDataFromDbAndDoCache(pjp, redis, redisKey);
        }
    }

    /**
     * 穿透到业务层获代码获取数据，并缓存起来
     *
     * @param pjp
     * @param redis
     * @param redisKey
     * @return
     */
    private Object getDataFromDbAndDoCache(ProceedingJoinPoint pjp, Redis redis, String redisKey) throws Throwable {
        //穿透，从业务方法获取数据
        Object resultFromDb = pjp.proceed();
        //缓存业务方法的数据，便于下次命中,这里可以异步存入，可以提升性能，暂时不做
        cacheDataToRedis(resultFromDb, redisKey, redis);
        return resultFromDb;
    }

    /**
     * 缓存数据到redis
     *
     * @param resultFromDb
     * @param redisKey
     * @param redis
     */
    private void cacheDataToRedis(Object resultFromDb, String redisKey, Redis redis) {
        //数据若不为null，直接缓存
        if (resultFromDb != null) {
            redisManager.setex(redisKey, redis.ttl(), resultFromDb, redis.sync());
        } else if (redis.cacheNull()) {
            //若数据为空，缓存一个代表null的字符串到redis
            redisManager.setex(redisKey, redis.ttl(), Redis.NULL_VALUE, redis.sync());
        }
    }


    /**
     * 从redis中获取已缓存的数据
     *
     * @param redisKey
     * @param redis
     * @param returnType
     * @return
     */
    private Object getDataFromRedis(String redisKey, Redis redis, Class <?> returnType) {

        //获取redis中缓存的数据，如果需要穿透redis到db获取，则赋值为null
        //byte[] value = redisManager.get(redisKey);
        Object value = redisManager.get(redisKey,returnType);

        //先判断redis中该key缓存的值是不是"null字符串"，是则返回null
        //这里是一个诡异的BUG，可能和JSON的序列化工具有关
        //存进去一个"_NULL_"的字符串，拿出来是这样的字符串 ""_NULL_""(补了两个双引号，找了很久，单元测试过不了，原来是这个问题)
        byte[] nullValue;
        if (RedisManagerImpl.NEED_ZIP) {
            nullValue = ZipUtil.zip(("\"" + redis.nullValue() + "\"").getBytes());
        } else {
            nullValue = redis.nullValue().getBytes();
        }
        //if (value != null && redis.cacheNull() && Arrays.equals(value, nullValue)) {
        if (value != null && redis.cacheNull() && value.toString().equals(redis.nullValue())) {
            return Redis.NULL_VALUE;
        } else {
            //返回经过类型转换的对象
            //return redisManager.convert(value, returnType);
            return value;
        }
    }

    private Object getDataFromRedis(String redisKey, Redis redis,Type type) {

        //获取redis中缓存的数据，如果需要穿透redis到db获取，则赋值为null
        //byte[] value = redisManager.get(redisKey);
        System.out.println( type);
        Object value = redisManager.get(redisKey,type.getClass());

        //先判断redis中该key缓存的值是不是"null字符串"，是则返回null
        //这里是一个诡异的BUG，可能和JSON的序列化工具有关
        //存进去一个"_NULL_"的字符串，拿出来是这样的字符串 ""_NULL_""(补了两个双引号，找了很久，单元测试过不了，原来是这个问题)
        byte[] nullValue;
        if (RedisManagerImpl.NEED_ZIP) {
            nullValue = ZipUtil.zip(("\"" + redis.nullValue() + "\"").getBytes());
        } else {
            nullValue = redis.nullValue().getBytes();
        }
        //if (value != null && redis.cacheNull() && Arrays.equals(value, nullValue)) {
        if (value != null && redis.cacheNull() && value.toString().equals(redis.nullValue())) {
            return Redis.NULL_VALUE;
        } else {
            //返回经过类型转换的对象
            //return redisManager.convert(value, returnType);
            return value;
        }
    }

    /**
     * 构建redisKey
     *
     * @param pjp
     * @param redis
     * @return
     */
    private String buildRedisKey(ProceedingJoinPoint pjp, Redis redis) {
        //获取构建redis key的参数
        Object[] keyArgs = getKeyArgs(pjp.getArgs(), redis.keyArgs());
        //构建redis key
        String redisKey = keyArgs == null ? redis.value() : String.format(redis.value(), keyArgs);
        //若参数为空，输出警告
        if (keyArgs == null || keyArgs.length == 0 || keyArgs[0] == null) {
            LOG.warn("key args is empty,key=" + redisKey);
        }
        return redisKey;
    }

    /**
     * 获得方法的返回值类型
     *
     * @param pjp
     * @return
     */
    private Class <?> getReturnClass(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Class <?> returnClass = method.getReturnType();
        return returnClass;
    }

    private Type getReturnType(ProceedingJoinPoint pjp){
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Class <?> returnClass = method.getReturnType();

        Type returnType = method.getGenericReturnType();
        System.out.println(returnType.getTypeName());

      //  Class itemTypeClass = (Class) ((ParameterizedType) returnType).getActualTypeArguments()[0];
       // return JsonHelper.getCollectionType(returnClass,itemTypeClass);
        //TypeReference typeReference = new TypeReference<itemTypeClass>(){};
        //return new TypeReference<List<returnType.getTypeName()>>(){};
        return returnType;
    }

    /**
     * 获取构造redis的key的参数数组
     *
     * @param args    参数数组
     * @param keyArgs 参数的位置数组
     * @return
     */
    private Object[] getKeyArgs(Object[] args, int[] keyArgs) {
        Object[] redisKeyArgs;
        int len = keyArgs.length;
        if (len == 0) {
            return null;
        } else {
            len = min(len, args.length);
            redisKeyArgs = new Object[len];
            int i = 0;
            for (int n : keyArgs) {
                redisKeyArgs[i++] = args[n];
                if (i >= len) {
                    break;
                }
            }
            return redisKeyArgs;
        }
    }

    private int min(int i, int j) {
        return i < j ? i : j;
    }


}
