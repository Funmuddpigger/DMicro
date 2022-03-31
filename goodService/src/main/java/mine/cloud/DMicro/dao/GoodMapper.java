package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Good;

import java.util.List;

public interface GoodMapper {
    int deleteByPrimaryKey(Integer goodId);

    int insertSelective(Good record);

    Good selectByPrimaryKey(Integer goodId);

    int updateByPrimaryKeySelective(Good record);

    List<Good> selectByPrimaryKeySelective(Good record);
}