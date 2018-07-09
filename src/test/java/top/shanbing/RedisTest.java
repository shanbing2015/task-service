package top.shanbing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.applet.Main;
import top.shanbing.bean.TestBean;
import top.shanbing.conf.redis.manager.RedisManager;
import top.shanbing.conf.redis.service.RedisStringService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;    //使用StringRedisSerializer序列化

    @Autowired
    private RedisTemplate redisTemplate;        //使用JdkSerializationRedisSerializer序列化


    @Autowired
    private RedisStringService redisStringService;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private TestService testService;

    //@Test
    public void test(){
        stringRedisTemplate.opsForValue().set("test:s2","张二");

        String redisValue = stringRedisTemplate.opsForValue().get("test:s2");
        System.out.println(redisValue);
        //Assert.assertEquals("v2",redisValue);
    }

    //@Test
    public void test2(){
        TestBean bean = new TestBean();
        bean.id = 1000;
        bean.name = "abc";

//        redisTemplate.opsForValue().set("test:s3","张三");
//        Object redisValue = redisTemplate.opsForValue().get("test:s3");
//        System.out.println(redisValue);

        redisTemplate.opsForValue().set("test:s4",bean);
        Object redisValue = redisTemplate.opsForValue().get("test:s4");
        System.out.println(redisValue.toString());
    }

    //@Test
    public void test4() throws InterruptedException{
        String key = "test:s5";

        TestBean bean = new TestBean();
        bean.id = 1000;
        bean.name = "abcd";

        System.out.println(redisStringService.set(key,15,bean));
        for(int i=0;i<25;i++){
            Object obj = redisStringService.get(key);
            System.out.println(i+"#" +LocalTime.now().toString() +"#" +obj!=null?obj.toString():null);
            Thread.sleep(1000);
        }
        //System.out.println(redisStringService.delete("test:s3"));

    }

    //@Test
    public void test5() throws InterruptedException{
        String key = "test:s9";
        TestBean bean = new TestBean();
        bean.id = 2000;
        bean.name = "张三";
        TestBean bean2 = new TestBean();
        bean.id = 3000;
        bean.name = "李四";
        List<TestBean> list = new LinkedList<>();
        list.add(bean);
        list.add(bean2);

        System.out.println(redisStringService.set(key,5,list));
        redisStringService.expired(key,20);
        for(int i=0;i<25;i++){
            //System.out.println(i+"#" +LocalTime.now().toString() +"#" +( redisStringService.exists(key)?()redisStringService.get(key):null ));
            TestBean t = redisStringService.get(key,TestBean.class);
            Thread.sleep(1000);
            if(i==15){
                redisStringService.delete(key);
            }
        }
        //System.out.println(redisStringService.delete("test:s3"));

    }

    //@Test
    public void test6() throws InterruptedException{
        String key = "test:s5";
        TestBean bean = new TestBean();
        bean.id = 2000;
        bean.name = "张三";
        TestBean bean2 = new TestBean();
        bean.id = 3000;
        bean.name = "李四";
        List<TestBean> list = new LinkedList<>();
        list.add(bean);
        list.add(bean2);


        redisManager.setex(key,10,list,false);

        for(int i=0;i<25;i++){
            bean = redisManager.get(key,TestBean.class);
            System.out.println(bean!=null?bean.toString():null);
            Thread.sleep(1000);
        }
    }

    //@Test
    public void test7(){
        List<TestBean> list = testService.getTests(13);
        list.forEach( item -> System.out.println(item.toString()));

//        Object obj = testService.getTests(12);
//        System.out.println(obj.toString());
    }

}

