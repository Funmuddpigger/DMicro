package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Topic;
import net.sf.jsqlparser.statement.select.Top;

import java.util.List;

public interface TopicMapper  {
    int deleteByPrimaryKey(Integer topicId);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Integer topicId);

    List<Topic> selectBySelectives(Topic record);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);
}