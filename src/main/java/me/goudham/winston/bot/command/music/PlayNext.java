package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.micronaut.context.annotation.Executable;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import me.goudham.winston.command.annotation.Option;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.command.annotation.SubCommand;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.List;

@SlashCommand(name = "playnext")
public class PlayNext {
    private final PlayerManager playerManager;
    private final ValidationService validationService;
    private final Display display;

    public PlayNext(PlayerManager playerManager, ValidationService validationService, Display display) {
        this.playerManager = playerManager;
        this.validationService = validationService;
        this.display = display;
    }

    @Executable
    @SubCommand(
            name = "position",
            description = "Play specified song next, given position of song in queue",
            options = {
                    @Option(
                            optionType = OptionType.INTEGER,
                            name = "input",
                            description = "position of song in queue",
                            isRequired = true
                    )
            }
    )
    public void positionCommand(SlashCommandEvent slashCommandEvent) {
        String position = slashCommandEvent.getOption("input").getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        TrackScheduler trackScheduler = musicManager.getTrackScheduler();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }
        if (validationService.trackPositionInvalid(trackScheduler, position, slashCommandEvent)) {
            return;
        }

        int index = Integer.parseInt(position);
        List<AudioTrack> trackList = trackScheduler.getDequeAsList();
        AudioTrack removedTrack = trackList.remove(index - 1);
        trackList.add(0, removedTrack);
        trackScheduler.setListAsDeque(trackList);

        display.displayPlayingNext(slashCommandEvent, removedTrack, true);
    }
}
