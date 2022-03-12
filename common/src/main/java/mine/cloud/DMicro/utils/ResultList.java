package mine.cloud.DMicro.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class ResultList implements Serializable {
    private Long total;
    private Integer code; //状态码
    private String msg; //消息
    private Object oneData;
    private List<?> data; //数据对象
    private Map<String,?> mapData;

}
