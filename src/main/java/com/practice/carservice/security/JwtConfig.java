package com.practice.carservice.security;

import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Date;

@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer accessTokenExpMinutes;
    private Integer refreshTokenExpMinutes;

    public String getSecretKey() {
        return secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Date getAccessTokenExpDate() {
        return new Date(System.currentTimeMillis() + accessTokenExpMinutes * 60 * 1000);
    }

    public Date getRefreshTokenExpDate() {
        return new Date(System.currentTimeMillis() + refreshTokenExpMinutes * 60 * 1000);
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public void setAccessTokenExpMinutes(Integer accessTokenExpMinutes) {
        this.accessTokenExpMinutes = accessTokenExpMinutes;
    }

    public void setRefreshTokenExpMinutes(Integer refreshTokenExpMinutes) {
        this.refreshTokenExpMinutes = refreshTokenExpMinutes;
    }
}
