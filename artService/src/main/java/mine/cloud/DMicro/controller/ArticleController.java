package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;


@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArtServiceApi iArtServiceApi;

    @GetMapping("/{id}")
    public Article queryByPrimaryKey(@PathVariable("id") Integer artId){
        return iArtServiceApi.selectByPrimaryKey(artId);
    }

    //文章搜索功能 ---只能根据搜索框给出的字符串搜索
    @RequestMapping(value = "/search",method = {RequestMethod.POST})
    public Result querySearchESWord(@RequestBody RequestParams params){
        return iArtServiceApi.selectByESKeyWord(params.getKey(), params.getPage(), params.getPageSize(),params.getSortBy(),params.getUpDown());
    }

    //文章自动补全功能
    @RequestMapping(value = "/select",method = {RequestMethod.GET,RequestMethod.POST})
    public Result getSuggestions(@RequestBody Article params){
        return iArtServiceApi.getSelectBySelectives(params);
    }

    //文章自动补全功能
    @RequestMapping(value = "/suggest",method = {RequestMethod.GET,RequestMethod.POST})
    public List<String> getSuggestions(@RequestParam("suggestKey") String suggestKey){
        return iArtServiceApi.getESSuggestWord(suggestKey);
    }

    //文章添加功能 
    @RequestMapping(value = "/insert",method = {RequestMethod.POST})
    public Result insertArticle(@RequestBody Article article){
        return iArtServiceApi.saveArticle(article);
    }

    //文章更新功能 
    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public Result updateArticleById(@RequestBody Article article){
        return iArtServiceApi.updateArticle(article);
    }

    //文章删除功能 
    @RequestMapping(value = "/del",method = {RequestMethod.POST})
    public Result deleteArticleById(@RequestBody Integer artId){
        return iArtServiceApi.deleteArticle(artId);
    }
}
