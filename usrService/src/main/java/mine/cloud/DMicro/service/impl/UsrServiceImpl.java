package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.dao.UserMapper;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IUsrServiceApi;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.JwtUtil;
import mine.cloud.DMicro.utils.Result;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class UsrServiceImpl implements IUsrServiceApi , UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User selectByPrimaryKey(Integer usrId) {
        return userMapper.selectByPrimaryKey(usrId);
    }

    @Override
    public ResultList selectOneBySelective(User user) {
        ResultList res = new ResultList();
        User usr = userMapper.selectOneBySelective(user);
        res.setOneData(usr);
        if(usr!=null){
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("登陆成功");
        }else{
            res.setCode(HttpStatusCode.HTTP_NOT_AUTHORITATIVE);
            res.setMsg("登陆失败");
        }
        return res;
    }

    @Override
    public ResultList updateUserBySelective(User user) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        userMapper.updateByPrimaryKeySelective(user);
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList insertUserBySelective(User user) {
        ResultList res = new ResultList();
        Date date = new Date();
        user.setUsrCreateTime(date);
        user.setUsrMoney(100L);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUsrPassword(passwordEncoder.encode(user.getUsrPassword()));
        userMapper.insertSelective(user);

        //userId通过mapper带回来
        if(user.getUsrId()>0){
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            res.setOneData(user);
        }else{
            res.setCode(HttpStatusCode.HTTP_INTERNAL_ERROR);
            res.setMsg("fail");
        }
        return res;
    }

    @Override
    public ResultList loginUserBySecurity(User user) {
        ResultList res = new ResultList();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsrPhone(), user.getUsrPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }

        LoginUserDetailsImpl loginUser = (LoginUserDetailsImpl)authenticate.getPrincipal();
        Integer usrId = loginUser.getUser().getUsrId();
        String token = JwtUtil.createJWT(usrId.toString());
        //放入redis
        redisTemplate.opsForValue().set("loginUser:"+usrId,loginUser);

        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setOneData(token);
        return res;
    }


    //实现security 查询用户
    @Override
    public UserDetails loadUserByUsername(String usrPhone) throws UsernameNotFoundException {
        //查询用户信息
        User selectUsr = new User();
        selectUsr.setUsrPhone(usrPhone);
        User user = userMapper.selectOneBySelective(selectUsr);

        if(Objects.isNull(user)){
            throw new RuntimeException("用户或者密码错误");
        }
        //查询权限信息

        //封装数据

        return new LoginUserDetailsImpl(user);
    }
}
