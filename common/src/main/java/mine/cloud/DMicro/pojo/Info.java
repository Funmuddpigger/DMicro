package mine.cloud.DMicro.pojo;

import lombok.*;
import java.util.Date;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Info {
    private Integer infoId;

    private String infoMsg;

    private Integer goodId;

    private Integer usrId;

    private Date infoTime;
}