package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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

    public void setArtTitle(String artTitle) {
        this.artTitle = artTitle == null ? null : artTitle.trim();
    }

}