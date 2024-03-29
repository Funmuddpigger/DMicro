package mine.cloud.DMicro.controller;


import mine.cloud.DMicro.pojo.Blockchain;
import mine.cloud.DMicro.service.IBlockChainService;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/blockchain")
public class BlockChainController {

    @Autowired
    private IBlockChainService blockChainService;

    @RequestMapping(value="/first",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList addCreateBlock(@RequestBody(required = false) Blockchain block){
        return blockChainService.createFirstBlock(block);
    }

    //返回验证路径
    @RequestMapping(value="/proof",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList queryProofBlock(@RequestParam("infoId") Integer infoId){
        ResultList res = new ResultList();
        res.setData(blockChainService.getCheckProof(infoId));
        return res;
    }

    //check
    @RequestMapping(value="/spvcheck",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList spvCheckMsgData(@RequestParam("infoId") Integer infoId){
        return blockChainService.spvCheckMsgData(infoId);
    }


}
