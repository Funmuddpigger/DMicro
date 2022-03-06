package mine.cloud.DMicro;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients(basePackages = "mine.cloud.DMicro.feignClients")
@MapperScan("mine.cloud.DMicro.dao")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableScheduling
public class TopicServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopicServiceApplication.class,args);
    }
}
