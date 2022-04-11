package mine.cloud.DMicro.dao;

import mine.cloud.DMicro.pojo.MerkleNode;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface MerkleNodeMapper {
    int deleteByPrimaryKey(Integer merkleNodeId);

    int insertSelective(MerkleNode record);

    MerkleNode selectByPrimaryKey(Integer merkleNodeId);

    int updateByPrimaryKeySelective(MerkleNode record);

    List<MerkleNode> selectBySelective(MerkleNode merkleNode);

    int insertBatchMerkleNode(List<MerkleNode> merkleNodes);

    List<MerkleNode> selectByMerkleIdx(@Param("list")ArrayList<Integer> list,@Param("block")Integer block);
}