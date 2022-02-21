package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class User {
    private Integer usrId;

    private String usrNickname;

    private String usrPhone;

    private String usrText;

    private Long usrMoney;

    private Date usrCreateTime;

    private String usrPassword;
}