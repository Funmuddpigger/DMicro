package mine.cloud.DMicro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic {
    private Integer topicId;

    private String topicText;

    private String topicDescribe;

    private Date topicCreateTime;

    private Long topicQuote;

    private Integer topicArticleId;

    private Integer topicCreateUsr;

}