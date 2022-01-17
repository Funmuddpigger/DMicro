package mine.cloud.DMicro.controller;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IUsrServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UsrController {

    @Autowired
    private IUsrServiceApi iUsrServiceApi;

    @GetMapping("/{id}")
    public User queryByUsrId(@PathVariable("id")Integer id){
        return iUsrServiceApi.selectByPrimaryKey(id);
    }
}
