package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Article;

import java.util.List;


public interface ArticleMapper {
    int deleteByPrimaryKey(Integer artId);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer artId);

    List<Article> selectBySelective(Article record);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);
}