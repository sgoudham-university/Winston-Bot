package me.goudham.winston.service;

import me.goudham.winston.domain.music.Playlist;
import me.goudham.winston.domain.music.Track;

public interface SpotifyService {
    Playlist getPlaylist(String playlistId);
    Track getTrack(String trackId);
    boolean accessTokenExpired();
}
