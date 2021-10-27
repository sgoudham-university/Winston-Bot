package me.goudham.winston.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.domain.music.Playlist;
import me.goudham.winston.domain.music.Track;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SpotifyServiceImpl implements SpotifyService {
    private final SpotifyApi spotifyApi;
    private Instant expiryTime;

    @Inject
    public SpotifyServiceImpl(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
        refreshAccessToken();
    }

    private void refreshAccessToken() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        ClientCredentials clientCredentials;
        try {
            clientCredentials = clientCredentialsRequest.execute();
            expiryTime = Instant.now();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public Playlist getPlaylist(String playlistId) {
        if (accessTokenExpired()) {
            refreshAccessToken();
        }
        GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistId).build();
        String name = "";
        List<Track> tracks = new ArrayList<>();

        try {
            com.wrapper.spotify.model_objects.specification.Playlist playlist = getPlaylistRequest.execute();
            name = playlist.getName();
            getTracks(tracks, playlistId, 0);
        } catch (IOException | SpotifyWebApiException | ParseException exp) {
            exp.printStackTrace();
            if (exp instanceof SpotifyWebApiException swae) {
                if (swae.getMessage().contains("401")) {
                    refreshAccessToken();
                    return getPlaylist(playlistId);
                }
            }
        }

        return new Playlist(name, tracks);
    }

    private Paging<PlaylistTrack> getPlaylistTracks(String playlistId, int offset) {
        GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
                .getPlaylistsItems(playlistId)
                .offset(offset)
                .build();

        try {
            return getPlaylistsItemsRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException exp) {
            exp.printStackTrace();
            if (exp instanceof SpotifyWebApiException swae) {
                if (swae.getMessage().contains("401")) {
                    refreshAccessToken();
                    return getPlaylistTracks(playlistId, offset);
                }
            }
        }

        return null;
    }

    private void getTracks(List<Track> tracks, String playlistId, int offset) {
        Paging<PlaylistTrack> playlistTracks = getPlaylistTracks(playlistId, offset);

        tracks.addAll(Arrays.stream(playlistTracks.getItems())
                .filter(item -> !item.getIsLocal())
                .map(item -> (com.wrapper.spotify.model_objects.specification.Track) item.getTrack())
                .map(track -> {
                    String name = track.getName();
                    String artistNames = Arrays.stream(track.getArtists())
                            .map(ArtistSimplified::getName)
                            .collect(Collectors.joining(", "));
                    String uri = track.getExternalUrls().get("spotify");
                    String image = track.getAlbum().getImages()[1].getUrl();
                    return new Track(name, artistNames, uri, image);
                }).toList());
        if (playlistTracks.getNext() != null) {
            offset += 100;
            getTracks(tracks, playlistId, offset);
        }
    }

    @Override
    public Track getTrack(String trackId) {
        if (accessTokenExpired()) {
            refreshAccessToken();
        }
        GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();
        String name;
        String artistNames;
        String uri;
        String image;

        try {
            com.wrapper.spotify.model_objects.specification.Track track = getTrackRequest.execute();
            name = track.getName();
            artistNames = Arrays.stream(track.getArtists())
                    .map(ArtistSimplified::getName)
                    .collect(Collectors.joining(", "));
            uri = track.getExternalUrls().get("spotify");
            image = track.getAlbum().getImages()[1].getUrl();
        } catch (IOException | SpotifyWebApiException | ParseException exp) {
            exp.printStackTrace();
            return getTrack(trackId);
        }

        return new Track(name, artistNames, uri, image);
    }

    @Override
    public boolean accessTokenExpired() {
        Instant newExpiryTime = expiryTime.plus(1, ChronoUnit.HOURS);
        return Instant.now().isAfter(newExpiryTime);
    }
}
