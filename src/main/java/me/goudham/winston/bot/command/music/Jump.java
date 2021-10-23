package me.goudham.winston.bot.command.music;

import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import me.goudham.winston.command.annotation.Option;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@SlashCommand(
        name = "jump",
        description = "Jump to a specific song in the queue",
        options = {
                @Option(
                        optionType = OptionType.INTEGER,
                        name = "position",
                        description = "The position of the track to jump to in the queue",
                        isRequired = true
                )
        }
)
public class Jump {
    private final ValidationService validationService;
    private final PlayerManager playerManager;

    @Inject
    public Jump(ValidationService validationService, PlayerManager playerManager) {
        this.validationService = validationService;
        this.playerManager = playerManager;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        String position = slashCommandEvent.getOption("position").getAsString();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        TrackScheduler trackScheduler = musicManager.getTrackScheduler();

        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }
        if (validationService.trackPositionInvalid(trackScheduler, position, slashCommandEvent)) {
            return;
        }

        int index = Integer.parseInt(position);
        trackScheduler.skipTo(index - 1);
        trackScheduler.nextTrack(true);
    }
}
