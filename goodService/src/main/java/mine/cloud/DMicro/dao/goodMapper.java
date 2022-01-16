package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.good;

public interface goodMapper {
    int deleteByPrimaryKey(Integer goodId);

    int insert(good record);

    int insertSelective(good record);

    good selectByPrimaryKey(Integer goodId);

    int updateByPrimaryKeySelective(good record);

    int updateByPrimaryKey(good record);
}