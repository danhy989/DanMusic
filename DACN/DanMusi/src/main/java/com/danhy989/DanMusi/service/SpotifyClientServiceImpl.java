package com.danhy989.DanMusi.service;

import com.danhy989.DanMusi.utils.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
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
public class SpotifyClientServiceImpl implements SpotifyClientService{
    private final String CLIENT_ID = "a0a76faa34084bcbb1c0180760beda39";
    private final String CLIENT_SECRET = "c5e48bde19404544a38d60f10fdab743";
    private final String URL = "https://accounts.spotify.com/api/token";

    private final RestTemplate restTemplate;

    public SpotifyClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public String getTokenFromSpotify() throws IOException {
        String clientSercetID64 = CLIENT_ID+":"+CLIENT_SECRET;
        String authorization = Base64.getEncoder().encodeToString(clientSercetID64.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(authorization);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(
                URL, request , String.class);
        JsonNode tokenJsonRes = JSONUtil.convertResponseEntityBodyToJson(response);
        return tokenJsonRes.path("access_token").textValue();
    }
}
