package com.chy.security.service;

import com.chy.security.context.SecurityUtils;
import com.chy.security.model.LoginUser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author chy
 * @since 2023-04-16 21:55
 */
@Service("ss")
public class PermissionService
{
    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission)
    {
        System.out.println("permission:" + permission);
        if (!StringUtils.hasText(permission))
        {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (Objects.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        return loginUser.getPermissions().contains(permission);
    }

}
