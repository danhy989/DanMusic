package com.danhy989.DanMusi.controller;


import com.danhy989.DanMusi.dto.TokenDTO;
import com.danhy989.DanMusi.enums.GrantTypeEnum;
import com.danhy989.DanMusi.service.RestTemplateService;
import com.danhy989.DanMusi.service.SpotifyClientService;
import com.danhy989.DanMusi.utils.JSONUtil;
import com.danhy989.DanMusi.utils.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
@RequestMapping(value = "/token")
public class TokenController {

    @Value("${spotify.client.url.authorize}")
    private String spotifyAuthorizeUrl;

    @Value("${spotify.client.url.token}")
    private String spotifyTokenUrl;

    @Value("${spotify.client.url.redirect}")
    private String spotifyRedirectUrl;

    @Value("${spotify.client.info.client_id}")
    private String spotifyClientId;

    @Value("${spotify.client.info.client_secret}")
    private String spotifyClientSecret;

    private RestTemplateService restTemplateService;

    private SpotifyClientService spotifyClientService;

    public TokenController(RestTemplateService restTemplateService ,SpotifyClientService spotifyClientService) {
        this.restTemplateService = restTemplateService;
        this.spotifyClientService = spotifyClientService;
    }

    @GetMapping("/callback")
    public RedirectView getCallBack(HttpServletRequest request,
                              @CookieValue(value = "spotify_auth_state") String spotifyAuthorCookie, Model model, RedirectAttributes redir) throws IOException {
        TokenDTO tokenDTO = null;

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

            tokenDTO = new TokenDTO(tokenJsonRes.get("access_token").asText(),
                    tokenJsonRes.get("refresh_token").asText(),
                    tokenJsonRes.get("scope").asText(),
                    tokenJsonRes.get("expires_in").asInt());
            redir.addFlashAttribute("token",tokenDTO);
        }
        final RedirectView redirectView= new RedirectView("/index",true);
        return redirectView;
    }

    @PutMapping("/refresh")
    @ResponseBody
    public TokenDTO requestToAccessTokenByRefreshToken(@RequestBody String tokenObj){
        TokenDTO tokenDTO = null;
        TokenDTO newTokenDTO = null;
        try {
            tokenDTO = JSONUtil.convertJsonToTokenDTO(tokenObj);
            newTokenDTO = this.spotifyClientService.requestNewAccessToken(tokenDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newTokenDTO;
    }
}
