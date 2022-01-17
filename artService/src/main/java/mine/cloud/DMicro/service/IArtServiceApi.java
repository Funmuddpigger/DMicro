package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.Article;

/**
 * Api:
 *  对内服务,不对外进行接口暴露
 */
public interface IArtServiceApi {

    //select by ID
    Article selectByPrimaryKey(Integer artId);

    //select by ID with Usr
    Article selectByPKWithUsr(Integer artId);
}
