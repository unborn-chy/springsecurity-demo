package com.chy.security.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chy
 * @since 2023-04-10 20:14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/list")
    @PermitAll
    public List<Object> list() {
        return List.of("张三", "李四", "王五");
    }

    @PostMapping("/add")
    @PreAuthorize("hasPermission('ccc','ddd')")
    public String add() {
        return "add";
    }

    @PostMapping("/update")
    @PreAuthorize("hasPermission('/update', 'update')")
    public String update() {
        return "update";
    }

    @PostMapping("/delete")
    @PreAuthorize("@ss.hasPermi('delete')")
    public String delete() {
        return "delete";
    }

    @PostMapping("/info")
    public String info() {
        return "info";
    }

}
