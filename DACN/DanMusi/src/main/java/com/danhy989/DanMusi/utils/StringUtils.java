package com.danhy989.DanMusi.utils;

import java.util.Base64;

public class StringUtils {
    public static String getAuthorizationCode(String spotifyClientId,String spotifyClientSecret){
        String clientSercetID64 = spotifyClientId+":"+spotifyClientSecret;
        return Base64.getEncoder().encodeToString(clientSercetID64.getBytes());
    }
}
