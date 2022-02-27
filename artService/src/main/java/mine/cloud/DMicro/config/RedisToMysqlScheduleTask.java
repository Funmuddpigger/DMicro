package mine.cloud.DMicro.config;

import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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
     */
    @Scheduled(cron = "0 0 20 * * ? ")
    public void syncArticleDataRedisToMysql(){
        Cursor<Map.Entry<String,Object>> map = redisTemplate.opsForHash().scan("read::article", ScanOptions.NONE);
        while (map.hasNext()){
            Article article = new Article();
            Map.Entry<String, Object> entry = map.next();
            article.setArtId(Integer.valueOf(entry.getKey()));
            article.setArtRead((Long)entry.getValue());
            articleMapper.updateByPrimaryKeySelective(article);
        }


    }
}
