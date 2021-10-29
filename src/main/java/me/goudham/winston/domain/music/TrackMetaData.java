package me.goudham.winston.domain.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.goudham.winston.bot.command.music.audio.spotify.SpotifyAudioSourceManager;

public final class TrackMetaData {
    private final String name;
    private final String artists;
    private final String uri;
    private final String image;
    private final AudioTrack audioTrack;
    private final SpotifyAudioSourceManager spotifyAudioSourceManager;
    private TrackUser trackUser;

    public TrackMetaData(
            String name,
            String artists,
            String uri,
            String image,
            AudioTrack audioTrack,
            SpotifyAudioSourceManager spotifyAudioSourceManager
    ) {
        this.name = name;
        this.artists = artists;
        this.uri = uri;
        this.image = image;
        this.audioTrack = audioTrack;
        this.spotifyAudioSourceManager = spotifyAudioSourceManager;
    }

    public String getName() {
        return name;
    }

    public String getArtists() {
        return artists;
    }

    public String getUri() {
        return uri;
    }

    public String getImage() {
        return image;
    }

    public AudioTrack getAudioTrack() {
        return audioTrack;
    }

    public SpotifyAudioSourceManager getAudioSourceManager() {
        return spotifyAudioSourceManager;
    }

    public void setTrackUser(TrackUser trackUser) {
        this.trackUser = trackUser;
    }

    public TrackUser getTrackUser() {
        return trackUser;
    }
}
