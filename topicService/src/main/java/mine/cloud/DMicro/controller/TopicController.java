package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.pojo.TopUsr;
import mine.cloud.DMicro.pojo.Topic;
import mine.cloud.DMicro.service.ITopicService;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private ITopicService iTopicService;

    //话题搜索功能 ---只能根据搜索框给出的字符串搜索
    @RequestMapping(value = "/search",method = {RequestMethod.POST})
    public ResultList querySearchESWord(@RequestBody RequestParams params){
        return iTopicService.selectByESKeyWord(params);
    }

    //话题搜索功能 ---token在header
    @RequestMapping(value = "/search-select",method = {RequestMethod.POST})
    public ResultList queryArticleWithUsr(@RequestHeader("token") String token, @RequestBody Topic params){
        return iTopicService.selectTopicBySelectives(token, params);
    }

    //话题自动补全功能
    @RequestMapping(value = "/suggest",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getSuggestions(@RequestParam("suggestKey") String suggestKey){
        return iTopicService.getESSuggestWord(suggestKey);
    }

    //发布吐槽
    @RequestMapping(value = "/post",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList postTopicViewWithUsr(@RequestHeader("token") String token, @RequestBody TopUsr params){
        return iTopicService.addTopicViewWithUsr(token,params);
    }

    //删除吐槽
    @RequestMapping(value = "/del",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList delTopicViewWithUsr(@RequestHeader("token") String token, @RequestBody TopUsr params){
        return iTopicService.delTopicViewWithUsr(token,params.getTopicId());
    }

    //发布吐槽
    @RequestMapping(value = "/get-top",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getTopTopic(@RequestParam("last") Integer last){
        return iTopicService.getTopTopic(last);
    }

    //删除吐槽
    @RequestMapping(value = "/get",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getTopUsrView(@RequestHeader(value="token" ,required=false) String token, @RequestBody(required=false) TopUsr params){
        return iTopicService.getTopicBySelective(token,params);
    }
}
