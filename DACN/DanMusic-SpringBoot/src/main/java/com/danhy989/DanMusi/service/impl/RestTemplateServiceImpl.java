package com.danhy989.DanMusi.service.impl;

import com.danhy989.DanMusi.service.RestTemplateService;
import com.danhy989.DanMusi.utils.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class RestTemplateServiceImpl implements RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateServiceImpl( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public JsonNode postForEntity(MultiValueMap map, HttpHeaders httpHeaders,String url) throws IOException {
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(
                url, httpEntity , String.class);
        return JSONUtil.convertResponseEntityBodyToJson(response);
    }
}
