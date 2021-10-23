package me.goudham.winston.service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import me.goudham.winston.bot.command.music.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface ValidationService {
    boolean cantPerformOperation(SlashCommandEvent slashCommandEvent);
    boolean botInVoiceChannel(GuildVoiceState botVoiceState, SlashCommandEvent slashCommandEvent);
    boolean memberNotInVoiceChannel(GuildVoiceState memberVoiceState, SlashCommandEvent slashCommandEvent);
    boolean noTrackPlaying(AudioPlayer audioPlayer, SlashCommandEvent slashCommandEvent);
    boolean dequeIsEmpty(BlockingDeque<AudioTrack> deque, SlashCommandEvent slashCommandEvent);
    boolean trackPositionInvalid(TrackScheduler scheduler, String trackPosition, SlashCommandEvent slashCommandEvent);
    boolean seekPositionInvalid(AudioTrack audioTrack, long trackPositionMill, SlashCommandEvent slashCommandEvent);
    boolean fastForwardInvalid(AudioTrack audioTrack, long trackPositionMill, SlashCommandEvent slashCommandEvent);
    boolean rewindPositionInvalid(AudioTrack audioTrack, long trackPositionMill, SlashCommandEvent slashCommandEvent);
    boolean searchInvalid(int userSearch, List<AudioTrack> searchResults, SlashCommandEvent slashCommandEvent);
    boolean numberFormatInvalid(String userInput, SlashCommandEvent slashCommandEvent);
    int convertToMilliseconds(int trackPosition);
    boolean isUrl(String link);
}
