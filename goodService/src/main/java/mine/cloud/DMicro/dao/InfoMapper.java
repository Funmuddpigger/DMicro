package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Info;

public interface InfoMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(Info record);

    int insertSelective(Info record);

    Info selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(Info record);

    int updateByPrimaryKey(Info record);
}