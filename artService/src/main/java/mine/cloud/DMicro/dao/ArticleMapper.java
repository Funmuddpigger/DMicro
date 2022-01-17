package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


public interface ArticleMapper {
    int deleteByPrimaryKey(Integer artId);

    int insert(Article record);

    int insertSelective(Article record);


    Article selectByPrimaryKey(Integer artId);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);
}