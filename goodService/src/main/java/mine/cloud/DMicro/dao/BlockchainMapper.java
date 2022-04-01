package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Blockchain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface BlockchainMapper {
    int deleteByPrimaryKey(Integer blockId);

    int insertSelective(Blockchain record);

    Blockchain selectByPrimaryKey(Integer blockId);

    int updateByPrimaryKeySelective(Blockchain record);

    Blockchain selectBlockChainBlock(Blockchain record);

    List<Blockchain> selectLastOne(String type);

    List<Blockchain> selectBlockChainByParents(Blockchain block);
}