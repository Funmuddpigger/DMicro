package mine.cloud.DMicro.blockChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BlockChainProgram {

    public static void main(String[] args) {
        ArrayList<Block> blockChain = new ArrayList<>();

        HashMap<String, String> map = new HashMap<>();
        map.put("GPU","GTX-3090");
        map.put("cpu","i9-9900k");
        Block block = new Block("0", map, new Date().getTime());
        blockChain.add(block);
        //hash: 19c039ad0f15bac1e4e45b39e5938e1e95b69f740db17b25359d2b4aa730b873
        System.out.println(block);

        HashMap<String, String> map1 = new HashMap<>();
        map.put("GPU","GTX-3080");
        map.put("cpu","i9-9900k");
        Block secondBlock = new Block(block.getBlockHash(), map1, new Date().getTime());
        blockChain.add(secondBlock);
        //cb97634350935773c2d95a78382f39228f7d17f1d7f52a870646e1489b0ce126
        System.out.println(secondBlock);
        System.out.println(blockChain);
    }
}
