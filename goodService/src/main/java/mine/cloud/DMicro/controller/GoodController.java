package mine.cloud.DMicro.controller;

import mine.cloud.DMicro.pojo.Good;
import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.service.IGoodService;
import mine.cloud.DMicro.service.InfoService;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/good")
public class GoodController {

    @Autowired
    private IGoodService iGoodService;

    @Autowired
    private InfoService infoService;

    @RequestMapping(value="/select",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList searchGoodBySelective(@RequestBody Good record){
        return iGoodService.searchGoodBySelective(record);
    }

    //good模块上传回显url(image)
    @RequestMapping(value="/upload",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList uploadImg(@RequestParam("image") MultipartFile file, HttpServletRequest request)  {
        return iGoodService.upLoadImageOrFile(file, request);//这里调用service的upfile方法，传入两个参数。
    }

    @RequestMapping(value = "delete",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList deleteGoodInfo(@RequestBody List<Integer> goodIdList){
        return iGoodService.deleteBatchByList(goodIdList);
    }

    @RequestMapping(value="/searchInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList queryGoodInfo(@RequestBody Info record){
        return infoService.queryInfos(record);
    }

    @RequestMapping(value="/add-info",method = {RequestMethod.GET,RequestMethod.POST})
    public ResultList addDetailsInfos(@RequestBody List<Info> records,@RequestParam("type") String type){
        return infoService.addDetailsInfos(records,type);
    }

}
