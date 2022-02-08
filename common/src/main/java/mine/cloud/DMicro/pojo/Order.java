package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Order {
    private Integer ordId;

    private Integer usrId;

    private Integer goodId;

    private Long ordTotal;
}