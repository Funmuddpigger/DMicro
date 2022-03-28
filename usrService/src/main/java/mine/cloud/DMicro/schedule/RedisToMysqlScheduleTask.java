package mine.cloud.DMicro.schedule;

import mine.cloud.DMicro.dao.UserMapper;
import mine.cloud.DMicro.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@Component
public class RedisToMysqlScheduleTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Scheduled(cron = "0 0 20 * * ? ")
    public void syncUsrDataRedisToMysqlFans(){
        Long size = redisTemplate.opsForSet().size("change::user");
        List<Integer> followers = redisTemplate.opsForSet().pop("change::user", size);
        HashMap<Integer, Long> recordMap = new HashMap<>();
        for (Integer usrId : followers){
            Long fans = redisTemplate.opsForSet().size("usr:fans:" + usrId);
            recordMap.put(usrId,fans);
        }
        if(!CollectionUtils.isEmpty(recordMap)){
            userMapper.updateByPrimaryKeyFansForeach(recordMap);
        }
    }
}
