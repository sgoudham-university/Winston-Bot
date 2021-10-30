package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.command.annotation.SubCommand;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.Color;

@SlashCommand(name = "loop")
public class Loop {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final EmbedService embedService;

    @Inject
    public Loop(ValidationService validationService, PlayerManager playerManager, EmbedService embedService) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.embedService = embedService;
    }

    @Executable
    @SubCommand(name = "on", description = "Loop the song currently playing")
    public void onCommand(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        TrackScheduler trackScheduler = musicManager.getTrackScheduler();
        boolean isRepeating = trackScheduler.isRepeating();

        MessageEmbed loopEmbed;
        if (isRepeating) {
            loopEmbed = embedService.getSimpleInfoEmbed("Current song already looping!", Color.YELLOW);
        } else {
            trackScheduler.setRepeating(true);
            loopEmbed = embedService.getSimpleInfoEmbed("Looping current song!", Color.GREEN);
        }

        slashCommandEvent.replyEmbeds(loopEmbed).queue();
    }

    @Executable
    @SubCommand(name = "off", description = "Un-loop the song currently playing")
    public void offCommand(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        TrackScheduler trackScheduler = musicManager.getTrackScheduler();
        boolean isRepeating = trackScheduler.isRepeating();

        MessageEmbed loopEmbed;
        if (!isRepeating) {
            loopEmbed = embedService.getSimpleInfoEmbed("Loop already turned off!", Color.YELLOW);
        } else {
            trackScheduler.setRepeating(false);
            loopEmbed = embedService.getSimpleInfoEmbed("Not looping current song anymore!", Color.GREEN);
        }

        slashCommandEvent.replyEmbeds(loopEmbed).queue();
    }
}
