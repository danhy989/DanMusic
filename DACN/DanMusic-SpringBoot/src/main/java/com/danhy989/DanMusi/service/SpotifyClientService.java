package com.danhy989.DanMusi.service;

import com.danhy989.DanMusi.dto.TokenDTO;

import java.io.IOException;

public interface SpotifyClientService {
    TokenDTO requestNewAccessToken(TokenDTO tokenDTO) throws IOException;
}
