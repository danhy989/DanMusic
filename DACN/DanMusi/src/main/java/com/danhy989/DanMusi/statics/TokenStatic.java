package com.danhy989.DanMusi.statics;

import com.danhy989.DanMusi.utils.TimeUtil;

import java.util.Date;

public class TokenStatic {
    private static final TokenStatic instance = new TokenStatic();
    private TokenStatic(){}
    public static TokenStatic getInstance(){
        return instance;
    }

    private String accessToken;

    private String refreshToken;

    private String scope;

    private Date expiresTime;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setInstance(String accessToken, String refreshToken, String scope, int expireIn){
        instance.accessToken = accessToken;
        instance.refreshToken = refreshToken;
        instance.scope = scope;
        instance.expiresTime = TimeUtil.addSecondToCurrentDate(expireIn);
    }

    public void setAccessTokenAndExpireTime(String accessToken,int expireIn){
        instance.accessToken = accessToken;
        instance.expiresTime = TimeUtil.addSecondToCurrentDate(expireIn);
    }
}
