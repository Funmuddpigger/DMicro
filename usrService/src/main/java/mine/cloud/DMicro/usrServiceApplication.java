package mine.cloud.DMicro;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)  //排除自动注入配置,导入手动配置
public class usrServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(usrServiceApplication.class,args);
    }
}
