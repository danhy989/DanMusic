package com.danhy989.DanMusi.service.impl;

import com.danhy989.DanMusi.dto.TokenDTO;
import com.danhy989.DanMusi.enums.GrantTypeEnum;
import com.danhy989.DanMusi.service.RestTemplateService;
import com.danhy989.DanMusi.service.SpotifyClientService;
import com.danhy989.DanMusi.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

@Service
public class SpotifyClientServiceImpl implements SpotifyClientService {
    private static final Logger log = LoggerFactory.getLogger(SpotifyClientServiceImpl.class);

    @Value("${spotify.client.url.token}")
    private String spotifyTokenUrl;

    @Value("${spotify.client.info.client_id}")
    private String spotifyClientId;

    @Value("${spotify.client.info.client_secret}")
    private String spotifyClientSecret;

    private RestTemplateService restTemplateService;

    public SpotifyClientServiceImpl(RestTemplateService restTemplateService ) {
        this.restTemplateService = restTemplateService;
    }

    @Override
    public TokenDTO requestNewAccessToken(TokenDTO tokenDTO) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(StringUtils.getAuthorizationCode(spotifyClientId,spotifyClientSecret));
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", GrantTypeEnum.REFRESH_TOKEN.getType());
        map.add("refresh_token",tokenDTO.getRefresh_token());
        JsonNode tokenJsonRes = restTemplateService.postForEntity(map,httpHeaders,spotifyTokenUrl);
        tokenDTO.setAccess_token(tokenJsonRes.get("access_token").asText());
        tokenDTO.setExpires_in(tokenJsonRes.get("expires_in").asInt());
        return tokenDTO;
    }
}
