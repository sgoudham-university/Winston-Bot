package me.goudham.winston.bot.command.music.audio.spotify;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public record SpotifyAlbum(
        String name,
        String artists,
        List<AudioTrack> tracks,
        AudioTrack selectedTrack,
        boolean isSearchResult) implements AudioPlaylist {

    @Override
    public String getName() {
        return name;
    }

    public String getArtists() {
        return artists;
    }

    @Override
    public List<AudioTrack> getTracks() {
        return tracks;
    }

    @Override
    public AudioTrack getSelectedTrack() {
        return selectedTrack;
    }
}
