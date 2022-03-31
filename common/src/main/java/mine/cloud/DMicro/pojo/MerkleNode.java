package mine.cloud.DMicro.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerkleNode {
    private Integer merkleNodeId;

    private String merkleNodeIndex;

    private String blockIndex;

    private Integer isLeafNode;

    private String hash;

    private String merkleData;
}