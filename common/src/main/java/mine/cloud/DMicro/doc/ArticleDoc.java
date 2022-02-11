package mine.cloud.DMicro.doc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mine.cloud.DMicro.pojo.Article;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleDoc {

    private Integer artId;

    private String artTitle;

    private Date artPostime;

    private Integer usrId;

    private String artType;

    private Long artLike;

    private Long artRead;

    private String artText;

    private String artSummary;

    private List<String> suggestion;

    public ArticleDoc() {
    }

    public ArticleDoc(Article article) {
        this.artId = article.getArtId();
        this.artTitle = article.getArtTitle();
        this.artPostime = article.getArtPostime();
        this.usrId = article.getUsrId();
        this.artType = article.getArtType();
        this.artLike = article.getArtLike();
        this.artRead = article.getArtRead();
        this.artText = article.getArtText();
        this.artSummary = article.getArtSummary();
        this.suggestion = Arrays.asList(this.artTitle,this.artType);
    }
}
