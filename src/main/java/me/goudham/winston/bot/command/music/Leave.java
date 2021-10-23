package me.goudham.winston.bot.command.music;

import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.MusicService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "leave", description = "Leaves the voice channel")
public class Leave {
    private final ValidationService validationService;
    private final MusicService musicService;

    @Inject
    public Leave(ValidationService validationService, MusicService musicService) {
        this.validationService = validationService;
        this.musicService = musicService;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        if (validationService.cantPerformOperation(slashCommandEvent)) {
            return;
        }

        MessageEmbed leaveVoiceChannelEmbed = musicService.leaveVoiceChannel(slashCommandEvent);
        slashCommandEvent.replyEmbeds(leaveVoiceChannelEmbed).queue();
    }
}
