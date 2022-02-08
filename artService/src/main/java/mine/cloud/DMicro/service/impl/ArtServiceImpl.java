package mine.cloud.DMicro.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.feignClients.UsrClient;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IArtServiceApi;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtServiceImpl implements IArtServiceApi  {

    //注入mapper
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UsrClient usrClient;

    @Override
    public Article selectByPrimaryKey(Integer artId) {
        Article article = null;
        if(artId != null){
            article = articleMapper.selectByPrimaryKey(artId);
            System.out.println(article);
        }else{

        }
        return article;
    }

    @Override
    public Article selectByPKWithUsr(Integer artId) {
        Article article = articleMapper.selectByPrimaryKey(artId);
        //利用feign调用
        User user = usrClient.selectByPK(article.getUsrId());

        article.setUser(user);
        return article;
    }

    @Override
    public Result selectByESKeyWord(String word, Integer page, Integer pageSize) {
        Article record = new Article();
        Result res = new Result();
        record.setArtTitle(word);

        PageHelper.startPage(page,pageSize);
        List<Article> articles = articleMapper.selectBySelective(record);
        PageInfo info = new PageInfo(articles);

        res.setCode(HttpStatusCode.HTTP_OK);
        res.setData(info);
        return res;
    }
}
