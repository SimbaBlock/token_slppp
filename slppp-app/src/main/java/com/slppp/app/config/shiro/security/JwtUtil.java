package com.slppp.app.config.shiro.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.slppp.app.core.constant.SecurityConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.auth0.jwt.algorithms.Algorithm;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    private static JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        jwtUtil = this;
        jwtUtil.jwtProperties = this.jwtProperties;
    }

    /**
     * 校验token是否正确
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        String secret = getClaim(token, SecurityConsts.ACCOUNT) + jwtUtil.jwtProperties.secretKey;
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(secret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        verifier.verify(token);
        return true;
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     * @param token
     * @param claim
     * @return
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,n分钟后过期
     * @param account
     * @param currentTimeMillis
     * @return
     */
    public static String sign(String account, String currentTimeMillis){
        // 帐号加JWT私钥加密
        String secret = account + jwtUtil.jwtProperties.getSecretKey();
        // 此处过期时间，单位：毫秒
        Date date = new Date(System.currentTimeMillis() + jwtUtil.jwtProperties.getTokenExpireTime()*60*1000L);
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(secret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return JWT.create()
                .withClaim(SecurityConsts.ACCOUNT, account)
                .withClaim(SecurityConsts.CURRENT_TIME_MILLIS, currentTimeMillis)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
