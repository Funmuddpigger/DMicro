package mine.cloud.DMicro.utils;

import lombok.Data;

@Data
public class Result {
    private Integer code; //状态码
    private String msg; //消息
    private Object data; //数据对象
}
