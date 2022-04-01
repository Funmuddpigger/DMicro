package mine.cloud.DMicro.service;

import mine.cloud.DMicro.blockChain.MerKleTreeNode;
import mine.cloud.DMicro.pojo.Blockchain;
import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.pojo.MerkleNode;
import mine.cloud.DMicro.utils.ResultList;

import java.util.List;
import java.util.Map;

public interface IMerkleService {

    int insertMerkleNode(MerkleNode node);

    List<MerkleNode> queryMerkleNodeBySelective(MerkleNode node);

    ResultList createBlockLinkWithData(List<Info> infos, String type);
}
