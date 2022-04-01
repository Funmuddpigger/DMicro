package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.MerkleNode;
import java.util.List;

public interface MerkleNodeMapper {
    int deleteByPrimaryKey(Integer merkleNodeId);

    int insertSelective(MerkleNode record);

    MerkleNode selectByPrimaryKey(Integer merkleNodeId);

    int updateByPrimaryKeySelective(MerkleNode record);

    List<MerkleNode> selectMerkleTree(MerkleNode merkleNode);

    int insertBatchMerkleNode(List<MerkleNode> merkleNodes);
}