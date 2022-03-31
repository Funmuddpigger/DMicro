package mine.cloud.DMicro.service;

import mine.cloud.DMicro.blockChain.Block;
import mine.cloud.DMicro.pojo.Blockchain;
import mine.cloud.DMicro.pojo.MerkleNode;
import mine.cloud.DMicro.utils.ResultList;

import java.util.List;

public interface IBlockChainService {
    ResultList insertBlock(MerkleNode node);

    ResultList queryBlockChainBlock(MerkleNode node);
}
