package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.Comment;
import mine.cloud.DMicro.utils.ResultList;

public interface IComServiceApi {

    //add comment
    ResultList addCommentBySelectives(String token,Comment params);

    //update comment
    ResultList updateCommentBySelectives(Comment params);

    //del comment
    ResultList delCommentById(Integer ComId);

    //select comment
    ResultList selectCommentBySelectives(Comment params);

    ResultList delCommentBySelectives(Comment record);
}
