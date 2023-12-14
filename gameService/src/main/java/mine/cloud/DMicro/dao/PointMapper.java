package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Point;

public interface PointMapper {
    int deleteByPrimaryKey(Integer pointId);

    int insert(Point record);

    int insertSelective(Point record);

    Point selectByPrimaryKey(Integer pointId);

    int updateByPrimaryKeySelective(Point record);

    int updateByPrimaryKey(Point record);
}