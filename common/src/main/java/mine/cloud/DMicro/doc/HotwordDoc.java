package mine.cloud.DMicro.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class HotwordDoc {

    private String searchText;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Field(type = FieldType.Date)
    private Date searchTime;

}
