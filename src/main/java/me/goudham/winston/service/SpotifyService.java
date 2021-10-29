package me.goudham.winston.service;

import me.goudham.winston.domain.music.Album;
import me.goudham.winston.domain.music.Playlist;
import me.goudham.winston.domain.music.Track;

public interface SpotifyService {
    Track getTrack(String trackId);
    Playlist getPlaylist(String playlistId);
    Album getAlbum(String albumId);
    boolean accessTokenExpired();
}
