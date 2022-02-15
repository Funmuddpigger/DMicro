package mine.cloud.DMicro.service;

import com.github.pagehelper.PageInfo;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.utils.Result;

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
    Result selectByESKeyWord(String word, Integer page, Integer pageSize,String sortBy,String upDown);

    List<String> getESSuggestWord(String suggestKey);

    //insert
    Result saveArticle(Article article);

    //uodate
    Result updateArticle(Article article);

    //del
    Result deleteArticle(Integer id);

    void esArtDelete(Integer artId);

    void esArtInsertOrUpdate(Article article);

    Result getSelectBySelectives(Article params);
}
