package me.goudham.winston.bot.command.music;

import io.micronaut.context.annotation.Executable;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.MusicService;
import me.goudham.winston.service.ValidationService;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "boomed", description = "Moral support for when you're down bad")
public class Boomed {
    private final ValidationService validationService;
    private final MusicService musicService;
    private final PlayerManager playerManager;

    public Boomed(ValidationService validationService, MusicService musicService, PlayerManager playerManager) {
        this.validationService = validationService;
        this.musicService = musicService;
        this.playerManager = playerManager;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        GuildVoiceState authorVoiceState = slashCommandEvent.getMember().getVoiceState();
        GuildVoiceState botVoiceState = slashCommandEvent.getGuild().getSelfMember().getVoiceState();

        if (validationService.memberNotInVoiceChannel(authorVoiceState, slashCommandEvent)) {
            return;
        }

        String playlistLink = "https://www.youtube.com/playlist?list=PLpe3gyx0RWMVC7NtXA_Rj_rQmG9ZykbjO";
        if (!botVoiceState.inVoiceChannel()) {
            MessageEmbed joinVoiceChannelEmbed = musicService.joinVoiceChannel(slashCommandEvent);
            slashCommandEvent.replyEmbeds(joinVoiceChannelEmbed).queue();
            playerManager.loadAndPlay(slashCommandEvent, playlistLink, false, true);
        } else {
            playerManager.loadAndPlay(slashCommandEvent, playlistLink, true, true);
        }
    }
}
