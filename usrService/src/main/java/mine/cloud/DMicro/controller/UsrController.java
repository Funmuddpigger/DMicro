package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IUsrServiceApi;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UsrController {

    @Autowired
    private IUsrServiceApi iUsrServiceApi;

    @Autowired
    private HttpServletRequest request;

    //验证实验权限页面
    @RequestMapping("/hello")
    public String queryByUsrId(){
        return "hello";
    }

    //查询用户页面by id
    @RequestMapping("/select-by-id")
    public User queryByUsrId(@RequestBody Integer id){
        return iUsrServiceApi.selectByPrimaryKey(id);
    }

    //查询用户 ---sql
    @RequestMapping(value = "/select", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList selectOneBySelective(@RequestBody User user){
        return iUsrServiceApi.selectOneBySelective(user);
    }

    //插入用户 ---sql
    @RequestMapping(value = "/insert", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList insertUserBySelective(@RequestBody User user){
        return iUsrServiceApi.insertUserBySelective(user);
    }

    //注销用户 --redis
    @RequestMapping(value = "/logout", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList logoutBySecurity(){
        return iUsrServiceApi.logoutBySecurity();
    }

    //验证登录用户 解析token ---redis
    @RequestMapping(value = "/if-auth", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList checkTokenAndUsr(@RequestHeader(name = "Token") String token){
        return iUsrServiceApi.checkTokenAndUsr(token);
    }

    //登入用户  ---redis
    @RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList loginUserBySecurity(@RequestBody User user){
        return iUsrServiceApi.loginUserBySecurity(user);
    }

    //更新用户信息 ---mysql ,mq ,redis 异步构建
    @RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList updateUserBySelective(@RequestBody User user){
        return iUsrServiceApi.updateUserBySelective(user);
    }

    //关注 ---redis 定时任务同步
    @RequestMapping(value = "/follow", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList followUser(@RequestBody Integer user){
        return iUsrServiceApi.followUser(user,request);
    }

    //得到用户关注信息 ---mysql ,mq ,redis 异步构建
    @RequestMapping(value = "/follower", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getFollowUser(@RequestBody Integer user){
        return iUsrServiceApi.getFollowUser(request);
    }

    //得到用户粉丝信息 ---mysql
    @RequestMapping(value = "/fans", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getFanAndNum(@RequestBody Integer user){
        return iUsrServiceApi.getFanAndNum(request);
    }
}
