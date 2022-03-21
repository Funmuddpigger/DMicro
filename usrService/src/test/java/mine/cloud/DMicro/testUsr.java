package mine.cloud.DMicro;

import mine.cloud.DMicro.dao.UserMapper;
import mine.cloud.DMicro.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class testUsr {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void TestBCryptPassworEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456789");
        String encode2 = bCryptPasswordEncoder.encode("123456789");
        String encode3 = bCryptPasswordEncoder.encode("123456789");
        System.out.println(encode);
        System.out.println(encode2);
        System.out.println(encode3);
    }

    @Test
    public void syncUsrDataRedisToMysqlFans(){
        Long size = redisTemplate.opsForSet().size("change::user");
        List<Integer> followers = redisTemplate.opsForSet().pop("change::user", size);
        HashMap<Integer, Long> map = new HashMap<>();
        for (Integer usrId : followers){
            Long fans = redisTemplate.opsForSet().size("usr:fans:" + usrId);
            map.put(usrId,fans);
        }
        if(CollectionUtils.isEmpty(map)){
            userMapper.updateByPrimaryKeyFansForeach(map);
        }
    }
}
