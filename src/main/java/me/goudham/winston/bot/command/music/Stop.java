package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.Color;

@SlashCommand(name = "stop", description = "Stop the currently playing song and clear the queue")
public class Stop {
    private final ValidationService validationService;
    private final EmbedService embedService;
    private final PlayerManager playerManager;

    @Inject
    public Stop(ValidationService validationService, PlayerManager playerManager, EmbedService embedService) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.embedService = embedService;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        TrackScheduler trackScheduler = musicManager.getTrackScheduler();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }

        trackScheduler.setRepeating(false);
        trackScheduler.getPlayer().setPaused(false);
        trackScheduler.getDeque().clear();
        audioPlayer.stopTrack();
        trackScheduler.startTimer();

        String message = "\uD83D\uDD0A Stopping Music & Clearing Queue \uD83D\uDD0A";
        Color colour = Color.YELLOW;
        slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed(message, colour)).queue();
    }
}
