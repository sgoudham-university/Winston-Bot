package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import java.awt.Color;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "shuffle", description = "Shuffles the queue")
public class Shuffle {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final EmbedService embedService;

    @Inject
    public Shuffle(ValidationService validationService, PlayerManager playerManager, EmbedService embedService) {
        this.validationService = validationService;
        this.embedService = embedService;
        this.playerManager = playerManager;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        musicManager.getTrackScheduler().shuffle();
        slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Queue Shuffled ", Color.GREEN)).queue();
    }
}
