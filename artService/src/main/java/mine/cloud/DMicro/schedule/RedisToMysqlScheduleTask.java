package mine.cloud.DMicro.schedule;

import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RedisToMysqlScheduleTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定时任务每天20点执行
     * 同步阅读数
     */
    @Scheduled(cron = "0 0 20 * * ? ")
    public void syncArticleDataRedisToMysqlRead(){
        Cursor<Map.Entry<Integer,Long>> cursor = redisTemplate.opsForHash().scan("read::article", ScanOptions.NONE);
        HashMap<Integer, Long> map = new HashMap<>();
        while(cursor.hasNext()){
            Map.Entry<Integer, Long> entry = cursor.next();
            map.put(entry.getKey(), entry.getValue());
        }
        articleMapper.updateByPrimaryKeyForeachRead(map);
        try {
            cursor.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 定时任务每天20点执行
     * 同步点赞数
     */
    @Scheduled(cron = "0 0 20 * * ? ")
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
        articleMapper.updateByPrimaryKeyForeachLike(recordMap);
    }
}
