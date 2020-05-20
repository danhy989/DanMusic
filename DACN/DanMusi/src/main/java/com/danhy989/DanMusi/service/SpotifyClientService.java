package com.danhy989.DanMusi.service;

import java.io.IOException;

public interface SpotifyClientService {
    boolean requestNewAccessToken() throws IOException;
}
