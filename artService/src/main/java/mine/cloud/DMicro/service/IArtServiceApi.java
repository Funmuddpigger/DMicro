package mine.cloud.DMicro.service;

import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.params.RequestParamsESArt;
import mine.cloud.DMicro.params.RequestParamsRedisArtUsr;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.utils.Result;
import mine.cloud.DMicro.utils.ResultList;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Api:
 *  对内服务,不对外进行接口暴露
 */
public interface IArtServiceApi {

    //select by ID
    Article selectByPrimaryKey(Integer artId);

    //select by ID with Usr
    Article selectByPKWithUsr(Integer artId);

    //select by KeyWord
    ResultList selectByESKeyWord(String word, Integer page, Integer pageSize, String sortBy, String upDown);

    List<String> getESSuggestWord(String suggestKey);

    //insert
    ResultList saveArticle(String token,Article article);

    //uodate
    ResultList updateArticle(Article article);

    //del
    ResultList deleteArticle(Integer id);

    void esRedisArtDelete(Integer artId);

    void esRedisArtInsertOrUpdate(Article article);

    ResultList getSelectBySelectives(Article params);

    ResultList getHotArticleList();

    ResultList getNewArticle();

    ResultList getESArticleByTitleOrType(String token,RequestParamsESArt params);

    ResultList selectByTokenWithUsr(HttpServletRequest request, RequestParams params);

    ResultList tapArticleLike(RequestParamsRedisArtUsr params);

    ResultList tapArticleRead(RequestParamsRedisArtUsr params);
}
