package me.goudham.winston.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.awt.Color;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

@Singleton
public class MusicServiceImpl implements MusicService {
    private final EmbedService embedService;
    private final PlayerManager playerManager;

    @Inject
    public MusicServiceImpl(EmbedService embedService, PlayerManager playerManager) {
        this.embedService = embedService;
        this.playerManager = playerManager;
    }

    @Override
    public MessageEmbed joinVoiceChannel(SlashCommandEvent slashCommandEvent) {
        AudioManager audioManager = slashCommandEvent.getGuild().getAudioManager();
        VoiceChannel memberVoiceChannel = slashCommandEvent.getMember().getVoiceState().getChannel();
        Member bot = slashCommandEvent.getGuild().getSelfMember();

        String message;
        Color colour;
        if (bot.hasPermission(Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(memberVoiceChannel);
            message = "\uD83D\uDD0A Connected to #" + memberVoiceChannel.getName() + " \uD83D\uDD0A";
            colour = Color.GREEN;
        } else {
            message = "I need Voice Permissions To Join '" + memberVoiceChannel.getName() + "'";
            colour = Color.RED;
        }

        return embedService.getSimpleInfoEmbed(message, colour);
    }

    @Override
    public MessageEmbed leaveVoiceChannel(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        musicManager.getTrackScheduler().setRepeating(false);
        musicManager.getTrackScheduler().getPlayer().setPaused(false);
        musicManager.getTrackScheduler().getDeque().clear();
        musicManager.getAudioPlayer().stopTrack();

        AudioManager audioManager = slashCommandEvent.getGuild().getAudioManager();
        VoiceChannel connectedChannel = audioManager.getConnectedChannel();
        audioManager.closeAudioConnection();

        String message = "\uD83D\uDD0A Leaving #" + connectedChannel.getName() + " \uD83D\uDD0A";
        Color colour = Color.YELLOW;
        return embedService.getSimpleInfoEmbed(message, colour);
    }
}
