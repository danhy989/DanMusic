package com.danhy989.DanMusi.controller;

import com.danhy989.DanMusi.service.SpotifyClientService;
import com.danhy989.DanMusi.utils.APIResponse;
import com.danhy989.DanMusi.utils.Constant;
import com.danhy989.DanMusi.utils.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping(value = "demo/spotify")
public class SpotifyController {

    private final RestTemplate restTemplate;

    private final SpotifyClientService spotifyClientService;

    public SpotifyController(SpotifyClientService spotifyClientService, RestTemplate restTemplate) {
        this.spotifyClientService = spotifyClientService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/users/{name}")
    public ResponseEntity<APIResponse> getUserInfo(@PathVariable(name = "name") String name){
        JsonNode json = null;
        long start = System.currentTimeMillis();
        long took = 0;
        try{
            //Get token
            String token = this.spotifyClientService.getTokenFromSpotify();

            //Call API
            String url = "https://api.spotify.com/v1/users/"+name;

            //Http header
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            json = JSONUtil.convertResponseEntityBodyToJson(response);

            took = System.currentTimeMillis()-start;
        }catch (HttpClientErrorException e){

        }catch (IOException e){

        }
        return new ResponseEntity<>(
                new APIResponse(
                        Constant.SUCCESS_STATUS, Constant.SUCCESS, null, took, json),
                HttpStatus.OK);
    }


}
