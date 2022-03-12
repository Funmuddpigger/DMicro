package mine.cloud.DMicro.service.impl;

import io.jsonwebtoken.Claims;
import mine.cloud.DMicro.dao.UserMapper;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IUsrServiceApi;
import mine.cloud.DMicro.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UsrServiceImpl implements IUsrServiceApi , UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResultList selectByPrimaryKey(String token, Integer id) {
        ResultList res = new ResultList();
        if (!ObjectUtils.isEmpty(id)){
            User user = userMapper.selectByPrimaryKey(id);
            res.setOneData(user);
        }else{
            res = checkTokenAndUsr(token);
        }
        return res;
    }

    @Override
    public ResultList selectOneBySelective(String token,User user) {
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
        user.setUsrFans(0L);
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
        //放入redis,设置过期时间为一天
        redisTemplate.opsForValue().set("loginUser:"+usrId,loginUser.getUser(),24*60*60, TimeUnit.SECONDS);

        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setOneData(token);
        return res;
    }

    @Override
    public ResultList logoutBySecurity() {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setMsg("ok");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User usr = (User) authentication.getPrincipal();
        Integer usrId = usr.getUsrId();
        redisTemplate.delete("loginUser:"+usrId);
        return res;
    }

    @Override
    public ResultList checkTokenAndUsr(String token) {
        ResultList res = new ResultList();
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String usrId = claims.getSubject();
            User user = userMapper.selectByPrimaryKey(Integer.valueOf(usrId));
            res.setMsg("ok");
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setOneData(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 每个用户维护一个hash对应粉丝,hash.size就是粉丝数
     * @param followUsrId
     * @param request
     * @return
     */
    @Override
    public ResultList followUser(Integer followUsrId, HttpServletRequest request) {
        ResultList res = new ResultList();
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);
        try {
            Integer usrId = handleRequestGetToken(request);
            User user = userMapper.selectByPrimaryKey(usrId);

            if(usrId == followUsrId){
                return res;
            }

            redisTemplate.opsForSet().add("change::user:" , followUsrId);
            Boolean exist = redisTemplate.opsForSet().isMember("follow:usr:" + usrId, followUsrId);
            //if not exist put / exist del
            if(!exist){
                //follow who
                redisTemplate.opsForSet().add("follow:usr:" + usrId, followUsrId);
                //who is his fans
                redisTemplate.opsForSet().add("fans:usr:" + followUsrId, usrId);
            }else{
                //unfollow who
                redisTemplate.opsForSet().remove("follow:usr:" + usrId, followUsrId);
                //who is not his fans
                redisTemplate.opsForSet().remove("fans:usr:" + followUsrId, usrId);
            }
            //exist following or not
            res.setOneData(!exist);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public ResultList getFollowUser(HttpServletRequest request,Integer usrId) {
        try {
            ResultList res = new ResultList();
            if(!Objects.isNull(request)){
                //request is not null ,query myself
                usrId = handleRequestGetToken(request);
            }
            Long size = redisTemplate.opsForSet().size("follow:usr:" + usrId);
            List<Integer> followIds = redisTemplate.opsForSet().pop("follow:usr:" + usrId, size);
            List<User> users = userMapper.selectBatchByIds(followIds);
            res.setData(users);
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultList getFanAndNum(HttpServletRequest request,Integer tapUsrId) {
        try {
            ResultList res = new ResultList();
            Integer currentUsrId = handleRequestGetToken(request);
            //usrId is null ,query mine
            if(Objects.isNull(tapUsrId)){
                tapUsrId = currentUsrId;
            }
            Long size = redisTemplate.opsForSet().size("fans:usr:" + tapUsrId);
            Cursor<Integer> cursor = redisTemplate.opsForSet().scan("fans:usr:" + tapUsrId, ScanOptions.NONE);

            //no fans jump
            ArrayList<Integer> fanIds = new ArrayList<>();
            while(cursor.hasNext()){
                fanIds.add(cursor.next());
            }

            if(fanIds.size()>0){
                List<User> users = userMapper.selectBatchByIds(fanIds);
                res.setData(users);
                Boolean isFollow = redisTemplate.opsForSet().isMember("fans:usr:" + tapUsrId, currentUsrId);
                HashMap<String, Object> map = new HashMap<>();
                map.put("isFollowed",isFollow);
                res.setMapData(map);
            }

            //close
            cursor.close();
            //set res
            res.setOneData(size);
            res.setCode(HttpStatusCode.HTTP_OK);
            res.setMsg("ok");
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultList upLoadImageOrFile(MultipartFile file, HttpServletRequest request) {
        ImageUploadUtils imageUploadUtils = new ImageUploadUtils();
        ResultList res = imageUploadUtils.upLoadFileOrImages(file, request);
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
        //查询权限信息 ---还未设置权限角色

        //封装数据
        return new LoginUserDetailsImpl(user);
    }

    /**
     * 统一处理token
     * @param request
     * @return
     * @throws Exception
     */
    private Integer handleRequestGetToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        return Integer.valueOf(claims.getSubject());
    }

}
