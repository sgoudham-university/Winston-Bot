package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.BlockingQueue;

import static winston.commands.music.util.Common.buildSimpleInfo;

public class Validation {
    private static final Color colour = Color.RED;

    public static boolean botInVoiceChannel(GuildVoiceState botVoiceState, TextChannel textChannel) {
        if (botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage(buildSimpleInfo("I am already in a voice channel!", colour)).queue();
            return true;
        }
        return false;
    }

    public static boolean botNotInVoiceChannel(GuildVoiceState botVoiceState, TextChannel textChannel) {
        if (!botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage(buildSimpleInfo("I need to be in a voice channel to use this command!", colour)).queue();
            return true;
        }
        return false;
    }

    public static boolean bothPartiesInDiffVoiceChannels(GuildVoiceState botVoiceState, GuildVoiceState authorVoiceState, TextChannel textChannel) {
        if (!authorVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            textChannel.sendMessage(buildSimpleInfo("This command requires both parties to be in the same voice channel!", colour)).queue();
            return true;
        }
        return false;
    }

    public static boolean memberNotInVoiceChannel(GuildVoiceState memberVoiceState, TextChannel textChannel) {
        if (!memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage(buildSimpleInfo("You need to be in a voice channel to use this command!", colour)).queue();
            return true;
        }
        return false;
    }

    public static boolean noTrackPlaying(AudioPlayer audioPlayer, TextChannel textChannel) {
        if (audioPlayer.getPlayingTrack() == null) {
            textChannel.sendMessage(buildSimpleInfo("There is no track playing currently!", colour)).queue();
            return true;
        }
        return false;
    }

    public static boolean queueIsEmpty(BlockingQueue<AudioTrack> queue, TextChannel textChannel) {
        if (queue.isEmpty()) {
            textChannel.sendMessage(buildSimpleInfo("The queue is currently empty", colour)).queue();
            return true;
        }
        return false;
    }

}
