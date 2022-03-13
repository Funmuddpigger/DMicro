package mine.cloud.DMicro.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mine.cloud.DMicro.pojo.Article;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleDoc {

    private Integer artId;

    private String artTitle;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Field(type = FieldType.Date)
    private Date artPostime;

    private Integer usrId;

    private String artType;

    private Long artLike;

    private Long artRead;

    private String artText;

    private String artSummary;

    private String artUrl;

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
        this.artUrl = article.getArtUrl();
        this.suggestion = Arrays.asList(this.artTitle,this.artType);
    }
}
