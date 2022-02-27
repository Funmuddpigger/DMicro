package mine.cloud.DMicro;

import mine.cloud.DMicro.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients(defaultConfiguration = DefaultFeignConfiguration.class,basePackages = "mine.cloud.DMicro.feignClients")
@MapperScan("mine.cloud.DMicro.dao")
@SpringBootApplication
@EnableScheduling
public class ArtServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtServiceApplication.class,args);
    }
}
