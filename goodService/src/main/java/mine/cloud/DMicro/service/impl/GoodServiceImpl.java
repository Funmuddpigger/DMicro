package mine.cloud.DMicro.service.impl;

import mine.cloud.DMicro.dao.GoodMapper;
import mine.cloud.DMicro.dao.InfoMapper;
import mine.cloud.DMicro.pojo.Good;
import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.service.IBlockChainService;
import mine.cloud.DMicro.service.IGoodService;
import mine.cloud.DMicro.service.IMerkleService;
import mine.cloud.DMicro.service.InfoService;
import mine.cloud.DMicro.utils.HttpStatusCode;
import mine.cloud.DMicro.utils.ImageUploadUtils;
import mine.cloud.DMicro.utils.ResultList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class GoodServiceImpl implements IGoodService , InfoService {

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private IMerkleService iMerkleService;

    @Autowired
    private InfoMapper infoMapper;

    @Override
    public ResultList searchGoodBySelective(Good record) {
        ResultList res = new ResultList();
        List<Good> goods = goodMapper.selectBySelective(record);
        res.setCode(HttpStatusCode.HTTP_OK);
        res.setData(goods);
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList upLoadImageOrFile(MultipartFile file, HttpServletRequest request) {
        ImageUploadUtils imageUploadUtils = new ImageUploadUtils();
        ResultList res = imageUploadUtils.upLoadFileOrImages(file, request);
        return res;
    }

    @Override
    public ResultList deleteBatchByList(List<Integer> goodIdList) {
        ResultList res = new ResultList();
        goodMapper.deleteBatchByIds(goodIdList);
        res.setMsg("ok");
        return res;
    }

    @Override
    public ResultList addGoodBatchByList(List<Good> records) {
        return null;
    }


    @Override
    public ResultList queryInfos(Info record) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultList addDetailsInfos(List<Info> records,String type) {
        for(Info info : records){
            info.setInfoTime(new Date());
        }
        infoMapper.insertBatchBySelective(records);
        ResultList res = iMerkleService.createBlockLinkWithData(records, type);
        return res;
    }

}
