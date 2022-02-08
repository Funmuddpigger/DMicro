package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Good;

public interface goodMapper {
    int deleteByPrimaryKey(Integer goodId);

    int insert(Good record);

    int insertSelective(Good record);

    Good selectByPrimaryKey(Integer goodId);

    int updateByPrimaryKeySelective(Good record);

    int updateByPrimaryKey(Good record);
}