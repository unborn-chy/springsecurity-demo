package com.chy.security.filter;

import com.chy.security.model.LoginUser;
import com.chy.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author chy
 * @since 2023-04-11 21:54
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("authorization");
        if (StringUtils.hasText(token)) {
            String username = JwtUtils.getUsernameFromToken(token);
            if (!ObjectUtils.isEmpty(username)) {
                LoginUser loginUser = new LoginUser().setUsername(username)
                        .setPermissions(List.of("delete"))
                        .setAuthorities(Stream.of("add", "update").map(SimpleGrantedAuthority::new).toList());

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                //WebAuthenticationDetails对象包含有关当前请求的详细信息，例如远程IP地址、会话ID等。这个方法的作用是将这些详细信息添加到Authentication对象中，以便在需要时进行访问。
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
