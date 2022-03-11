package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.utils.ResultList;

import javax.servlet.http.HttpServletRequest;

public interface IUsrServiceApi {

    //select by pk
    ResultList selectByPrimaryKey(String token ,Integer id);

    //select by selective
    ResultList selectOneBySelective(String token,User user);

    //update
    ResultList updateUserBySelective(User user);

    //insert
    ResultList insertUserBySelective(User user);

    //login
    ResultList loginUserBySecurity(User user);

    //logout
    ResultList logoutBySecurity();

    //check token
    ResultList checkTokenAndUsr(String token);

    ResultList followUser(Integer user, HttpServletRequest request);

    ResultList getFollowUser(HttpServletRequest request,Integer usrId);

    ResultList getFanAndNum(HttpServletRequest request,Integer usrId);
}
