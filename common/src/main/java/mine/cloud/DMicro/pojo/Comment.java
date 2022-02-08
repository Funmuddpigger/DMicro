package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Comment {
    private Integer comId;

    private Date comDate;

    private Integer usrId;

    private String comText;

    private Integer artId;

    private Integer comParId;

    private Long comLike;

    public void setComText(String comText) {
        this.comText = comText == null ? null : comText.trim();
    }
}