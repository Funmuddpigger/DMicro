package mine.cloud.DMicro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@MapperScan("mine.cloud.DMicro.dao")
@SpringBootApplication
public class ArtServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtServiceApplication.class,args);
    }
}
