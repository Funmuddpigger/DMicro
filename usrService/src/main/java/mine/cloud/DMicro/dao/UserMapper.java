package mine.cloud.DMicro.dao;


import mine.cloud.DMicro.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer usrId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer usrId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}