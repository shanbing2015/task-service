package top.shanbing;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.shanbing.conf.redis.service.RedisStringService;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedisSlaveTest {
    @Autowired
    private RedisStringService redisStringService;

    private static String key = "k10";
    private static String value = "v123";

    @Test
    public void test(){
        System.out.println(redisStringService.set(key,value));
    }

    @Test
    public void testGet() throws InterruptedException{
        int i=0;
        while (true){
            System.out.println(i+"\t"+redisStringService.get(key));
            Thread.sleep(1000);
        }
    }
}
