package com.danhy989.DanMusi.controller;

import com.danhy989.DanMusi.enums.ResponseTypeEnum;
import com.danhy989.DanMusi.enums.ScopeEnum;
import com.danhy989.DanMusi.service.SpotifyClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SpotifyController {

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

    private final SpotifyClientService spotifyClientService;

    public SpotifyController(SpotifyClientService spotifyClientService) {
        this.spotifyClientService = spotifyClientService;
    }

    @GetMapping("/")
    public String test(Model model){
        model.addAttribute("spotifyAuthorizeUrl",spotifyAuthorizeUrl);
        model.addAttribute("spotifyRedirectUrl",spotifyRedirectUrl);
        model.addAttribute("spotifyClientId",spotifyClientId);
        model.addAttribute("spotifyClientSecret",spotifyClientSecret);
        model.addAttribute("spotifyScope", ScopeEnum.getScope(
                ScopeEnum.STREAMING.getType(),
                ScopeEnum.USER_READ_EMAIL.getType(),
                ScopeEnum.USER_READ_PRIVATE.getType(),
                ScopeEnum.USER_READ_CURRENTLY_PLAYING.getType(),
                ScopeEnum.USER_READ_PLAYBACK_STATE.getType(),
                ScopeEnum.USER_MODIFY_PLAYBACK_STATE.getType(),
                ScopeEnum.USER_FOLLOW_READ.getType(),
                ScopeEnum.USER_TOP_READ.getType()
                )
        );
        model.addAttribute("spotifyResponseType", ResponseTypeEnum.CODE.getType());
        return "test";
    }
}
