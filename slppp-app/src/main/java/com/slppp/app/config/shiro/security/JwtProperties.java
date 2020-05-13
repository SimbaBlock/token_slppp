package com.slppp.app.config.shiro.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {

    /**
     * token过期时间，单位分钟
     */
    @Value("${token.tokenExpireTime}")
    Integer tokenExpireTime;

    /**
     * 更新令牌时间，单位分钟
     */
    @Value("${token.refreshCheckTime}")
    Integer refreshCheckTime;

    /**
     * Shiro缓存有效期，单位分钟
     */
    @Value("${token.shiroCacheExpireTime}")
    Integer shiroCacheExpireTime;

    /**
     * token加密密钥
     */
    @Value("${token.secretKey}")
    String secretKey;
}
