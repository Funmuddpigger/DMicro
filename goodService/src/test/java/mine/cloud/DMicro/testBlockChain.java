package mine.cloud.DMicro;

import mine.cloud.DMicro.blockChain.MerKleTreeUtils;
import mine.cloud.DMicro.pojo.MerKleNode;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class testBlockChain {

    public static void main(String[] args) {
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map0 = new HashMap<>();
        map0.put("CPU","I9-9900K");
        map0.put("GPU","RTX2060");
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("CPU","I9-9901K");
        map1.put("GPU","RTX2061");
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("CPU","I9-9902K");
        map2.put("GPU","RTX2062");
//        HashMap<String, String> map3 = new HashMap<>();
//        map3.put("CPU","I9-9903K");
//        map3.put("GPU","RTX2063");
//        HashMap<String, String> map4 = new HashMap<>();
//        map4.put("CPU","I9-9904K");
//        map4.put("GPU","RTX2064");
//        HashMap<String, String> map5 = new HashMap<>();
//        map5.put("CPU","I9-9905K");
//        map5.put("GPU","RTX2065");
//        HashMap<String, String> map6 = new HashMap<>();
//        map6.put("CPU","I9-9906K");
//        map6.put("GPU","RTX2066");
//        HashMap<String, String> map7 = new HashMap<>();
//        map7.put("CPU","I9-9907K");
//        map7.put("GPU","RTX2067");
//        HashMap<String, String> map8 = new HashMap<>();
//        map8.put("CPU","I9-9908K");
//        map8.put("GPU","RTX2068");
//        HashMap<String, String> map9 = new HashMap<>();
//        map9.put("CPU","I9-9909K");
//        map9.put("GPU","RTX2069");

        list.add(map0);
        list.add(map1);
        list.add(map2);
//        list.add(map3);
//        list.add(map4);
//        list.add(map5);
//        list.add(map6);
//        list.add(map7);
//        list.add(map8);
//        list.add(map9);
        List<MerKleNode> merKleNodes = MerKleTreeUtils.buildInitMerKleHashNode(list);
        System.out.println(MerKleTreeUtils.generateMerKleRootHash(merKleNodes).getData());
    }
}
