package mine.cloud.DMicro;

import mine.cloud.DMicro.dao.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testRedisValue(){
        redisTemplate.opsForValue().set("1111aaa","aaa78798");
        Object res =  redisTemplate.opsForValue().get("1111aaa");
        System.out.println(res);
    }

    @Test
    public void syncArticleDataRedisToMysqlRead(){
        Cursor<Map.Entry<Integer,Long>> cursor = redisTemplate.opsForHash().scan("read::article", ScanOptions.NONE);
        HashMap<Integer, Long> map = new HashMap<>();
        while(cursor.hasNext()){
            Map.Entry<Integer, Long> entry = cursor.next();
            map.put(entry.getKey(), entry.getValue());
        }
        if(!CollectionUtils.isEmpty(map)){
            articleMapper.updateByPrimaryKeyForeachRead(map);
        }
        try {
            cursor.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void syncArticleDataRedisToMysqlLike(){
        //get 修改过的artid 数量
        Long size = redisTemplate.opsForSet().size("change::article");
        List<Integer> pop = redisTemplate.opsForSet().pop("change::article", size);
        redisTemplate.delete("change::article");
        HashMap<Integer, Long> recordMap = new HashMap<>();
        //foreach 今日修改过的article.artId
        for(int artId: pop){
            //得到现在点赞的人数
            Long artLike = redisTemplate.opsForSet().size("likeUser:article:" + artId);
            recordMap.put(artId,artLike);
        }
        //update foreach mysql
        if(!CollectionUtils.isEmpty(recordMap)){
            articleMapper.updateByPrimaryKeyForeachLike(recordMap);
        }
    }
}
