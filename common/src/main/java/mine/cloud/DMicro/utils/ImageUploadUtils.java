package mine.cloud.DMicro.utils;

import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ImageUploadUtils {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/");

    /**
     * 图片保存路径，自动从yml文件中获取数据
     *   示例： E:/images/
     */
//    private static final String filePath = "F:/images/" ;
    private static final String filePath = "/data/images/" ;

    public ResultList upLoadFileOrImages(MultipartFile file, HttpServletRequest request){
        ResultList res = new ResultList();
        String directory = simpleDateFormat.format(new Date());
        /**
         *  2.文件保存目录  F:/images/+directory
         *  如果目录不存在，则创建
         */
        File dir = new File(filePath + directory );
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println("图片上传，保存位置：" + filePath + directory );
        //3.给文件重新设置一个名字
        //后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replaceAll("-", "")+suffix;
        //4.创建这个新文件
        File newFile = new File(filePath + directory + newFileName);
        //设置具体服务器地址(外网地址,否则取到的是内网)
        String trueServerIp = "8.130.16.197";
        //5.复制操作
        try {
            file.transferTo(newFile);
            //协议 :// ip地址 ：端口号 / 文件目录(/images/xxx/xx/xx/xxx.jpg)
            //String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/images/" + directory + newFileName;
            String url = request.getScheme() + "://" + trueServerIp + ":" + request.getServerPort() + "/images/" + directory + newFileName;
            System.out.println("图片上传，访问URL：" + url);
            res.setOneData(url);
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
