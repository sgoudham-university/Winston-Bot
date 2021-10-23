package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "replay", description = "Replays the current song from the beginning")
public class Replay {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final Display display;

    @Inject
    public Replay(ValidationService validationService, PlayerManager playerManager, Display display) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.display = display;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        playingTrack.setPosition(0);
        display.displayRestarting(slashCommandEvent, audioPlayer, true);
    }
}
