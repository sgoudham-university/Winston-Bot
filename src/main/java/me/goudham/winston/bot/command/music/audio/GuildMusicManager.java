package me.goudham.winston.bot.command.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.goudham.winston.service.Display;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioPlayerSendHandler sendHandler;

    GuildMusicManager(AudioPlayerManager manager, SlashCommandEvent slashCommandEvent, Display display) {
        audioPlayer = manager.createPlayer();
        trackScheduler = new TrackScheduler(audioPlayer, manager, slashCommandEvent, display);
        audioPlayer.addListener(trackScheduler);
        sendHandler = new AudioPlayerSendHandler(audioPlayer);
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }
}
