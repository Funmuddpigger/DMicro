package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Blockchain;

import java.util.List;

public interface BlockchainMapper {
    int deleteByPrimaryKey(Integer blockId);

    int insertSelective(Blockchain record);

    Blockchain selectByPrimaryKey(Integer blockId);

    int updateByPrimaryKeySelective(Blockchain record);

    List<Blockchain> selectBlockChainBlock(Blockchain record);
}