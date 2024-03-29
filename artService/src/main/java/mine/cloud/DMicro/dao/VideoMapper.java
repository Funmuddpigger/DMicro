package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Video;

import java.util.List;

public interface VideoMapper {
    int deleteByPrimaryKey(Integer videoId);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Integer videoId);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    List<Video> selectBySelective(Video record);
}