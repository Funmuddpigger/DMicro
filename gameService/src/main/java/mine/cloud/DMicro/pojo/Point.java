package mine.cloud.DMicro.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Point {
    private Integer pointId;

    private Integer mapId;

    private String pointName;

    private Integer pointAbility;

    private Integer pointPrice;

    private Integer pointPre;

    private BigDecimal pointIncreasement;
}