package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.user;

public interface userMapper {
    int deleteByPrimaryKey(Integer usrId);

    int insert(user record);

    int insertSelective(user record);

    user selectByPrimaryKey(Integer usrId);

    int updateByPrimaryKeySelective(user record);

    int updateByPrimaryKey(user record);
}