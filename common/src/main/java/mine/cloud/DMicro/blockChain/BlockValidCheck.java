package mine.cloud.DMicro.blockChain;

import java.util.HashMap;
import java.util.List;

public class BlockValidCheck {
    /**
     * 校验区块数据是否被篡改
     * @return
     */
    public Boolean checkBlockDataValid(List<Block> blockChain) {
        int size = blockChain.size();
        //只有创世区块
        if (size <= 1) {
            return true;
        }

        //比对当前的数据hash，与生成是创建的hash是否相同(源数据完全相同并再次通过散列函数,则它将生成完全相同的散列,否则完全不一样)
        for (int i = 1; i < size; i++) {
            Block currentBlock = blockChain.get(i);
            Block previousBlock = blockChain.get(i - 1);
            //判断当前区块的hash和当前区块数据生成的hash
            if (!currentBlock.getBlockHash().equals(BlockHashAlgoUtils.encodeDataBySHA_256(currentBlock.getData()))) {
                System.out.println("Current hash not equals");
                return false;
            }
            //判断上一个区块的hash和新区块的上一个hash
            if (!previousBlock.getBlockHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hash not equals");
                return false;
            }
        }
        return true;
    }
}
