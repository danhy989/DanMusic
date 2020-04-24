package com.danhy989.DanMusi.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class JSONUtil {
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
}
