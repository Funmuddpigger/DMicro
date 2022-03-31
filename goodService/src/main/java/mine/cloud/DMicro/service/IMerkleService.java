package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.MerkleNode;

import java.util.List;

public interface IMerkleService {

    int insertMerkleNode(MerkleNode node);

    List<MerkleNode> queryMerkleNodeBySelective(MerkleNode node);
}
