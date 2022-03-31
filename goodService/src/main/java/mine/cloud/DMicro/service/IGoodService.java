package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.Good;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface IGoodService {
    ResultList queryGoodInfo(Good record);

    ResultList upLoadImageOrFile(MultipartFile file, HttpServletRequest request);
}
