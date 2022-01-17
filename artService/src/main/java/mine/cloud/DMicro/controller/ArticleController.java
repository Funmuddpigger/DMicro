package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.service.IArtServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArtServiceApi iArtServiceApi;

//    @GetMapping("/{id}")
//    public Article queryByPrimaryKey(@PathVariable("id") Integer artId){
//        return iArtServiceApi.selectByPrimaryKey(artId);
//    }

    @GetMapping("/{id}")
    public Article queryByPKWithUsr(@PathVariable("id") Integer artId){
        return iArtServiceApi.selectByPKWithUsr(artId);
    }
}
