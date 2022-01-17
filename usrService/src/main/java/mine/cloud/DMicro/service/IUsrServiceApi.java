package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.User;

public interface IUsrServiceApi {

    //select by pk
    User selectByPrimaryKey(Integer usrId);
}
