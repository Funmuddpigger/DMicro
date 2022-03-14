package mine.cloud.DMicro.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.pojo.Article;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
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

    @Autowired
    private RestHighLevelClient client;

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


    /**
     * 定时任务每天21点执行
     * 同步es数据
     */
    @Scheduled(cron = "0 0 21 * * ? ")
    public void syncBatchArticleDataToES(){
        //先删除原有数据，即使直接更新，es也会删除原文档再添加
        try {
            // request
            DeleteByQueryRequest request = new DeleteByQueryRequest("article");
            // dsl send
            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            queryBuilder.must(QueryBuilders.matchAllQuery());
            request.setQuery(queryBuilder);
            client.deleteByQuery(request, RequestOptions.DEFAULT);

            Article article = new Article();
            //根据ID查询数据
            List<Article> articles = articleMapper.selectBySelective(article);
            //准备Request对象
            BulkRequest bulkRequest = new BulkRequest();
            ObjectMapper mapper = new ObjectMapper();
            for(Article a : articles){
                //转换为es索引库对应的实体类
                ArticleDoc articleDoc = new ArticleDoc(a);
                //转json
                bulkRequest.add(new IndexRequest("article")
                        .id(articleDoc.getArtId().toString())
                        .source(mapper.writeValueAsString(articleDoc), XContentType.JSON));
            }
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
