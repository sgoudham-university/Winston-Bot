package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.Option;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@SlashCommand(
        name = "rewind",
        description = "Rewinds the current song",
        options = {
                @Option(
                        optionType = OptionType.INTEGER,
                        name = "seconds",
                        description = "Number of seconds to rewind by",
                        isRequired = true
                )
        }
)
public class Rewind {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final Display display;

    @Inject
    public Rewind(ValidationService validationService, PlayerManager playerManager, Display display) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.display = display;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        String rewindPosition = slashCommandEvent.getOption("seconds").getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        if (validationService.numberFormatInvalid(rewindPosition, slashCommandEvent)) {
            return;
        }

        long rewindPositionMilliseconds = validationService.convertToMilliseconds(Integer.parseInt(rewindPosition));
        if (validationService.rewindPositionInvalid(playingTrack, rewindPositionMilliseconds, slashCommandEvent)) {
            return;
        }

        long currentPosition = playingTrack.getPosition();
        long newPosition = currentPosition - rewindPositionMilliseconds;
        playingTrack.setPosition(newPosition);
        display.displayRewinding(slashCommandEvent, audioPlayer, true);
    }
}
