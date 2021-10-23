package me.goudham.winston.service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Singleton
public class ValidationServiceImpl implements ValidationService {
    private final Color colour = Color.RED;
    private final EmbedService embedService;

    @Inject
    public ValidationServiceImpl(EmbedService embedService) {
        this.embedService = embedService;
    }

    @Override
    public boolean cantPerformOperation(SlashCommandEvent slashCommandEvent) {
        TextChannel textChannel = slashCommandEvent.getTextChannel();
        GuildVoiceState authorVoiceState = slashCommandEvent.getMember().getVoiceState();
        GuildVoiceState botVoiceState = slashCommandEvent.getGuild().getSelfMember().getVoiceState();

        return botNotInVoiceChannel(botVoiceState, slashCommandEvent) ||
                memberNotInVoiceChannel(authorVoiceState, slashCommandEvent) ||
                bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel);
    }

    @Override
    public boolean botInVoiceChannel(GuildVoiceState botVoiceState, SlashCommandEvent slashCommandEvent) {
        if (botVoiceState.inVoiceChannel()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("I am already in a voice channel!", colour)).queue();
            return true;
        }
        return false;
    }

    private boolean botNotInVoiceChannel(GuildVoiceState botVoiceState, SlashCommandEvent slashCommandEvent) {
        if (!botVoiceState.inVoiceChannel()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("I need to be in a voice channel to use this command!", colour)).queue();
            return true;
        }
        return false;
    }

    private boolean bothPartiesInDiffVoiceChannels(GuildVoiceState botVoiceState, GuildVoiceState authorVoiceState, TextChannel textChannel) {
        if (!authorVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            textChannel.sendMessageEmbeds(embedService.getSimpleInfoEmbed("This command requires both parties to be in the same voice channel!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean memberNotInVoiceChannel(GuildVoiceState memberVoiceState, SlashCommandEvent slashCommandEvent) {
        if (!memberVoiceState.inVoiceChannel()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("You need to be in a voice channel to use this command!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean noTrackPlaying(AudioPlayer audioPlayer, SlashCommandEvent slashCommandEvent) {
        if (audioPlayer.getPlayingTrack() == null) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("There is no track playing currently!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean dequeIsEmpty(BlockingDeque<AudioTrack> deque, SlashCommandEvent slashCommandEvent) {
        if (deque.isEmpty()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("The queue is currently empty", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean trackPositionInvalid(TrackScheduler scheduler, String trackPosition, SlashCommandEvent slashCommandEvent) {
        BlockingDeque<AudioTrack> deque = scheduler.getDeque();
        int index;

        try {
            index = Integer.parseInt(trackPosition);
        } catch (NumberFormatException nfe) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Please Enter A Valid Number!", colour)).queue();
            return true;
        }

        if (index < 1 || index > deque.size()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Please Enter Position That Is Valid For Current Queue!", colour)).queue();
            return true;
        }

        return false;
    }

    @Override
    public boolean seekPositionInvalid(AudioTrack audioTrack, long trackPositionMill, SlashCommandEvent slashCommandEvent) {
        if (trackPositionMill < audioTrack.getPosition() || trackPositionMill > audioTrack.getDuration()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Please Enter Timestamp That Is Valid For Current Track!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean fastForwardInvalid(AudioTrack audioTrack, long trackPositionMill, SlashCommandEvent slashCommandEvent) {
        if ((audioTrack.getPosition() + trackPositionMill) < audioTrack.getPosition() || trackPositionMill > audioTrack.getDuration()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Please Enter Position That Is Valid For Current Track!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean rewindPositionInvalid(AudioTrack audioTrack, long trackPositionMill, SlashCommandEvent slashCommandEvent) {
        if ((audioTrack.getPosition() - trackPositionMill) > audioTrack.getPosition() || trackPositionMill > audioTrack.getDuration()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Please Enter Position That Is Valid For Current Track!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean searchInvalid(int userSearch, List<AudioTrack> searchResults, SlashCommandEvent slashCommandEvent) {
        if (userSearch < 1 || userSearch > searchResults.size()) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Search Position Invalid! Please Search Again!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public boolean numberFormatInvalid(String userInput, SlashCommandEvent slashCommandEvent) {
        try {
            Integer.parseInt(userInput);
        } catch (NumberFormatException nfe) {
            slashCommandEvent.replyEmbeds(embedService.getSimpleInfoEmbed("Invalid Number Entered!", colour)).queue();
            return true;
        }
        return false;
    }

    @Override
    public int convertToMilliseconds(int trackPosition) {
        return trackPosition * 1000;
    }

    @Override
    public boolean isUrl(String link) {
        try {
            new URL(link).toURI();
        } catch (URISyntaxException | MalformedURLException exp) {
            return false;
        }
        return true;
    }
}
