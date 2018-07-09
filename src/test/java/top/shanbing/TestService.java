package top.shanbing;

import org.springframework.stereotype.Service;
import top.shanbing.bean.TestBean;
import top.shanbing.conf.redis.annotation.Redis;
import top.shanbing.conf.redis.constant.RedisKeys;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {

    @Redis(RedisKeys.TEST_REDIS_KEY)
    public TestBean getTest(int i){
        System.out.println("初始化TestBean");
        TestBean t = new TestBean(1,"张三"+i);
        TestBean t1 = new TestBean(1,"1张三"+i);
        TestBean t2 = new TestBean(1,"2张三"+i);
        TestBean t3 = new TestBean(1,"3张三"+i);
        List list = new ArrayList();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        t.list = list;
        return t;
    }

    @Redis(RedisKeys.TEST_REDIS_KEY)
    public List<TestBean> getTests(int i){
        TestBean t = new TestBean(1,"张三"+i);
        TestBean t1 = new TestBean(1,"1张三"+i);
        TestBean t2 = new TestBean(1,"2张三"+i);
        TestBean t3 = new TestBean(1,"3张三"+i);
        List list = new ArrayList();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        return list;
    }

    @Redis(RedisKeys.TEST_REDIS_KEY)
    public Map<String,TestBean> getTestMap(int i){
        Map<String,TestBean> map = new LinkedHashMap<>();

        TestBean t1 = new TestBean(1,"1张三"+i);
        TestBean t2 = new TestBean(1,"2张三"+i);
        TestBean t3 = new TestBean(1,"3张三"+i);
        map.put("1",t1);
        map.put("2",t2);
        map.put("3",t3);
        return map;
    }

}
