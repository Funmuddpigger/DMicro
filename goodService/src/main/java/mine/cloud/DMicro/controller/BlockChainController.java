package mine.cloud.DMicro.controller;

import mine.cloud.DMicro.pojo.Blockchain;
import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.service.IBlockChainService;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blockchain")
public class BlockChainController {

    @Autowired
    private IBlockChainService blockChainService;

    @RequestMapping(value="first",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList addCreateBlock(@RequestBody Blockchain block){
        return blockChainService.createFirstBlock(block);
    }

}
