package com.example.smartstudy.utils;

import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.dto.Security.JwtClaims;
import com.example.smartstudy.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 令牌工具类，用于生成及解析令牌
 */
@Component
public class JwtUtils {

    @Autowired
    private JwtProperties jwtProperties;

    public String createUserIdJwt(String userId, String loginType, List<String> authorities){
        Map<String, Object> claims=new HashMap<>();

        // 设置User对象的属性到claims中
        claims.put("userId", userId);
        claims.put("loginType", loginType);
        claims.put("authorities", authorities);

        return createJwt(jwtProperties.getOrSecretKey(), jwtProperties.getOrTtl(), claims);
    }

    public JwtClaims getUserIdJwt(String token){
        Claims claims = praseJwt(jwtProperties.getOrSecretKey(), token);
        String userId=(String) claims.get("userId");
        String loginType= (String) claims.get("loginType");
        List<String> authorities= (List<String>) claims.get("authorities");
        return new JwtClaims(userId,loginType,authorities);
    }
    /**
     * @param secretKey 密钥
     * @param ttlMillis 过期时间
     * @param claims    具体信息
     * @return
     */
    //创建jwt令牌
    public static String createJwt(String secretKey, long ttlMillis, Map<String, Object> claims) {
        //指定256签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成jwt令牌的时间
        long ttlTime = System.currentTimeMillis() + ttlMillis;
        Date time = new Date(ttlTime);

        //设置jwt令牌
        JwtBuilder jwtBuilder = Jwts.builder()
        //设置声明
                .setClaims(claims)
        //设置签名算法和签名算法的密钥
                .signWith(signatureAlgorithm, secretKey)
        //设置过期时间
                .setExpiration(time);
        return jwtBuilder.compact();
    }


    /**
     * @param token 加密后的token
     * @return
     */
    //解密jwt
    public static Claims praseJwt(String secretKey, String token) {
        Claims claims = null;
        try {
            // 处理成功解析的JWT
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
        // 处理签名验证失败的异常
            throw new BaseException("令牌验证失败");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
        // 处理JWT过期的异常
            throw new BaseException("令牌时间过期");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
        // 处理JWT格式错误的异常
            throw new BaseException("令牌格式错误");
        } catch (io.jsonwebtoken.UnsupportedJwtException | io.jsonwebtoken.security.SecurityException e) {
        // 处理其他JWT相关异常
            throw new BaseException("其他异常，请联系管理员");
        }
        return claims;
    }
}
