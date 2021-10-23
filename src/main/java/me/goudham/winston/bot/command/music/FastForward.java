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
        name = "fastforward",
        description = "Fast forwards to the specified amount in the current song",
        options = {
                @Option(
                        optionType = OptionType.INTEGER,
                        name = "seconds",
                        description = "The amount to fast forward by",
                        isRequired = true
                )
        }
)
public class FastForward {
    private final ValidationService validationService;
    private final EmbedService embedService;
    private final PlayerManager playerManager;
    private final Display display;

    @Inject
    public FastForward(ValidationService validationService, EmbedService embedService, PlayerManager playerManager, Display display) {
        this.validationService = validationService;
        this.embedService = embedService;
        this.playerManager = playerManager;
        this.display = display;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        String fastForwardPosition = slashCommandEvent.getOption("seconds").getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (validationService.cantPerformOperation(slashCommandEvent) || validationService.noTrackPlaying(audioPlayer, slashCommandEvent)) {
            return;
        }

        if (playingTrack.isSeekable()) {
            if (validationService.numberFormatInvalid(fastForwardPosition, slashCommandEvent)) {
                return;
            }
            long fastForwardMillis = validationService.convertToMilliseconds(Integer.parseInt(fastForwardPosition));
            if (validationService.fastForwardInvalid(playingTrack, fastForwardMillis, slashCommandEvent)) {
                return;
            }
            playingTrack.setPosition(playingTrack.getPosition() + fastForwardMillis);
            display.displayFastForwarding(slashCommandEvent, audioPlayer, true);
        } else {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Cannot Fast Forward On This Track!", Color.RED)).queue();
        }
    }
}
