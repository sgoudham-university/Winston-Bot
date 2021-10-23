package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import java.awt.Color;
import java.util.concurrent.BlockingDeque;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import me.goudham.winston.command.annotation.Option;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@SlashCommand(
        name = "remove",
        description = "Removes the specified track from the queue",
        options = {
                @Option(
                        optionType = OptionType.INTEGER,
                        name = "position",
                        description = "The position of the track in the queue. If no position is given, it will remove the first track",
                        isRequired = false
                )
        }
)
public class Remove {
    private final ValidationService validationService;
    private final PlayerManager playerManager;
    private final EmbedService embedService;
    private final Display display;

    @Inject
    public Remove(ValidationService validationService, PlayerManager playerManager, EmbedService embedService, Display display) {
        this.validationService = validationService;
        this.playerManager = playerManager;
        this.embedService = embedService;
        this.display = display;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        OptionMapping positionMapping = slashCommandEvent.getOption("position");
        String trackPosition = positionMapping == null ? "null" : positionMapping.getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        TrackScheduler trackScheduler = musicManager.getTrackScheduler();
        BlockingDeque<AudioTrack> deque = trackScheduler.getDeque();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }

        if (deque.isEmpty()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("No Tracks In Queue To Remove!", Color.RED)).queue();
        } else {
            AudioTrack removedTrack;

            if (trackPosition.equals("null")) {
                removedTrack = trackScheduler.removeTrack();
            } else {
                if (validationService.trackPositionInvalid(trackScheduler, trackPosition, slashCommandEvent)) {
                    return;
                }
                int index = Integer.parseInt(trackPosition);
                removedTrack = trackScheduler.removeTrack(index - 1);
            }

            display.displayRemoved(slashCommandEvent, removedTrack, true);
        }
    }
}
