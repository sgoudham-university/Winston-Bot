package me.goudham.winston.bot.command.music;

import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.MusicService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "join", description = "Joins the voice channel")
public class Join {
    private final ValidationService validationService;
    private final MusicService musicService;

    @Inject
    public Join(ValidationService validationService, MusicService musicService) {
        this.validationService = validationService;
        this.musicService = musicService;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildVoiceState authorVoiceState = slashCommandEvent.getMember().getVoiceState();
        GuildVoiceState botVoiceState = slashCommandEvent.getGuild().getSelfMember().getVoiceState();

        if (validationService.botInVoiceChannel(botVoiceState, slashCommandEvent) || validationService.memberNotInVoiceChannel(authorVoiceState, slashCommandEvent)) {
            return;
        }

        MessageEmbed joinVoiceChannelEmbed = musicService.joinVoiceChannel(slashCommandEvent);
        slashCommandEvent.replyEmbeds(joinVoiceChannelEmbed).queue();
    }
}
