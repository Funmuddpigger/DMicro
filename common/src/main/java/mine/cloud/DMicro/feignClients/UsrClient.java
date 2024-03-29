package mine.cloud.DMicro.feignClients;


import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("usr-service")
public interface UsrClient {

    @PostMapping("/user/select-by-id")
    ResultList selectByPK(@RequestHeader(name = "token") String token,@RequestParam(value = "id",required = false) Integer id);

    /**
     * 提供usrService验证token
     * @param token
     * @return
     */
    @PostMapping("/user/if-auth")
    ResultList getAuthAndCheck(@RequestHeader(name = "token") String token);

    /**
     * 提供usrServive得到fans数和info
     * @param userId
     * @return
     */
    @PostMapping("/user/fans")
    ResultList getFanAndNum(@RequestHeader(name="token") String token,@RequestBody Integer userId);
}
