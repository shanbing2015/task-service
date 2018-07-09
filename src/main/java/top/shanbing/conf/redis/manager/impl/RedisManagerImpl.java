package top.shanbing.conf.redis.manager.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import redis.clients.util.SafeEncoder;
import top.shanbing.bean.TestBean;
import top.shanbing.conf.redis.manager.RedisManager;
import top.shanbing.util.json.JsonHelper;
import top.shanbing.util.zip.ZipUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Redis封装
 *
 * @author KangKai
 * @date 2017/3/29.
 */

@EnableAsync
@Component
public class RedisManagerImpl implements RedisManager {


    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    private static final Logger LOG = LoggerFactory.getLogger(RedisManagerImpl.class);

    /**
     * 定义字节编码为UTF-8
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 存入的数据是否需要用{@link ZipUtil}压缩
     */
    public static final boolean NEED_ZIP = false;


    @Override
    public <T> void setex(String key, int ttl, T data, boolean sync) {
        //同步写入
        if (sync) {
            setex(key, ttl, data);
        } else {
            asyncSetex(key, ttl, data);
        }
    }

    @Async
    public <T> void asyncSetex(String key, int ttl, T data) {
        setex(key, ttl, data);
    }

    @Override
    public <T> T get(String key, Class <T> clazz) {
        return convert(get(key), clazz);
    }

    @Override
    public <T> T get2(String key, Class <T> clazz) {
        return convert2(get(key), clazz);
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T> T convert(byte[] value, Class <T> clazz) {
        if (value != null) {
            //原始类型直接转型返回
            if (clazz.isPrimitive()) {
                String temp;
                try {
                    temp = new String(value, CHARSET);
                } catch (UnsupportedEncodingException e) {
                    LOG.error("UnsupportedEncodingException", e);
                    return null;
                }
                if (String.class.isAssignableFrom(clazz)) {
                    return (T) temp;
                } else if (Integer.class.isAssignableFrom(clazz)) {
                    return (T) new Integer(temp);
                } else if (Long.class.isAssignableFrom(clazz)) {
                    return (T) new Long(temp);
                } else if (Boolean.class.isAssignableFrom(clazz)) {
                    return (T) new Boolean(temp);
                } else if (Double.class.isAssignableFrom(clazz)) {
                    return (T) new Double(temp);
                } else if (Float.class.isAssignableFrom(clazz)) {
                    return (T) new Float(temp);
                } else if (Byte.class.isAssignableFrom(clazz)) {
                    return (T) new Byte(temp);
                } else if (Short.class.isAssignableFrom(clazz)) {
                    return (T) new Short(temp);
                } else if (Character.class.isAssignableFrom(clazz)) {
                    return (T) new Character((char) (((value[0] & 0xFF) << 8) | (value[1] & 0xFF)));
                } else {
                    throw new RuntimeException(
                            "unsupport type:type=" + clazz.getName() + "value=" + temp);
                }
            } else {
                //非基本类型：判断是否需要利用ZipUtil解压数据，并返回
                if (NEED_ZIP) {
                    byte[] dataBytes = ZipUtil.unzip(value);
                    return JsonHelper.readValue(dataBytes, 0, dataBytes.length, clazz);
                } else {
                    return JsonHelper.readValue(value, 0, value.length, new TypeReference<T>(){});
                }

            }
        }
        return null;

    }

    @Override
    public <T> T convert2(byte[] value,Class <T> c) {
        Object o = new ArrayList<T>();
        System.out.println(c);
        T tt = JsonHelper.readValue(value,0, value.length, new TypeReference<T>(){});

       /* try {
            Class<?> c =  Class.forName(c.toString());
        }catch (Exception e){
            e.printStackTrace();
        }*/

        /*if (NEED_ZIP) {
            byte[] dataBytes = ZipUtil.unzip(value);
            return JsonHelper.readValue(dataBytes,0, value.length,(Class<T>) type);
        } else {
            //return JsonHelper.readValue(value,0, value.length,(Class<T>) type);
            try {
                Class c = Class.forName(type.getTypeName()).newInstance().getClass();
                return JsonHelper.readValue(value,0, value.length, new TypeReference<T>(){});
            }catch (Exception e){
                e.printStackTrace();
            }
           return null;
        }*/

        return null;
    }

    @Override
    public byte[] get(String key) {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        try {
            //get操作用同一个connection
            return redisConnection.get(SafeEncoder.encode(key));
        } finally {
            redisConnection.close();
        }

    }

    @Override
    public void setex(String key, int ttl, byte[] value) {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        try {
            if (NEED_ZIP) {
                jedisConnectionFactory.getConnection().setEx(SafeEncoder.encode(key), ttl, ZipUtil.zip(value));
            } else {
                jedisConnectionFactory.getConnection().setEx(SafeEncoder.encode(key), ttl, value);
            }

        } finally {
            redisConnection.close();
        }

    }

    @Override
    public boolean isExist(String key) {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        try {
            return redisConnection.exists(SafeEncoder.encode(key));
        } finally {
            redisConnection.close();
        }

    }

    @Override
    public void delete(String key) {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        try {
            redisConnection.del(SafeEncoder.encode(key));
        } finally {
            redisConnection.close();
        }

    }

    private <T> void setex(String key, int ttl, T data) {
        RedisConnection redisConnection = jedisConnectionFactory.getConnection();
        //如果是基本类型，直接处理
        if (data.getClass().isPrimitive()) {
            try {
                redisConnection.setEx(SafeEncoder.encode(key), ttl, SafeEncoder.encode(data.toString()));
            } finally {
                redisConnection.close();
            }

        } else {
            try {
                byte[] dataBytes;
                //判断是否需要利用ZipUtil压缩数据
                if (NEED_ZIP) {
                    dataBytes = ZipUtil.zip(JsonHelper.OM.writeValueAsString(data).getBytes(CHARSET));
                } else {
                    dataBytes = JsonHelper.OM.writeValueAsString(data).getBytes(CHARSET);
                }
                redisConnection.setEx(key.getBytes(CHARSET), ttl, dataBytes);

            } catch (Exception e) {
                LOG.error("数据存入redis错误", e);
            } finally {
                redisConnection.close();
            }
        }
    }

}
