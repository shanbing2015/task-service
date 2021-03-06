package top.shanbing.conf.redis.stringconvertor;

import java.lang.reflect.Type;


/**  
 * Function: java对象到字符串的转换接口. <br/>
 * Date: 2015年6月26日 上午11:44:51 <br/>  
 *  
 * @author seven.zhao  
 * @version   
 * @since JDK 1.6  
 */
public interface StringConverter{
    
    /**  
     * toString:将java对象转化为字符串. <br/>  
     * @author seven.zhao  
     * @param t java对象
     * @return  java对象序列化后的字符串
     * @since JDK 1.6  
     */
    public <T> String toString(T t);

    /**  
     * fromString:将字符串转换为指定类型的对象. <br/>  
     * 适用于非泛型类型.<br/>    
     * @author seven.zhao  
     * @param value  字符串
     * @param clazz  指定的类型
     * @return  指定类型的对象
     * @since JDK 1.6  
     */
    public <T> T fromString(String value, Class<T> clazz);
    
    /**  
     * fromString:将字符串转换为指定类型的对象. <br/>  
     * 适用于泛型类型.<br/>  
     * gson和fastjson提供了方法获得泛型类型的type.<br/>
     * gson： new TypeToken<genericType>(){}.getType()  <br/>
     * fastjson: new TypeReference<genericType>(){}.getType() <br/>
     * @author seven.zhao  
     * @param value 字符串
     * @param type  指定的类型
     * @return  指定类型的对象
     * @since JDK 1.6  
     */
    public <T> T fromString(String value, Type type);  
    
}
