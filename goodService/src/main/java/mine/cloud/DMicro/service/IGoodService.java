package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.Good;
import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IGoodService {
    //select good info
    ResultList searchGoodBySelective(Good record);

    //upload
    ResultList upLoadImageOrFile(MultipartFile file, HttpServletRequest request);

    //delete
    ResultList deleteBatchByList(List<Integer> goodIdList);

    //add
    ResultList addGoodBatchByList(List<Good> records,String token);

    //add
    ResultList addGood(Good records,String token);
}
