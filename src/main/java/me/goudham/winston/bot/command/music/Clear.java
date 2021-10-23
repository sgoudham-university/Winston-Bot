package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import java.awt.Color;
import java.util.concurrent.BlockingDeque;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "clear", description = "Clears the queue")
public class Clear {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final EmbedService embedService;

    @Inject
    public Clear(ValidationService validationService, PlayerManager playerManager, EmbedService embedService) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.embedService = embedService;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        BlockingDeque<AudioTrack> deque = musicManager.getTrackScheduler().getDeque();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }

        deque.clear();
        slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Queue Has Been Cleared ✔", Color.GREEN)).queue();
    }
}
