package mine.cloud.DMicro.clients;

import mine.cloud.DMicro.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("usr-service")
public interface UsrClient {

    @GetMapping("/user/{id}")
    User selectByPK(@PathVariable("id") Integer usrId);
}
