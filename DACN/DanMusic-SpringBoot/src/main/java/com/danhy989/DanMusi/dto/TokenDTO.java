package com.danhy989.DanMusi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TokenDTO {
    private String access_token;
    private String refresh_token;
    private String scope;
    private Integer expires_in;

    public TokenDTO() {
    }

    public TokenDTO(String access_token, String refresh_token, String scope, Integer expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.scope = scope;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
