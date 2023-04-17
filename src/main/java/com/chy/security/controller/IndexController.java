package com.chy.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chy
 * @since 2023-04-10 20:14
 */
@RestController
public class IndexController {


    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }


}
