package com.chy.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chy.security.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author chy
 * @since 2023-04-12 22:31
 */

public interface UserMapper  extends BaseMapper<UserEntity> {
}
