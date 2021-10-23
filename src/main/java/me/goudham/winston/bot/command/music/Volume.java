package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import java.awt.Color;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.Option;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@SlashCommand(
        name = "volume",
        description = "Adjust the volume of the bot",
        options = {
                @Option(
                        optionType = OptionType.INTEGER,
                        name = "input",
                        description = "A value between 0 and 100",
                        isRequired = true
                )
        }
)
public class Volume {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final EmbedService embedService;

    @Inject
    public Volume(ValidationService validationService, PlayerManager playerManager, EmbedService embedService) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.embedService = embedService;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        String newVolume = slashCommandEvent.getOption("input").getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }
        if (validationService.numberFormatInvalid(newVolume, slashCommandEvent)) {
            return;
        }

        int oldVolume = audioPlayer.getVolume();
        audioPlayer.setVolume(Integer.parseInt(newVolume));
        slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbedWithDesc("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`", Color.GREEN)).queue();
    }
}
