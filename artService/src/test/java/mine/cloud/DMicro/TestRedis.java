package mine.cloud.DMicro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testRedisList(){
        Object o = redisTemplate.opsForHash().get("read::article",9);
        System.out.println(o);
//        Cursor<Map.Entry<Integer,Long>> scan = redisTemplate.opsForHash().scan("read::article", ScanOptions.NONE);
//        HashMap<Integer, Long> map = new HashMap<>();
//        while(scan.hasNext()){
//            Map.Entry<Integer, Long> entry = scan.next();
////            map.put(entry.getKey(), entry.getValue());
//            System.out.println(entry.getKey() +"::" +entry.getValue() );


    }

    @Test
    public void testRedisNone(){
        //redisTemplate.delete("read::article");
        HashMap<Integer, Long> map = new HashMap<>();
        map.put(9,966L);
        map.put(2,462L);
        map.put(3,583L);
        map.put(11,644L);
        map.put(12,6536L);
        map.put(13,2060L);
        map.put(14,64L);
        map.put(15,235L);
        map.put(16,875L);
        map.put(10,324L);
        redisTemplate.opsForHash().putAll("read::article",map);
    }
}
