//package mine.cloud.DMicro;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import mine.cloud.DMicro.dao.ArticleMapper;
//import mine.cloud.DMicro.doc.ArticleDoc;
//import mine.cloud.DMicro.pojo.Article;
//import mine.cloud.DMicro.service.IArtServiceApi;
//import org.apache.http.HttpHost;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.reindex.DeleteByQueryRequest;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.BucketOrder;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.elasticsearch.search.sort.SortOrder;
//import org.elasticsearch.search.suggest.Suggest;
//import org.elasticsearch.search.suggest.SuggestBuilder;
//import org.elasticsearch.search.suggest.SuggestBuilders;
//import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@SpringBootTest
//public class TestElasticSearch {
//
//    private RestHighLevelClient client;
//
//    @Autowired
//    private IArtServiceApi iArtServiceApi;
//
//    @Autowired
//    private ArticleMapper articleMapper;
//
//    @Test
//    public void testAddDocument() throws IOException {
//        Integer artId = 1;
//        //根据ID查询数据
//        Article article = iArtServiceApi.selectByPrimaryKey(artId);
//        //准备Request对象
//        IndexRequest request = new IndexRequest("article").id("100");
//        ArticleDoc articleDoc = new ArticleDoc(article);
//        //转json
//        ObjectMapper mapper = new ObjectMapper();
//        //准备JSON文档
//        request.source(mapper.writeValueAsString(articleDoc), XContentType.JSON);
//        //发送请求
//        client.index(request, RequestOptions.DEFAULT);
//    }
//
//    @Test
//    public void testGetDocument() throws IOException {
//        //准备Request对象
//        GetRequest request = new GetRequest("article","1");
//        //发送请求 得到响应
//        GetResponse response = client.get(request,RequestOptions.DEFAULT);
//        //解析响应结果----转换为字符串
//        String json = response.getSourceAsString();
//        //反序列化为对象
//        ObjectMapper mapper = new ObjectMapper();
//        Article article = mapper.readValue(json, Article.class);
//        System.out.println(article);
//    }
//
//    @Test
//    public void testBulkAddDocument() throws IOException {
//        Article article = new Article();
//        article.setArtTitle("");
//        //根据ID查询数据
//        List<Article> articles = articleMapper.selectBySelective(article);
//        //准备Request对象
//        BulkRequest bulkRequest = new BulkRequest();
//        ObjectMapper mapper = new ObjectMapper();
//        for(Article a : articles){
//            //转换为es索引库对应的实体类
//            ArticleDoc articleDoc = new ArticleDoc(a);
//            //转json
//            bulkRequest.add(new IndexRequest("article")
//                    .id(articleDoc.getArtId().toString())
//                    .source(mapper.writeValueAsString(articleDoc),XContentType.JSON));
//        }
//        client.bulk(bulkRequest, RequestOptions.DEFAULT);
//    }
//
//    @Test
//    void testMatchAll() throws IOException {
//        //准备Request
//        SearchRequest request = new SearchRequest("article");
//        //准备DSL
//        request.source().query(QueryBuilders.matchAllQuery());
//        //发送请求
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        //解析结果
//        handleResponse(response);
//
//    }
//
//    @Test
//    void testMatch() throws IOException {
//        //准备Request
//        SearchRequest request = new SearchRequest("article");
//        //准备DSL
//        request.source().query(QueryBuilders.matchQuery("searchText","美国"));
//        //发送请求
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        //解析结果
//        handleResponse(response);
//    }
//
//    @Test
//    void testBool() throws IOException {
//        //准备Request
//        SearchRequest request = new SearchRequest("article");
//        //准备DSL
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must(QueryBuilders.termQuery("artTitle.title","美国密歇根州霍兰德（Holland）市议会近日批准了全球第二大电池制造商 LG 新能源"));
//        //发送请求
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        //解析结果
//        handleResponse(response);
//    }
//    @Test
//    void  searchHotWord() throws IOException {
//
//            SearchRequest searchRequest = new SearchRequest("article");//indexName是索引名称
//            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//            searchRequest.source(searchSourceBuilder);
//            //聚合分析查询出现次数最多的10个词汇，hotWord是聚合名称，name是es的字段名
//            TermsAggregationBuilder keyword_agg = AggregationBuilders.terms("hotWord").field("name").size(10).order(BucketOrder.count(false));
//            searchSourceBuilder.aggregation(keyword_agg);
//            searchRequest.source(searchSourceBuilder);
//            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//            Aggregations aggregations = response.getAggregations();
//
//            Terms term = aggregations.get("hotWord");
//            List<Terms.Bucket> buckets = (List<Terms.Bucket>) term.getBuckets();
//            List<String> hotWords = new ArrayList<>();
//            for (Terms.Bucket bucket : buckets) {
//                String key = (String) bucket.getKey();
//                long docCount = bucket.getDocCount();
//                hotWords.add(key);
//                System.out.println("热词:"+key+"数量为："+docCount);
//            }
//
//    }
//
//
//    @Test
//    void testPageAndSort() throws IOException {
//        //准备Request
//        SearchRequest request = new SearchRequest("article");
//        //准备DSL
//        request.source().query(QueryBuilders.matchAllQuery());
//        //排序,分页
//        request.source().sort("artRead", SortOrder.DESC).sort("artLike",SortOrder.DESC).from(0).size(2);
//        //发送请求
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        //解析结果
//        handleResponse(response);
//    }
//
//    @Test
//    void testHightLight() throws IOException {
//        //准备Request
//        SearchRequest request = new SearchRequest("article");
//        //准备DSL
//        request.source().query(QueryBuilders.multiMatchQuery("谷歌发布Android 12L最后一个Beta更新","artTitle","artSummary","artType"));
//        //request.source().query(QueryBuilders.matchQuery("artTitle","美国"));
//        //高亮
//        request.source().highlighter(new HighlightBuilder()
//                .field("artTitle").field("artSummary").requireFieldMatch(false).preTags("<em style='color:red;'>").postTags("<em>"));
//        //发送请求
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        //解析结果
//        handleResponse(response);
//    }
//
//    @Test
//    void testSuggest() throws IOException {
//        SearchRequest request = new SearchRequest("article");
//        //DSL
//        request.source().suggest(new SuggestBuilder().addSuggestion(
//           "suggestions",
//                SuggestBuilders.completionSuggestion("suggestion")
//                .prefix("m")
//                .skipDuplicates(true)
//                .size(10)
//        ));
//        //发起请求
//        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//        //解析结果
//        Suggest suggest = response.getSuggest();
//        CompletionSuggestion suggestion  = response.getSuggest().getSuggestion("suggestions");
//        //遍历自动补全数值数组
//        List<CompletionSuggestion.Entry.Option> options = suggestion.getOptions();
//        //遍历
//        for(CompletionSuggestion.Entry.Option option : options){
//            String res = option.getText().toString();
//            String id = option.getHit().getId();
//            System.out.println(res);
//        }
//    }
//
//    private void handleResponse(SearchResponse response) throws com.fasterxml.jackson.core.JsonProcessingException {
//        SearchHits searchHits = response.getHits();
//        //得到文档总条数
//        long total = searchHits.getTotalHits().value;
//        System.out.println("共搜索到: " + total);
//        SearchHit[] hits1 = searchHits.getHits();
//
//        //反序列化
//        ObjectMapper mapper = new ObjectMapper();
//        for (SearchHit h : hits1) {
//            String json = h.getSourceAsString();
//            ArticleDoc article = mapper.readValue(json, ArticleDoc.class);
//            //获取高亮
//            Map<String, HighlightField> map = h.getHighlightFields();
//            if(!CollectionUtils.isEmpty(map)){
//                //覆盖非高亮结果
//                HighlightField titleLight = map.get("artTitle");
//                HighlightField summaryLight = map.get("artSummary");
//                if(!ObjectUtils.isEmpty(titleLight)){
//                    String artTitle = titleLight.getFragments()[0].string();
//                    article.setArtTitle(artTitle);
//                }
//                if(!ObjectUtils.isEmpty(summaryLight)){
//                    String artSummary = summaryLight.getFragments()[0].string();
//                    article.setArtSummary(artSummary);
//                }
//            }
//            System.out.println(article);
//        }
//    }
//    @Test
//    void testDeletedDoc() throws IOException {
//        // request
//        DeleteByQueryRequest request = new DeleteByQueryRequest("article");
//        // dsl send
//        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
//        queryBuilder.must(QueryBuilders.matchAllQuery());
//        request.setQuery(queryBuilder);
//        client.deleteByQuery(request,RequestOptions.DEFAULT);
//    }
//
//
//    @Test
//    public void testInit(){
//        System.out.println(client);
//    }
//
//    @BeforeEach
//    public void setUp(){
//        this.client = new RestHighLevelClient(RestClient.builder(
//                HttpHost.create("http://119.23.78.113:9200")
//        ));
//    }
//
//    @AfterEach
//    public void tearDown() throws IOException {
//        this.client.close();
//    }
//
//}
