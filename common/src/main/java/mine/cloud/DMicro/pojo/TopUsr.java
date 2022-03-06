package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class TopUsr extends Topic{
    private Integer topicUsrId;

    private Date topicUsrPostTime;

    private Integer usrId;

    private Integer topicId;

    private String topicUsrText;
}