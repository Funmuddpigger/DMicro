package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.Blockchain;

public interface BlockchainMapper {
    int deleteByPrimaryKey(Integer blockId);

    int insertSelective(Blockchain record);

    Blockchain selectByPrimaryKey(Integer blockId);

    int updateByPrimaryKeySelective(Blockchain record);

}