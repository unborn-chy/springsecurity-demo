package com.chy.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chy
 * @since 2023-04-10 22:04
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(200);

        String format = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", format);
        result.put("status", 200);
        String s = new ObjectMapper().writeValueAsString(result);


        response.getWriter().print(s);
        System.out.println(s);

    }
}
