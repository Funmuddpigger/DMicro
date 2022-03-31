package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.dao.GoodMapper;
import mine.cloud.DMicro.pojo.Good;
import mine.cloud.DMicro.service.IGoodService;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GoodServiceImpl implements IGoodService {

    @Autowired
    private GoodMapper goodMapper;

    @Override
    public ResultList queryGoodInfo(Good record) {
        ResultList res = new ResultList();
        List<Good> goods = goodMapper.selectByPrimaryKeySelective(record);
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setData(goods);
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList upLoadImageOrFile(MultipartFile file, HttpServletRequest request) {
        return null;
    }
}
