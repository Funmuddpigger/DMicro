package mine.cloud.DMicro.service;

import mine.cloud.DMicro.params.RequestParams;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.TopUsr;
import mine.cloud.DMicro.pojo.Topic;
import mine.cloud.DMicro.utils.ResultList;

import java.util.List;
import java.util.Map;

public interface ITopicService {
    //Search topic -sql
    ResultList selectTopicBySelectives(String token,Topic record);

    //add topic --sql
    ResultList addTopicBySelective(String token,Topic record);

    //select by KeyWord --es
    ResultList selectByESKeyWord(RequestParams params);

    //topic suggest es
    Map<String,String> getESSuggestWord(String suggestKey);

    //Post topic view --redis
    ResultList addTopicViewWithUsr(String token,TopUsr record);

    //delete topic view --redis
    ResultList delTopicViewWithUsr(String token,Integer id);

    //batch insert SQL
    void batchInsertToES();
    

    void esTopicInsertOrUpdate(Topic topic);

    void esTopicDelete(Integer id);
}
