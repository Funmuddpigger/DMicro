package mine.cloud.DMicro;

import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.Result;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.awt.geom.QuadCurve2D;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class TestElasticSearch {

    private RestHighLevelClient client;

    @Autowired
    private IArtServiceApi iArtServiceApi;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testAddDocument() throws IOException {
        Integer artId = 1;
        //根据ID查询数据
        Article article = iArtServiceApi.selectByPrimaryKey(artId);
        //准备Request对象
        IndexRequest request = new IndexRequest("article").id(artId.toString());
        //转json
        ObjectMapper mapper = new ObjectMapper();
        //准备JSON文档
        request.source(mapper.writeValueAsString(article), XContentType.JSON);
        //发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    public void testGetDocument() throws IOException {
        //准备Request对象
        GetRequest request = new GetRequest("article","1");
        //发送请求 得到响应
        GetResponse response = client.get(request,RequestOptions.DEFAULT);
        //解析响应结果----转换为字符串
        String json = response.getSourceAsString();
        //反序列化为对象
        ObjectMapper mapper = new ObjectMapper();
        Article article = mapper.readValue(json, Article.class);
        System.out.println(article);
    }

    @Test
    public void testBulkAddDocument() throws IOException {
        Article article = new Article();
        article.setArtTitle("");
        //根据ID查询数据
        List<Article> articles = articleMapper.selectBySelective(article);
        //准备Request对象
        BulkRequest bulkRequest = new BulkRequest();
        ObjectMapper mapper = new ObjectMapper();
        for(Article a : articles){
            //转json
            bulkRequest.add(new IndexRequest("article")
                    .id(a.getArtId().toString())
                    .source(mapper.writeValueAsString(a),XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Test
    void testMatchAll() throws IOException {
        //准备Request
        SearchRequest request = new SearchRequest("article");
        //准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        SearchHits searchHits = response.getHits();
        //得到文档总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到: "+ total);
        SearchHit[] hits1 = searchHits.getHits();

        //反序列化
        ObjectMapper mapper = new ObjectMapper();
        for(SearchHit h : hits1){
            String json = h.getSourceAsString();
            Article article = mapper.readValue(json, Article.class);
            System.out.println(article);
        }

    }

    @Test
    public void testInit(){
        System.out.println(client);
    }

    @BeforeEach
    public void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://127.0.0.1:9200")
        ));
    }

    @AfterEach
    public void tearDown() throws IOException {
        this.client.close();
    }

}
