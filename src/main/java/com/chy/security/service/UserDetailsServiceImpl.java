package com.chy.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chy.security.entity.UserEntity;
import com.chy.security.mapper.UserMapper;
import com.chy.security.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chy
 * @since 2023-04-12 22:57
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;


    /**
     * 自定义 UserDetailsService的话，没有传入用户密码
     * 如果需要密码需要一个工具类，在登录时候就存入用户信息
     *
     * 调用 AbstractUserDetailsAuthenticationProvider.additionalAuthenticationChecks()->additionalAuthenticationChecks() 检查密码
     * additionalAuthenticationChecks()是一个抽象方法 由具体是子类执行密码匹配  DaoAuthenticationProvider.additionalAuthenticationChecks()
     * 匹配比对是会使用 我们配置的：BCryptPasswordEncoder
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
        if (Objects.isNull(userEntity)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // DaoAuthenticationProvider#additionalAuthenticationChecks() 会校验密码(会使用注入的BCryptPasswordEncoder）
        return createLoginUser(userEntity);
    }

    public UserDetails createLoginUser(UserEntity user) {

        LoginUser loginUser = new LoginUser().setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setPermissions(List.of("delete"))
                .setAuthorities(Stream.of("add", "update").map(SimpleGrantedAuthority::new).toList());

        // 先将该用户所拥有的资源id全部查询出来，再转换成`SimpleGrantedAuthority`权限对象

        return loginUser;
    }
}
