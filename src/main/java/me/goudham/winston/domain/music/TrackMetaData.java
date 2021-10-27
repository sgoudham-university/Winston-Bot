package me.goudham.winston.domain.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.goudham.winston.bot.command.music.audio.spotify.SpotifyAudioSourceManager;

public record TrackMetaData(
        String name,
        String artists,
        String uri,
        String image,
        AudioTrack audioTrack,
        SpotifyAudioSourceManager spotifyAudioSourceManager
) {
}
