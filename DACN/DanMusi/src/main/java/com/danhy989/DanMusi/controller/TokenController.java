package com.danhy989.DanMusi.controller;

import com.danhy989.DanMusi.cronjob.TokenTasks;
import com.danhy989.DanMusi.enums.GrantTypeEnum;
import com.danhy989.DanMusi.service.RestTemplateService;
import com.danhy989.DanMusi.statics.TokenStatic;
import com.danhy989.DanMusi.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping(value = "/token")
public class TokenController {

    @Value("${spotify.authorize.url}")
    private String spotifyAuthorizeUrl;

    @Value("${spotify.token.url}")
    private String spotifyTokenUrl;

    @Value("${spotify.redirect_url}")
    private String spotifyRedirectUrl;

    @Value("${spotify.client_id}")
    private String spotifyClientId;

    @Value("${spotify.client_secret}")
    private String spotifyClientSecret;

    private RestTemplateService restTemplateService;

    public TokenController(RestTemplateService restTemplateService ) {
        this.restTemplateService = restTemplateService;
    }

    @GetMapping("/callback")
    @ResponseBody
    public void getCallBack(HttpServletRequest request,
                            @CookieValue(value = "spotify_auth_state") String spotifyAuthorCookie) throws IOException {
        String code = request.getQueryString().split("&")[0].split("=")[1];
        String state = request.getQueryString().split("&")[1].split("=")[1];
        if(state == null || !state.equals(spotifyAuthorCookie)){
            System.out.println("state_mismatch");
        }else{
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            httpHeaders.setBasicAuth(StringUtils.getAuthorizationCode(spotifyClientId,spotifyClientSecret));
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("redirect_uri",spotifyRedirectUrl);
            map.add("grant_type", GrantTypeEnum.AUTHORIZATION_CODE.getType());
            JsonNode tokenJsonRes  = restTemplateService.postForEntity(map,httpHeaders,spotifyTokenUrl);
            TokenStatic.getInstance().setInstance(
                    tokenJsonRes.get("access_token").asText(),
                    tokenJsonRes.get("refresh_token").asText(),
                    tokenJsonRes.get("scope").asText(),
                    tokenJsonRes.get("expires_in").asInt());
        }
    }

    @GetMapping("")
    @ResponseBody
    public String getAccessToken(){
        if(TokenStatic.getInstance().getAccessToken()!= null && TokenStatic.getInstance().getAccessToken() != "") {
            return TokenStatic.getInstance().getAccessToken();
        }else{
            System.out.println("Authorization failed");
            return "Authorization failed";
        }
    }
}
