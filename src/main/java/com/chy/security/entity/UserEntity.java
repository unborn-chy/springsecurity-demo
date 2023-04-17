package com.chy.security.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author chy
 * @since 2023-04-12 22:28
 */

@Data
@TableName("user")
public class UserEntity {
    @TableId
    private Integer id;
    private String username;
    private Integer age;
    private String password;
}
