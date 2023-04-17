package com.chy.security.handle;

/**
 * @author chy
 * @since 2023-04-10 22:00
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {


    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // 删除用户缓存记录
//            tokenService.delLoginUser(loginUser.getToken());
        // 记录用户退出日志

        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");


        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "退出成功");
        result.put("status", 200);
        String s = new ObjectMapper().writeValueAsString(result);


        response.getWriter().print(s);
        System.out.println(s);
    }
}

