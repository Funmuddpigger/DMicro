package mine.cloud.DMicro.params;

import lombok.Data;

@Data
public class RequestParams {
    //查询输入的key/token
    private String key;
    private String token;
    private Integer page;
    private Integer pageSize;
    //查询的排序条件
    private String sortBy;

    private String upDown;
}
