package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import java.awt.Color;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "loop", description = "Loops the song currently playing")
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
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        System.out.println(slashCommandEvent);
        TrackScheduler trackScheduler = musicManager.getTrackScheduler();
        trackScheduler.setRepeating(!trackScheduler.isRepeating());

        MessageEmbed loopMessageEmbed = trackScheduler.isRepeating() ?
                embedService.getSimpleInfoEmbed("Looping Current Track!", Color.GREEN) :
                embedService.getSimpleInfoEmbed("Not Looping Current Track Anymore!", Color.RED);

        slashCommandEvent.replyEmbeds(loopMessageEmbed).queue();
    }
}
