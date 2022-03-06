package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Topic {
    private Integer topicId;

    private String topicText;

    private String topicDescribe;

    private Date topicCreateTime;

    private Long topicQuote;

    private Integer topicArticleId;

    private Integer topicCreateUsr;

}