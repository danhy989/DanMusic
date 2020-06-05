package com.danhy989.DanMusi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpotifyController {

    @RequestMapping({"/","/index"})
    public String index()
    {
        return "index";
    }
}

