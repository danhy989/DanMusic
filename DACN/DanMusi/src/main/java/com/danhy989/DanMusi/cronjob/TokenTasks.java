package com.danhy989.DanMusi.cronjob;

import com.danhy989.DanMusi.service.SpotifyClientService;
import com.danhy989.DanMusi.statics.TokenStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class TokenTasks {

    private static final Logger log = LoggerFactory.getLogger(TokenTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private SpotifyClientService spotifyClientService;

    TokenTasks(SpotifyClientService spotifyClientService){this.spotifyClientService=spotifyClientService;}


    @Scheduled(fixedRate = 1000*60*50)
    public void reportCurrentTime() {
            log.info("The time is now {}", dateFormat.format(new Date()));
            try {
                boolean rs = spotifyClientService.requestNewAccessToken();
                if (rs) {
                    log.info("Access token has been refreshed");
                } else {
                    log.error("Can't request new access token");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
