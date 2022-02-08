package mine.cloud.DMicro.controller;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArtServiceApi iArtServiceApi;

    @GetMapping("/{id}")
    public Article queryByPrimaryKey(@PathVariable("id") Integer artId){
        return iArtServiceApi.selectByPrimaryKey(artId);
    }

//    @GetMapping("/{id}")
//    public Article queryByPKWithUsr(@PathVariable("id") Integer artId){
//        return iArtServiceApi.selectByPKWithUsr(artId);
//    }

    //文章搜索功能 ---只能根据搜索框给出的字符串搜索
    @RequestMapping(value = "/search",method = {RequestMethod.POST})
    public Result querySearchESWord(String ESKeyWord,
                                    @RequestParam(value = "page",required = true,defaultValue = "1") Integer page,
                                    @RequestParam(value = "pagesize",required = true,defaultValue = "20") Integer pagesize){
        return iArtServiceApi.selectByESKeyWord(ESKeyWord, page, pagesize);
    }
}
