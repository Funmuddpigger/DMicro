package mine.cloud.DMicro.dao;


import mine.cloud.DMicro.pojo.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer usrId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer usrId);

    int updateByPrimaryKeySelective(User record);

    User selectOneBySelective(User record);
}