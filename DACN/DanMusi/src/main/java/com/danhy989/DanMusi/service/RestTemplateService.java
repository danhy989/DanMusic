package com.danhy989.DanMusi.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

public interface RestTemplateService {
    JsonNode postForEntity(MultiValueMap map, HttpHeaders httpHeaders, String url) throws IOException;
}
