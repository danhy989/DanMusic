package com.danhy989.DanMusi.utils;

import com.danhy989.DanMusi.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public class JSONUtil{
    static private ObjectMapper mapper = new ObjectMapper();

    public JSONUtil() {
        super();
    }

    static public <T> T deserialize(final String response, final Class<T> responseClass) throws IOException {
        if(response == null || responseClass == null) return null;

        return mapper.readValue(response, responseClass);
    }

    static public JsonNode convertResponseEntityBodyToJson(final ResponseEntity<String> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

    static public String convertMapToJson(final Map map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    static public TokenDTO convertJsonToTokenDTO(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,TokenDTO.class);
    }
}
