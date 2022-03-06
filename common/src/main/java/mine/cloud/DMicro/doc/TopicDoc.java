package mine.cloud.DMicro.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mine.cloud.DMicro.pojo.Topic;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class TopicDoc {

    private Integer topicId;

    private String topicText;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Field(type = FieldType.Date)
    private Date topicCreateTime;

    private Integer topicArticleId;

    private Integer topicCreateUsr;

    private List<String> suggestion;

    public TopicDoc(){

    }

    public TopicDoc(Topic topic) {
        this.topicId = topic.getTopicId();
        this.topicText = topic.getTopicText();
        this.topicCreateTime = topic.getTopicCreateTime();
        this.topicArticleId = topic.getTopicArticleId();
        this.topicCreateUsr = topic.getTopicCreateUsr();
        this.suggestion = Arrays.asList(this.topicText);
    }
}
