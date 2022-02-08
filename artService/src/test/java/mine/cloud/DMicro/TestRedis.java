package mine.cloud.DMicro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisValue(){
        redisTemplate.opsForValue().set("1111aaa","aaa78798");
        Object res =  redisTemplate.opsForValue().get("1111aaa");
        System.out.println(res);
    }
}