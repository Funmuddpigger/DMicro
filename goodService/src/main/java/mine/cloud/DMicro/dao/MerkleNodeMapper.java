package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.MerkleNode;

public interface MerkleNodeMapper {
    int deleteByPrimaryKey(Integer merkleNodeId);

    int insertSelective(MerkleNode record);

    MerkleNode selectByPrimaryKey(Integer merkleNodeId);

    int updateByPrimaryKeySelective(MerkleNode record);
}