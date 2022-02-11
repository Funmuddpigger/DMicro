package mine.cloud.DMicro.utils;

public class StringHelperUtils {

    public static boolean isNotEmpty(String str){
        if(str == null || "".equals(str.trim())){
            return false;
        }else{
            return true;
        }
    }
}
