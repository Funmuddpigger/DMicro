package mine.cloud.DMicro;

import com.fasterxml.jackson.databind.ObjectMapper;
import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.doc.ArticleDoc;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.service.IArtServiceApi;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        ArticleDoc articleDoc = new ArticleDoc(article);
        //转json
        ObjectMapper mapper = new ObjectMapper();
        //准备JSON文档
        request.source(mapper.writeValueAsString(articleDoc), XContentType.JSON);
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
            //转换为es索引库对应的实体类
            ArticleDoc articleDoc = new ArticleDoc(a);
            //转json
            bulkRequest.add(new IndexRequest("article")
                    .id(articleDoc.getArtId().toString())
                    .source(mapper.writeValueAsString(articleDoc),XContentType.JSON));
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
        handleResponse(response);

    }

    @Test
    void testMatch() throws IOException {
        //准备Request
        SearchRequest request = new SearchRequest("article");
        //准备DSL
        request.source().query(QueryBuilders.matchQuery("searchText","美国"));
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        handleResponse(response);
    }

    @Test
    void testBool() throws IOException {
        //准备Request
        SearchRequest request = new SearchRequest("article");
        //准备DSL
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("artTitle","美国密歇根州霍兰德（Holland）市议会近日批准了全球第二大电池制造商 LG 新能源"));
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        handleResponse(response);
    }

    @Test
    void testPageAndSort() throws IOException {
        //准备Request
        SearchRequest request = new SearchRequest("article");
        //准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        //排序,分页
        request.source().sort("artRead", SortOrder.DESC).sort("artLike",SortOrder.DESC).from(0).size(2);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        handleResponse(response);
    }

    @Test
    void testHightLight() throws IOException {
        //准备Request
        SearchRequest request = new SearchRequest("article");
        //准备DSL
        request.source().query(QueryBuilders.matchQuery("artType","芯片"));
        //高亮
        request.source().highlighter(new HighlightBuilder()
                .field("artType").requireFieldMatch(false));
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        handleResponse(response);
    }

    @Test
    void testSuggest() throws IOException {
        SearchRequest request = new SearchRequest("article");
        //DSL
        request.source().suggest(new SuggestBuilder().addSuggestion(
           "suggestions",
                SuggestBuilders.completionSuggestion("suggestion")
                .prefix("mg")
                .skipDuplicates(true)
                .size(10)
        ));
        //发起请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        CompletionSuggestion suggestion  = response.getSuggest().getSuggestion("suggestions");
        //遍历自动补全数值数组
        List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
        //遍历
        for(CompletionSuggestion.Entry.Option option : options){
            String res = option.getText().toString();
            System.out.println(res);
        }
    }

    private void handleResponse(SearchResponse response) throws com.fasterxml.jackson.core.JsonProcessingException {
        SearchHits searchHits = response.getHits();
        //得到文档总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到: " + total);
        SearchHit[] hits1 = searchHits.getHits();

        //反序列化
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit h : hits1) {
            String json = h.getSourceAsString();
            ArticleDoc article = mapper.readValue(json, ArticleDoc.class);
            //获取高亮
            Map<String, HighlightField> map = h.getHighlightFields();
            if(!CollectionUtils.isEmpty(map)){
                HighlightField light = map.get("artType");
                String artType = light.getFragments()[0].string();
                //覆盖非高亮结果
                article.setArtType(artType);

            }
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
                HttpHost.create("http://119.23.78.113:9200")
        ));
    }

    @AfterEach
    public void tearDown() throws IOException {
        this.client.close();
    }

}
