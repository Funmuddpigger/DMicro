package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Comment;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer comId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer comId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectBySelectives(Comment record);
}