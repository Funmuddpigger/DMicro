package mine.cloud.DMicro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.CommentMapper;
import mine.cloud.DMicro.feignClients.UsrClient;
import mine.cloud.DMicro.pojo.Comment;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IComServiceApi;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.Result;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ComServiceImpl implements IComServiceApi {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UsrClient usrClient;

    /**
     *  add comment
     * @param params
     * @return
     */
    @Override
    public ResultList addCommentBySelectives(Comment params) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        int count = commentMapper.insert(params);
        if(count >=1 ){
            res.setMsg("fail");
            res.setOneData(true);
        }else{
            res.setMsg("ok");
            res.setOneData(false);
        }
        return res;
    }

    /**
     * update comment
     * @param params
     * @return
     */
    @Override
    public ResultList updateCommentBySelectives(Comment params) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        commentMapper.insert(params);
        res.setMsg("ok");
        return res;
    }

    /**
     * del comment
     * @param comId
     * @return
     */
    @Override
    public ResultList delCommentById(Integer comId) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        commentMapper.deleteByPrimaryKey(comId);
        res.setMsg("ok");
        return res;
    }

    /**
     * select comment
     * @param params
     * @return
     */
    @Override
    public ResultList selectCommentBySelectives(Comment params) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        List<Comment> comments = commentMapper.selectBySelectives(params);

        ArrayList<HashMap> list = new ArrayList<>();
        for(Comment comment : comments){
            HashMap<String,Object> map = new HashMap<>();
            ResultList resultList = usrClient.selectByPK(null, comment.getUsrId());
            User usr = new ObjectMapper().convertValue(resultList.getOneData(), User.class);
            map.put("comment",comment);
            map.put("usr",usr);
            list.add(map);
        }
        res.setMsg("ok");
        res.setData(list);
        return res;
    }

    @Override
    public ResultList delCommentBySelectives(Comment record) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        commentMapper.deleteBySelectives(record);
        return null;
    }
}
