package mine.cloud.DMicro;

import mine.cloud.DMicro.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration.class,basePackages = "mine.cloud.DMicro.feignClients")
@MapperScan("mine.cloud.DMicro.dao")
@SpringBootApplication
public class CommentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentServiceApplication.class,args);
    }
}
