package mine.cloud.DMicro.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class good {
    private Integer goodId;

    private String goodName;

    private BigDecimal goodPrice;

    private String goodArgs;

    private String goodText;

    public void setGoodName(String goodName) {
        this.goodName = goodName == null ? null : goodName.trim();
    }

    public void setGoodArgs(String goodArgs) {
        this.goodArgs = goodArgs == null ? null : goodArgs.trim();
    }


    public void setGoodText(String goodText) {
        this.goodText = goodText == null ? null : goodText.trim();
    }
}