package com.danhy989.DanMusi.enums;

public enum ResponseTypeEnum {
    CODE("code"),
    TOKEN("token");

    private String type;

    ResponseTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
