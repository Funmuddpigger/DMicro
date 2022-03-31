package mine.cloud.DMicro.blockChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MerKleTreeUtils {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static List<MerKleNode> buildInitMerKleHashNode(List<Map<String,String>> list){
        ArrayList<MerKleNode> res = new ArrayList<>();
        for(Map<String,String> map : list){
            MerKleNode node = new MerKleNode(null,null,BlockHashAlgoUtils.encodeDataBySHA_256(map));
            res.add(node);
        }
        return res;
    }
    //接收分片后的数据，然后生成一棵树
    public static MerKleNode generateMerKleRootHash(List<MerKleNode> data){
        int size = data.size();
        /**
         * 计算能容纳size最接近的的2的幂次的那个数 e.g.
         * init: 0001101011011001
         * >>>1: 0001101010000100 | 0000110101000010 = 000111111000110
         *                  ````````(无符号右移类推)
         *                  ====>>> 0001111111111111
         * res = 0001111111111111 + 1 = 0010000000000000 = 2^14
         */
        if((size & (size -1))!=0){
            int n = size;
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            n = n >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : n + 1;
            //计算差距
            int distance = n - size;
            for(int i =size-distance;i < size; i++){
                //复制对称数据，凑齐merkle树所有节点
                data.add(data.get(i));
            }
            size = n;
        }
        /**
         * 获取size的二进制形式，它的长度就是merkle树的高度
         */
        int height = Integer.toBinaryString(size).length();
        /**
         * 两两hash,构造merkle root
         * 最后一层已经通过函数buildInitMerKleHashNode构建好并传入
         * 此函数中
         */
        while(height-1>0){
            /**
             * 逐层构建，new temp(list) 作为新一层的数据赋值到data（list）上，循环
             *
             */
            ArrayList<MerKleNode> temp = new ArrayList<>();
            for(int j = 0; j < size ; j+=2){
                MerKleNode lChild = data.get(j);
                MerKleNode rChild = data.get(j+1);
                String hash = BlockHashAlgoUtils.encodeDataBySHA_256(lChild.getData() + rChild.getData());
                MerKleNode node = new MerKleNode(lChild, rChild, hash);
                temp.add(node);
            }
            //temp 下个循环进行新建回收，赋值data
            data = temp;
            height--;
            //size长度每次减半
            size = size >> 1;
        }
        return data.get(0);
    }
}
