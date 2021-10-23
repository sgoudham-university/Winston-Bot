package me.goudham.winston.bot.command.music;

import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.Option;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.MusicService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@SlashCommand(
        name = "play",
        description = "Joins the voice channel if not already present and plays the requested track",
        options = {
                @Option(
                        optionType = OptionType.STRING,
                        name = "input",
                        description = "Track name or youtube playlist link",
                        isRequired = true
                )
        }
)
public class Play {
    private final ValidationService validationService;
    private final MusicService musicService;
    private final PlayerManager playerManager;

    @Inject
    public Play(ValidationService validationService, MusicService musicService, PlayerManager playerManager) {
        this.validationService = validationService;
        this.musicService = musicService;
        this.playerManager = playerManager;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        String inputLink = slashCommandEvent.getOption("input").getAsString();
        GuildVoiceState authorVoiceState = slashCommandEvent.getMember().getVoiceState();
        GuildVoiceState botVoiceState = slashCommandEvent.getGuild().getSelfMember().getVoiceState();

        if (validationService.memberNotInVoiceChannel(authorVoiceState, slashCommandEvent)) {
            return;
        }

        String validatedInputLink = !validationService.isUrl(inputLink) ? "ytsearch:" + inputLink : inputLink;
        if (!botVoiceState.inVoiceChannel()) {
            MessageEmbed joinVoiceChannelEmbed = musicService.joinVoiceChannel(slashCommandEvent);
            slashCommandEvent.replyEmbeds(joinVoiceChannelEmbed).queue();
            playerManager.loadAndPlay(slashCommandEvent, validatedInputLink, false);
        } else {
            playerManager.loadAndPlay(slashCommandEvent, validatedInputLink, true);
        }
    }
}
