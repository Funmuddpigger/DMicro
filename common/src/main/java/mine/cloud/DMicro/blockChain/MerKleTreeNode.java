package mine.cloud.DMicro.blockChain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerKleTreeNode {
    private MerKleTreeNode lChild;
    private MerKleTreeNode rChild;
    private String data;
    private Integer isLeaf;
    private Integer infoId;
}
