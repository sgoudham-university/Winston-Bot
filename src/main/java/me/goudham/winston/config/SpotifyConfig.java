package me.goudham.winston.config;

import com.wrapper.spotify.SpotifyApi;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Factory
public class SpotifyConfig {
    private final String spotifyClientId;
    private final String spotifyClientSecret;

    @Inject
    public SpotifyConfig(@Value("${spotify.client.id}") String spotifyClientId,
                         @Value("${spotify.client.secret}") String spotifyClientSecret) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
    }

    @Singleton
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(spotifyClientId)
                .setClientSecret(spotifyClientSecret)
                .build();
    }
}
