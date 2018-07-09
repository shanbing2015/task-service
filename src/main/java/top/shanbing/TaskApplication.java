package top.shanbing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * Created by shanbing on 2018/3/24.
 */
@SpringBootApplication
@EnableEurekaClient
public class TaskApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TaskApplication.class).web(true).run(args);
    }
}
