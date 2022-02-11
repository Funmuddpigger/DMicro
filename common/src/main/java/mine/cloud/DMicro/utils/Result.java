package mine.cloud.DMicro.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class Result implements Serializable {
    private Long total;
    private Integer code; //状态码
    private String msg; //消息
    private List<?> data; //数据对象

}
