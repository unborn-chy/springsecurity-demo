package com.chy.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author chy
 * @since 2023-04-11 21:56
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginUser implements UserDetails {
    private String username;

    /**
     * DaoAuthenticationProvider.additionalAuthenticationChecks 会校验密码
     * 然后会调用UserDetails.getPassword()方法，如果密码字段不是叫password字段，则需要重写改方法，返回密码字段
     */
    private String password;

    /**
     * 用户权限信息，主要在注解 @PreAuthorize("@ss.hasPermi('delete')")调用我们直接的实现类ss
     */
    private List<String> permissions;


    private List<SimpleGrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
