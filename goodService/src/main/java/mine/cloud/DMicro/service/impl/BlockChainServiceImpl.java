package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.blockChain.Block;
import mine.cloud.DMicro.blockChain.BlockHashAlgoUtils;
import mine.cloud.DMicro.blockChain.MerKleTreeNode;
import mine.cloud.DMicro.dao.BlockchainMapper;
import mine.cloud.DMicro.dao.MerkleNodeMapper;
import mine.cloud.DMicro.pojo.Blockchain;
import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.pojo.MerkleNode;
import mine.cloud.DMicro.service.IBlockChainService;
import mine.cloud.DMicro.service.IMerkleService;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class BlockChainServiceImpl implements IBlockChainService , IMerkleService {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    @Autowired
    private BlockchainMapper blockchainMapper;

    @Autowired
    private MerkleNodeMapper merkleNodeMapper;



    @Override
    public int insertMerkleNode(MerkleNode node) {
        return 0;
    }

    @Override
    public List<MerkleNode> queryMerkleNodeBySelective(MerkleNode node) {
        return null;
    }

    //生成创世区块
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultList createFirstBlock(Blockchain blockChain) {
        ResultList res = new ResultList();
        res.setCode(HttpStatusCode.HTTP_OK);

        //设置map数据,随机可变值
        HashMap<String, String> map = new HashMap<>();
        map.put("Msg1","This is first block" + new Date());
        Double v = Math.random() * Math.random();
        map.put("Msg2",v.toString());
        blockChain.setBlockTimestamp(new Date());
        blockChain.setBlockPrev("0");
        blockChain.setBlockMerkle("");
        blockChain.setBlockHash(BlockHashAlgoUtils.encodeDataBySHA_256(map));

        int i = blockchainMapper.insertSelective(blockChain);

        if(i<1){
            res.setMsg("fail");
        }
        res.setMsg("ok");
        return res;
    }
    //传入创世区块，查询链条
    @Override
    public ResultList queryBlockChainBlock(Blockchain block) {
        blockchainMapper.selectBlockChainByParents(block);
        return null;
    }


    @Override
    public ResultList createBlockLinkWithData(List<Info> infos,String type) {
        ResultList res = new ResultList();
        res.setMsg("ok");
        res.setCode(HttpStatusCode.HTTP_OK);

        List<MerKleTreeNode> merKleTreeNodes = buildInitMerKleHashNode(infos);
        MerKleTreeNode merKleRoot = generateMerKleRootHash(merKleTreeNodes);

        //获取上一个块的hash，构造下一个块
        List<Blockchain> blockchains = blockchainMapper.selectLastOne(type);
        Blockchain prevBlock = blockchains.get(0);
        HashMap<String, String> map = new HashMap<>();
        map.put("Infos",infos.toString());
        Block block = new Block(prevBlock.getBlockHash(), map, new Date().getTime(), merKleRoot.getData());
        //构造block
        Blockchain currentBlock = new Blockchain(block,type);
        blockchainMapper.insertSelective(currentBlock);
        return res;
    }


    public List<MerKleTreeNode> buildInitMerKleHashNode(List<Info> list){
        ArrayList<MerKleTreeNode> res = new ArrayList<>();
        for(Info info : list){
            MerKleTreeNode node = new MerKleTreeNode(null,null, BlockHashAlgoUtils.encodeDataBySHA_256(info.toString()),1,info.getInfoId());
            res.add(node);
            System.out.println(info);
        }
        return res;
    }

    /**
     * 接收分片后的数据，然后生成一棵树
     */
    @Transactional(rollbackFor = Exception.class)
    public MerKleTreeNode generateMerKleRootHash(List<MerKleTreeNode> data){
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
         * 从满二叉树左边第一个叶子节点数起
         * 利用满二叉树特性
         */
        int height = Integer.toBinaryString(size).length();
        int total = (int)(Math.pow(2,height)-1);

        ArrayList<MerkleNode> merkleNodes = new ArrayList<>();
        int idxStart = (total +1) >> 1;
        /**
         * 叶子节点加入集合
         */
        for(MerKleTreeNode node : data){
            MerkleNode leafNode = new MerkleNode(node);
            leafNode.setMerkleNodeIndex(idxStart++);
            //加入集合
            merkleNodes.add(leafNode);
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
            //每一层的最左节点序号
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

                //通过idx方式插入数据库
                MerkleNode merkleEntity = new MerkleNode(node);
                merkleEntity.setMerkleNodeIndex(idxStart);
                //加入集合
                merkleNodes.add(merkleEntity);
                //idx 加1
                idxStart++;
            }
            /**
             * temp 下个循环进行新建回收，赋值data
             * size长度每次减半
             */
            data = temp;
            height--;
            size = size >> 1;
        }
        /**
         * 批量插入,有问题全部回滚,比逐条插入好
         */
        if(!CollectionUtils.isEmpty(merkleNodes)){
            merkleNodeMapper.insertBatchMerkleNode(merkleNodes);
        }
        return data.get(0);
    }


}
