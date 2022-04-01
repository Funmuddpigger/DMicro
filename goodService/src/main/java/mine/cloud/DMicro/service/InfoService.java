package mine.cloud.DMicro.service;

import mine.cloud.DMicro.pojo.Info;
import mine.cloud.DMicro.utils.ResultList;

import java.util.List;

public interface InfoService {
    ResultList queryInfos(Info record);

    ResultList addDetailsInfos(List<Info> records,String type);
}
