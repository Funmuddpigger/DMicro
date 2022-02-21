package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IUsrServiceApi;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UsrController {

    @Autowired
    private IUsrServiceApi iUsrServiceApi;

    @RequestMapping("/hello")
    public String queryByUsrId(){
        return "hello";
    }

    @RequestMapping("/select-by-id")
    public User queryByUsrId(@RequestBody Integer id){
        return iUsrServiceApi.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/select", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList selectOneBySelective(@RequestBody User user){
        return iUsrServiceApi.selectOneBySelective(user);
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList insertUserBySelective(@RequestBody User user){
        return iUsrServiceApi.insertUserBySelective(user);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList updateUserBySelective(@RequestBody User user){
        return iUsrServiceApi.updateUserBySelective(user);
    }
}
