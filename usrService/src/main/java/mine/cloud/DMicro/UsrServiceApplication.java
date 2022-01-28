package mine.cloud.DMicro;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "mine.cloud.DMicro.feignClients")
@MapperScan("mine.cloud.DMicro.dao")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)  //排除自动注入配置,导入手动配置
public class UsrServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsrServiceApplication.class,args);
    }
}
