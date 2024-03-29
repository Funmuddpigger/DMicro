package mine.cloud.DMicro.controller;

import lombok.extern.slf4j.Slf4j;
import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.params.RequestParamsESArt;
import mine.cloud.DMicro.params.RequestParamsRedisArtUsr;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.Video;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    public ResultList querySearchESWord(@RequestBody RequestParams params){
        return iArtServiceApi.selectByESKeyWord(params.getKey(), params.getPage(), params.getPageSize(),params.getSortBy(),params.getUpDown());
    }

    //文章搜索功能 ---其中key为token,有token,需要校验token,无token需要当作访问请求校验
    @RequestMapping(value = "/search-usr",method = {RequestMethod.POST})
    public ResultList queryArticleWithUsr(HttpServletRequest request,@RequestBody RequestParams params){
        return iArtServiceApi.selectByTokenWithUsr(request, params);
    }

    //文章多条件搜索--mysql
    @RequestMapping(value = "/select-sql",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getSelectBySelectives(@RequestBody Article params){
        return iArtServiceApi.getSelectBySelectives(params);
    }

    //文章根据标题条件搜索--es
    @RequestMapping(value = "/select-es",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getSelectBySelectives(@RequestHeader(value = "token",required = false) String token,@RequestBody RequestParamsESArt params){
        return iArtServiceApi.getESArticleByTitleOrType(token,params);
    }

    //文章自动补全功能
    @RequestMapping(value = "/suggest",method = {RequestMethod.GET,RequestMethod.POST})
    public List<String> getSuggestions(@RequestParam("suggestKey") String suggestKey){
        return iArtServiceApi.getESSuggestWord(suggestKey);
    }

    //文章热榜功能
    @RequestMapping(value = "/hot-article",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getHotArticleList(){ return iArtServiceApi.getHotArticleList();
    }

    //文章热榜功能
    @RequestMapping(value = "/new-article",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList getNewArticle(){
        return iArtServiceApi.getNewArticle();
    }

    //文章添加功能 
    @RequestMapping(value = "/insert",method = {RequestMethod.POST})
    public ResultList insertArticle(@RequestHeader("token") String token ,@RequestBody Article article){
        return iArtServiceApi.saveArticle(token,article);
    }

    //文章更新功能 
    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public ResultList updateArticleById(@RequestBody Article article){
        return iArtServiceApi.updateArticle(article);
    }

    //文章点赞功能  --redis
    @RequestMapping(value = "/like",method = {RequestMethod.POST})
    public ResultList tapArticleLike(@RequestBody RequestParamsRedisArtUsr params){
        return iArtServiceApi.tapArticleLike(params);
    }

    //文章阅读功能  --redis
    @RequestMapping(value = "/read",method = {RequestMethod.POST})
    public ResultList tapArticleRead(@RequestBody RequestParamsRedisArtUsr params){
        return iArtServiceApi.tapArticleRead(params);
    }

    //文章删除功能 
    @RequestMapping(value = "/del",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultList deleteArticleById(@RequestParam("artId") Integer artId){
        return iArtServiceApi.deleteArticle(artId);
    }

    //article模块上传回显url(image/video)
    @RequestMapping("/upload")
    public ResultList uploadImg(@RequestParam("image") MultipartFile file, HttpServletRequest request)  {
        return iArtServiceApi.upLoadImageOrFile(file, request);//这里调用service的upfile方法，传入两个参数。
    }

    //art-video save
    @RequestMapping("/video-insert")
    public ResultList saveVideoUrl(@RequestHeader("token") String token ,@RequestBody Video params)  {
        return iArtServiceApi.saveVideoUrl(token,params);
    }

    //art-video del
    @RequestMapping("/video-del")
    public ResultList delVideoUrl(@RequestBody Integer id)  {
        return iArtServiceApi.delVideoById(id);
    }

    //art-video read
    @RequestMapping("/video-read")
    public ResultList tapToRead(@RequestBody Integer id)  {
        return iArtServiceApi.tapToReadVideo(id);
    }

    //art-video like
    @RequestMapping("/video-like")
    public ResultList tapToLike(@RequestBody Integer id)  {
        return iArtServiceApi.tapToLikeVideo(id);
    }

    //art-select
    @RequestMapping("video-select")
    public ResultList selectVideoBySelective(@RequestBody(required = false) Video record){
        return iArtServiceApi.selectVideoBySelective(record);
    }
}
