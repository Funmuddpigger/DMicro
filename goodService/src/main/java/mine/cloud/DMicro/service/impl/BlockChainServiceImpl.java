package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.dao.BlockchainMapper;
import mine.cloud.DMicro.dao.MerkleNodeMapper;
import mine.cloud.DMicro.pojo.MerkleNode;
import mine.cloud.DMicro.service.IBlockChainService;
import mine.cloud.DMicro.service.IMerkleService;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockChainServiceImpl implements IBlockChainService , IMerkleService {

    @Autowired
    private BlockchainMapper blockchainMapper;

    @Autowired
    private MerkleNodeMapper merkleNodeMapper;

    @Override
    public ResultList insertBlock(MerkleNode node) {
        return null;
    }

    @Override
    public ResultList queryBlockChainBlock(MerkleNode node) {
        return null;
    }

    @Override
    public int insertMerkleNode(MerkleNode node) {
        return 0;
    }

    @Override
    public List<MerkleNode> queryMerkleNodeBySelective(MerkleNode node) {
        return null;
    }
}
