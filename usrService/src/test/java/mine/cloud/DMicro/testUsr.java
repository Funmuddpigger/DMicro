package mine.cloud.DMicro;

import mine.cloud.DMicro.dao.UserMapper;
import mine.cloud.DMicro.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class testUsr {

    @Autowired
    private UserMapper userMapper;

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

    @Test
    void TestBatchUserSelect(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<User> users = userMapper.selectBatchByIds(list);
        for(User usr : users){
            System.out.println(usr.getUsrNickname());
        }
    }
}
