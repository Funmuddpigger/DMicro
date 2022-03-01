package mine.cloud.DMicro.dao;


import mine.cloud.DMicro.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer usrId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer usrId);

    int updateByPrimaryKeySelective(User record);

    User selectOneBySelective(User record);

    int updateByPrimaryKeyFansForeach(@Param("recordMap") Map<Integer, Long> recordMap);

    List<User> selectBatchByIds(@Param("ids") List<Integer> ids);
}