package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.TopUsr;

public interface TopUsrMapper {
    int deleteByPrimaryKey(Integer topicUsrId);

    int insert(TopUsr record);

    int insertSelective(TopUsr record);

    TopUsr selectByPrimaryKey(Integer topicUsrId);

    int updateByPrimaryKeySelective(TopUsr record);

    int updateByPrimaryKeyWithBLOBs(TopUsr record);

    int updateByPrimaryKey(TopUsr record);
}