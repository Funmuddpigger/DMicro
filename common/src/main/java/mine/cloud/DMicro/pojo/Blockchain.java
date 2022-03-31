package mine.cloud.DMicro.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mine.cloud.DMicro.blockChain.Block;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Blockchain {
    private Integer blockId;

    private String blockPrev;

    private String blockData;

    private Long blockTimestamp;

    private String blockHash;

    private String blockMerkle;

    public Blockchain(Block block){
        this.blockPrev = block.getPreviousHash();
        this.blockHash = block.getBlockHash();
        this.blockMerkle = block.getMerkleRoot();
        this.blockTimestamp = new Date().getTime();
    }

}