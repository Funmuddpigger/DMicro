package mine.cloud.DMicro.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mine.cloud.DMicro.blockChain.MerKleTreeNode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerkleNode {
    private Integer merkleNodeId;

    private Integer merkleNodeIndex;

    private String blockIndex;

    private Integer isLeafNode;

    private String hash;

    private String merkleData;

    public MerkleNode(MerKleTreeNode node){
        this.isLeafNode = node.getIsLeaf();
        this.hash = node.getData();

    }
}