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

@SlashCommand(name = "resume", description = "Resumes the song currently paused")
public class Resume {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final EmbedService embedService;
    private final Display display;

    @Inject
    public Resume(ValidationService validationService, PlayerManager playerManager, EmbedService embedService, Display display) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.embedService = embedService;
        this.display = display;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }

        if (audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
            display.displayResuming(slashCommandEvent, audioPlayer, true);
        } else {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("No Track To Resume!", Color.YELLOW)).queue();
        }
    }
}
