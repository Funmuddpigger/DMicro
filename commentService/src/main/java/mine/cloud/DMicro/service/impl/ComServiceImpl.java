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
import mine.cloud.DMicro.utils.StringHelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    public ResultList addCommentBySelectives(String token,Comment params) {
        User user = handleTokenAuthRes(token);
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        params.setComDate(new Date());
        params.setUsrId(user.getUsrId());
        params.setComParId(0);
        params.setComLike(0L);
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

    private User handleTokenAuthRes(String token){
        if(!StringHelperUtils.isNotEmpty(token)){
            return new User();
        }
        ResultList authRes = usrClient.getAuthAndCheck(token);
        try{
            if(authRes.getCode()!=HttpStatusCode.HTTP_OK){
                throw new RuntimeException("无效token,请校验");
            }
            //token有效
            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(authRes.getOneData());
            User usr = mapper.readValue(str, User.class);
            return usr;
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
