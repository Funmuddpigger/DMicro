package mine.cloud.DMicro.feignClients;


import mine.cloud.DMicro.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("usr-service")
public interface UsrClient {

    @GetMapping("/user/select-by-id")
    User selectByPK(@RequestBody Integer usrId);

}
