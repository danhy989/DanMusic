package com.danhy989.DanMusi.service.impl;

import com.danhy989.DanMusi.enums.GrantTypeEnum;
import com.danhy989.DanMusi.service.RestTemplateService;
import com.danhy989.DanMusi.service.SpotifyClientService;
import com.danhy989.DanMusi.statics.TokenStatic;
import com.danhy989.DanMusi.utils.JSONUtil;
import com.danhy989.DanMusi.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;

@Service
public class SpotifyClientServiceImpl implements SpotifyClientService {
    private static final Logger log = LoggerFactory.getLogger(SpotifyClientServiceImpl.class);

    @Value("${spotify.client_id}")
    private String spotifyClientId;

    @Value("${spotify.client_secret}")
    private String spotifyClientSecret;

    @Value("${spotify.token.url}")
    private String spotifyTokenUrl;

    private RestTemplateService restTemplateService;

    public SpotifyClientServiceImpl(RestTemplateService restTemplateService ) {
        this.restTemplateService = restTemplateService;
    }

    @Override
    public boolean requestNewAccessToken() throws IOException {
        boolean outcome = false;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(StringUtils.getAuthorizationCode(spotifyClientId,spotifyClientSecret));
        if(TokenStatic.getInstance().getRefreshToken()!= null && TokenStatic.getInstance().getRefreshToken() != ""){
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("grant_type", GrantTypeEnum.REFRESH_TOKEN.getType());
            map.add("refresh_token",TokenStatic.getInstance().getRefreshToken());
            JsonNode tokenJsonRes = restTemplateService.postForEntity(map,httpHeaders,spotifyTokenUrl);
            TokenStatic.getInstance().setAccessTokenAndExpireTime(
                    tokenJsonRes.get("access_token").asText(),
                    tokenJsonRes.get("expires_in").asInt());
            outcome = true;
        }else{
            log.error("Authorization failed");
        }
        return outcome;
    }
}
