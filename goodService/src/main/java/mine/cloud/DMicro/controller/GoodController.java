package mine.cloud.DMicro.controller;

import mine.cloud.DMicro.pojo.Good;
import mine.cloud.DMicro.service.IGoodService;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/good")
public class GoodController {

    @Autowired
    private IGoodService iGoodService;

    public ResultList searchGoodInfo(@RequestBody Good record){
        return iGoodService.queryGoodInfo(record);
    }

    //good模块上传回显url(image)
    @RequestMapping("/upload")
    public ResultList uploadImg(@RequestParam("image") MultipartFile file, HttpServletRequest request)  {
        return iGoodService.upLoadImageOrFile(file, request);//这里调用service的upfile方法，传入两个参数。
    }
}
