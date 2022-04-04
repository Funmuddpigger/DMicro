package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Info;

import java.util.List;

public interface InfoMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(Info record);

    int insertSelective(Info record);

    Info selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(Info record);

    int updateByPrimaryKey(Info record);

    int insertBatchBySelective(List<Info> records);

    List<Info> selectBySelectvie(Info record);
}