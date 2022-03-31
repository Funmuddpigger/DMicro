package mine.cloud.DMicro.controller;

import mine.cloud.DMicro.service.IBlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blockchain")
public class BlockChainController {

    @Autowired
    private IBlockChainService blockChainService;



}
