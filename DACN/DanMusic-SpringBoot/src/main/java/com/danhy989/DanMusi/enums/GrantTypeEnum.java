package com.danhy989.DanMusi.enums;

public enum GrantTypeEnum {
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    private String type;

    GrantTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
