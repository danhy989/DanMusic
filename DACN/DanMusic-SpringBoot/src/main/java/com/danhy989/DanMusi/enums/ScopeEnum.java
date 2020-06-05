package com.danhy989.DanMusi.enums;

public enum ScopeEnum {
    USER_READ_PRIVATE("user-read-private"),
    USER_READ_EMAIL("user-read-email"),
    STREAMING("streaming"),
    UGC_IMAGE_UPLOAD("ugc-image-upload"),
    USER_READ_PLAYBACK_STATE("user-read-playback-state"),
    USER_MODIFY_PLAYBACK_STATE(" user-modify-playback-state"),
    USER_READ_CURRENTLY_PLAYING("user-read-currently-playing"),
    APP_REMOTE_CONTROL("app-remote-control"),
    PLAYLIST_READ_COLLABORATIVE("playlist-read-collaborative"),
    PLAYLIST_MODIFY_PUBLIC(" playlist-modify-public"),
    PLAYLIST_READ_PRIVATE("playlist-read-private"),
    PLAYLIST_MODIFY_PRIVATE(" playlist-modify-private"),
    USER_LIBRARY_MODIFY("user-library-modify"),
    USER_LIBRARY_READ("user-library-read"),
    USER_TOP_READ("user-top-read"),
    USER_READ_PLAYBACK_POSITION("user-read-playback-position"),
    USER_READ_RECENTLY_PLAYED("user-read-recently-played"),
    USER_FOLLOW_READ("user-follow-read"),
    USER_FOLLOW_MODIFY("user-follow-modify");

    private String type;

    ScopeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public static String getScope(String... scopes){
        String outcome = "";
        for(int i=0;i<scopes.length;i++){
            if(i!=scopes.length-1){
                outcome=outcome+scopes[i]+"%20";
            }else{
                outcome=outcome+scopes[i];
            }
        }
        return outcome;
    }
}
