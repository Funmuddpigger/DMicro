package mine.cloud.DMicro.feignClients;


import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("usr-service")
public interface UsrClient {

    @GetMapping("/user/select-by-id")
    User selectByPK(@RequestBody Integer usrId);

    /**
     * 提供usrService验证token
     * @param token
     * @return
     */
    @PostMapping("/user/if-auth")
    ResultList getAuthAndCheck(@RequestHeader(name = "Token") String token);
}
