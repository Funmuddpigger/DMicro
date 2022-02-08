package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Order;

public interface orderMapper {
    int deleteByPrimaryKey(Integer ordId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer ordId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}