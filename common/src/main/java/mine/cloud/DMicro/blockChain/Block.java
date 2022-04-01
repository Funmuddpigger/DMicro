package mine.cloud.DMicro.blockChain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class Block {

    private String blockHash;
    private String previousHash;
    private Map<String,String> data;
    private Long timeStamp;
    private String MerkleRoot;

    public Block(String previousHash, Map<String,String> data, Long timeStamp,String merkleRoot) {
        this.previousHash = previousHash;
        this.data = data;
        data.put("prevBlock",previousHash);
        data.put("MerkleRoot",merkleRoot);
        this.timeStamp = timeStamp;
        this.blockHash = BlockHashAlgoUtils.encodeDataBySHA_256(data);
        this.MerkleRoot = merkleRoot;
    }
}
