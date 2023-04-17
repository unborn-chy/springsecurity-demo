package com.chy.security.controller;

import com.chy.security.model.LoginUser;
import com.chy.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author chy
 * @since 2023-04-10 20:14
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    @GetMapping("info")
    public Authentication info(Authentication authentication) {
        return authentication;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUser loginUser) {
        return loginService.login(loginUser);
    }


    @GetMapping("/register/{id}")
    public String register(@PathVariable("id") String id) {
        return "register:" + id;
    }


//    @GetMapping("/hello")
//    public DefaultOAuth2User hello(){
//        System.out.println("hello ");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (DefaultOAuth2User) authentication.getPrincipal();
//    }
}
