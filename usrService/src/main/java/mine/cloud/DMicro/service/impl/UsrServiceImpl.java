package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.dao.UserMapper;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IUsrServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsrServiceImpl implements IUsrServiceApi {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByPrimaryKey(Integer usrId) {
        return userMapper.selectByPrimaryKey(usrId);
    }
}
