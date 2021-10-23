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
        name = "seek",
        description = "Seeks forward to a specified timestamp in the current song",
        options = {
                @Option(
                        optionType = OptionType.INTEGER,
                        name = "timestamp",
                        description = "The timestamp (in second) to seek to",
                        isRequired = true
                )
        }
)
public class Seek {
    private final ValidationService validationService;
    private final EmbedService embedService;
    private final PlayerManager playerManager;
    private final Display display;

    @Inject
    public Seek(ValidationService validationService, EmbedService embedService, PlayerManager playerManager, Display display) {
        this.validationService = validationService;
        this.embedService = embedService;
        this.playerManager = playerManager;
        this.display = display;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        String seekTimestamp = slashCommandEvent.getOption("timestamp").getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        if (playingTrack.isSeekable()) {
            if (validationService.numberFormatInvalid(seekTimestamp, slashCommandEvent)) {
                return;
            }
            long seekTimestampMillis = validationService.convertToMilliseconds(Integer.parseInt(seekTimestamp));
            if (validationService.seekPositionInvalid(playingTrack, seekTimestampMillis, slashCommandEvent)) {
                return;
            }
            playingTrack.setPosition(seekTimestampMillis);
            display.displayNowPlaying(slashCommandEvent, audioPlayer, true);
        } else {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Cannot Seek On This Track!", Color.RED)).queue();
        }
    }
}
