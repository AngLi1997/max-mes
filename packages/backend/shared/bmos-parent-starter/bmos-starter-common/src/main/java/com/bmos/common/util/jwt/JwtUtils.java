package com.bmos.common.util.jwt;

import cn.hutool.core.convert.Convert;
import com.bmos.common.constant.RequestConstant;
import com.bmos.common.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Map;

/**
 * Jwt工具类
 */
public class JwtUtils
{
    public static String secret = RequestConstant.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder().claims(claims).signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token)
    {
        return (Claims) Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token).getPayload();
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    /**
     * 根据令牌获取登录标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getLoginToken(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstant.LOGIN_TOKEN);
    }

    /**
     * 根据令牌获取登录标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getLoginToken(Claims claims)
    {
        return getValue(claims, SecurityConstant.LOGIN_TOKEN);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstant.USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims)
    {
        return getValue(claims, SecurityConstant.USER_ID);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key 键
     * @return 值
     */
    public static String getValue(Claims claims, String key)
    {
        return Convert.toStr(claims.get(key), "");
    }
}
