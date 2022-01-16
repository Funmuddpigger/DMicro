package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.article;

public interface articleMapper {
    int deleteByPrimaryKey(Integer artId);

    int insert(article record);

    int insertSelective(article record);

    article selectByPrimaryKey(Integer artId);

    int updateByPrimaryKeySelective(article record);

    int updateByPrimaryKey(article record);
}