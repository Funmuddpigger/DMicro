package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.order;

public interface orderMapper {
    int deleteByPrimaryKey(Integer ordId);

    int insert(order record);

    int insertSelective(order record);

    order selectByPrimaryKey(Integer ordId);

    int updateByPrimaryKeySelective(order record);

    int updateByPrimaryKey(order record);
}