package mine.cloud.DMicro.blockChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MerKleTreeUtils {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static List<MerKleTreeNode> buildInitMerKleHashNode(List<Map<String,String>> list){
        ArrayList<MerKleTreeNode> res = new ArrayList<>();
        for(Map<String,String> map : list){
            MerKleTreeNode node = new MerKleTreeNode(null,null,BlockHashAlgoUtils.encodeDataBySHA_256(map),1,-1);
            res.add(node);
        }
        return res;
    }
    /**
     *     接收分片后的数据，然后生成一棵树
     */
    public static MerKleTreeNode generateMerKleRootHash(List<MerKleTreeNode> data){
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

        int total = (int)(Math.pow(2,height)-1);
        /**
         * 输出叶子节点
         */
        int idxStart = (total +1) >> 1;
        for(MerKleTreeNode node : data){
            System.out.println(idxStart++);
        }
        /**
         * 两两hash,构造merkle root
         * 最后一层已经通过函数buildInitMerKleHashNode构建好并传入
         * 此函数中
         */
        while(height-1>0){
            /**
             * 总节点数 2^n-1
             * 每一层的最左节点序号
             */
            total = (int)(Math.pow(2,height-1)-1);
            idxStart = (total +1) >> 1;
            /**
             * 逐层构建，new temp(list) 作为新一层的数据赋值到data（list）上，循环
             */
            ArrayList<MerKleTreeNode> temp = new ArrayList<>();
            for(int j = 0; j < size ; j+=2){
                MerKleTreeNode lChild = data.get(j);
                MerKleTreeNode rChild = data.get(j+1);
                String hash = BlockHashAlgoUtils.encodeDataBySHA_256(lChild.getData() + rChild.getData());
                MerKleTreeNode node = new MerKleTreeNode(lChild, rChild, hash,0,-1);
                temp.add(node);
                //输出非叶节点的序号
                System.out.println(idxStart++);
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
