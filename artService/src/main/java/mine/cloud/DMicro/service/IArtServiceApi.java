package mine.cloud.DMicro.service;

import com.github.pagehelper.PageInfo;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.utils.Result;

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
    Result selectByESKeyWord(String word, Integer page, Integer pageSize);
}
