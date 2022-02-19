package mine.cloud.DMicro.feignClients;

import mine.cloud.DMicro.pojo.Comment;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//指定注册中心中的服务名称
@FeignClient("com-service")
public interface ComClient {

    /**
     * 供文章调用评论接口
     * @param record 只传artId
     * @return
     */
    @PostMapping("/comment/select")
    ResultList selectCommentBySelectives(@RequestBody Comment record);

}
