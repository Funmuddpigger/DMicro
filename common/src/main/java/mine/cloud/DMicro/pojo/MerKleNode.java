package mine.cloud.DMicro.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerKleNode {
    private MerKleNode lChild;
    private MerKleNode rChild;
    private String data;
}
