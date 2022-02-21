package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.utils.ResultList;

public interface IUsrServiceApi {

    //select by pk
    User selectByPrimaryKey(Integer usrId);

    //select by selective
    ResultList selectOneBySelective(User user);

    //update
    ResultList updateUserBySelective(User user);

    //insert
    ResultList insertUserBySelective(User user);

    ResultList loginUserBySecurity(User user);
}
