package mine.cloud.DMicro.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Article {
    private Integer artId;

    private String artTitle;

    private Date artPostime;

    private Integer usrId;

    private String artType;

    private User user;

    private Long artLike;

    private Long artRead;

    private String artText;

    private String artSummary;

    private String artUrl;

    public void setArtTitle(String artTitle) {
        this.artTitle = artTitle == null ? null : artTitle.trim();
    }

}