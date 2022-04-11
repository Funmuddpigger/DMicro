package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.pojo.Comment;
import mine.cloud.DMicro.service.IComServiceApi;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private IComServiceApi iComServiceApi;

    @RequestMapping(value = "/select",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultList selectCommentBySelectives(@RequestBody Comment comment){
        return iComServiceApi.selectCommentBySelectives(comment);
    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultList updateCommentBySelectives(@RequestBody Comment comment){
        return iComServiceApi.updateCommentBySelectives(comment);
    }

    @RequestMapping(value = "/delete",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultList delCommentById(@RequestParam("comId") Integer comId){
        return iComServiceApi.delCommentById(comId);
    }

    @RequestMapping(value = "/del",method = {RequestMethod.POST,RequestMethod.GET})
        public ResultList delCommentById(@RequestBody Comment record){
        return iComServiceApi.delCommentBySelectives(record);
    }

    @RequestMapping(value = "/insert",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultList addCommentBySelectives(@RequestHeader("token") String token,@RequestBody Comment comment){
        return iComServiceApi.addCommentBySelectives(token,comment);
    }
}
