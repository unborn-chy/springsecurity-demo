package com.chy.security.config;

import com.chy.security.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author chy
 * @since 2023-04-10 20:31
 */
@EnableWebSecurity
@Configuration
@EnableMethodSecurity //开启权限注解
public class SecurityConfig {

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF禁用，因为不使用session
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用HTTP响应标头(缓存)Cache-Control
                .headers(HeadersConfigurer::disable)
                // 认证失败处理类(访问一个需要登录的页面 就会被他拦截)
                .exceptionHandling(a -> a.authenticationEntryPoint(authenticationEntryPoint))
                // 基于token，所以不需要session
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> {
                    // 对于登录login 注册register 允许匿名访问
                    authorize.requestMatchers("/login", "/login/**", "/register", "/index").permitAll();
                    // 静态资源，可匿名访问
                    authorize.requestMatchers(HttpMethod.GET, "/", "/*.html", "/*/*.html", "/*/*.css", "/*/*.js", "/profile/**").permitAll();
                    authorize.requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll();
                    // 除上面外的所有请求全部需要鉴权认证
                    authorize.anyRequest().authenticated();
//                    try {
//                        authorize.and().oauth2Login();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
                });
        // 禁用X-Frame-Options响应头 允许通过frame或iframe加载当前页面
//        http.headers(h -> h.frameOptions().disable());

        // 添加Logout filter
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//    @Bean
//    UserDetailsService users() {
//        return new InMemoryUserDetailsManager(
//                User.withUsername("root")
//                        .password("{noop}root")
//                        .authorities("app")
//                        .build()
//        );
//    }


    /**
     * 如果容器中没有指定，则默认使用：DelegatingPasswordEncoder
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 定义一个全局的AuthenticationManager，可使用@Autowired进行使用
     * 如果在 authenticationManager中配置了 userDetailsService、passwordEncoder则会使用配置的，就不会使用我们在容器中自动配置的
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                //指定 userDetailsService， passwordEncoder。不配也会自动加载
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }

    /**
     * 没啥用，不太知道有啥用
     * 如果想使用springboot中的使用默认 @PreAuthorize("hasPermission('ccc','ddd')") 【hasPermission('ccc','ddd')不知道这个是啥意思】
     * 就需要自定义配置（大概）
     */

    @Autowired
    private MyPermissionEvaluator permissionEvaluator;
    @Bean
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}
