package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ArticleMapper {
    int deleteByPrimaryKey(Integer artId);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer artId);

    List<Article> selectBySelective(Article record);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyForeachRead(@Param("recordMap") Map<Integer,Long> recordMap);

    int updateByPrimaryKeyForeachLike(@Param("recordMap") Map<Integer,Long> recordMap);
}