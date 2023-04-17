package com.chy.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author chy
 * @since 2023-04-16 17:52
 */
public class JwtUtils {
    /**
     * 过期时间 15天
     */
    private static final long EXPIRE_TIME = 15 * 24 * 60 * 60 * 1000;

    /**
     * token 私钥
     */
    private static final String TOKEN_SECRET = "07d319d1860c463e8a18b723d35902fc";

    /**
     * 加密算法
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(TOKEN_SECRET);

    /**
     * 生成token15分钟后过期
     */
    public static String sign(String username) {
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        //生成token  默认header是有数据的 {type:JWT,alg:HS256}
        return JWT.create()
                .withClaim("username",username) //name存入进去
                .withExpiresAt(date)
                .sign(ALGORITHM);
    }

    /**
     * 校验token是否正确
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(ALGORITHM).build().verify(StringUtils.replace(token, "Bearer ", ""));
    }

    public static String getTokenClaimUsername(String token) {
        return JWT.decode(StringUtils.replace(token, "Bearer ", "")).getClaim("username").asString();
    }

    public static String getUsernameFromToken(String token) {
        try {
            verify(token);
           return getTokenClaimUsername(token);
        } catch (JWTDecodeException e) {
            e.printStackTrace();
           return null;
        }
    }
}