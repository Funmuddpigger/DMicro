package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class user {
    private Integer usrId;

    private String usrNickname;

    private String usrPhone;

    private String usrText;

    private Long usrMoney;
}