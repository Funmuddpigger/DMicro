package mine.cloud.DMicro.service;

import mine.cloud.DMicro.blockChain.Block;
import mine.cloud.DMicro.pojo.Blockchain;
import mine.cloud.DMicro.pojo.MerkleNode;
import mine.cloud.DMicro.utils.ResultList;

import java.util.List;

public interface IBlockChainService {
    ResultList createFirstBlock(Blockchain block);

    ResultList queryBlockChainBlock(Blockchain block);

    List<MerkleNode> getCheckProof(Integer infoId);

    ResultList spvCheckMsgData(Integer infoId);
}
