package mine.cloud.DMicro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class testUsr {

    @Test
    void TestBCryptPassworEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456789");
        String encode2 = bCryptPasswordEncoder.encode("123456789");
        String encode3 = bCryptPasswordEncoder.encode("123456789");
        System.out.println(encode);
        System.out.println(encode2);
        System.out.println(encode3);
    }
}
