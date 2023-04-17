package com.chy.security;

import com.chy.security.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurityDemoApplicationTests {

//    @Autowired
//    private UserMapper userMapper;

    @Test
    void contextLoads() {
//        List<UserMapper> userMappers = userMapper.selectList(null);
        System.out.println("userMappers");
    }

}
