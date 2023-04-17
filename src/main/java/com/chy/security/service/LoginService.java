package com.chy.security.service;

import com.chy.security.model.LoginUser;
import com.chy.security.util.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author chy
 * @since 2023-04-10 23:31
 */
@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(LoginUser loginUser) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());
        try {
            /**
             * ProviderManager.authenticate()-> provider.authenticate()
             * AbstractUserDetailsAuthenticationProvider.authenticate()->retrieveUser()
             * DaoAuthenticationProvider.retrieveUser()->this.getUserDetailsService().loadUserByUsername(username)
             * 最后会调用UserDetailsServiceImpl.loadUserByUsername
             */
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
//            AuthenticationContextHolder.setContext(authenticationToken);
            authenticationManager.authenticate(authenticationToken);
            return JwtUtils.sign(loginUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return "登录失败";
        } finally {
//            AuthenticationContextHolder.clearContext();
        }

    }
}