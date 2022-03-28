package mine.cloud.DMicro.blockChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.util.Map;

/**
 * SHA（Secure Hash Algorithm）安全散列算法
 * SHA-256 作为区块链加密
 * 少量更改会在Hash值中产生不可预知的大量更改，hash值用作表示大量数据的固定大小的唯一值
 * 长度256位
 */

public class BlockHashAlgoUtils {
    public static String encodeDataBySHA_256(Map<String,String> data){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input
            ObjectMapper mapper = new ObjectMapper();
            byte[] hash = digest.digest(mapper.writeValueAsBytes(data));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
