package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.dao.ArticleMapper;
import mine.cloud.DMicro.feignClients.UsrClient;
import mine.cloud.DMicro.pojo.Article;
import mine.cloud.DMicro.pojo.User;
import mine.cloud.DMicro.service.IArtServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
