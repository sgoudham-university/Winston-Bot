package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.BlockingQueue;

public class Validation {
    public static boolean botInVoiceChannel(GuildVoiceState botVoiceState, TextChannel textChannel) {
        if (botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("I am already in a voice channel!").queue();
            return true;
        }
        return false;
    }

    public static boolean botNotInVoiceChannel(GuildVoiceState botVoiceState, TextChannel textChannel) {
        if (!botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("I need to be in a voice channel to use this command!").queue();
            return true;
        }
        return false;
    }

    public static boolean bothPartiesInDiffVoiceChannels(GuildVoiceState botVoiceState, GuildVoiceState authorVoiceState, TextChannel textChannel) {
        if (!authorVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            textChannel.sendMessage("This command requires both parties to be in the same voice channel!").queue();
            return true;
        }
        return false;
    }

    public static boolean memberNotInVoiceChannel(GuildVoiceState memberVoiceState, TextChannel textChannel) {
        if (!memberVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("You need to be in a voice channel to use this command!").queue();
            return true;
        }
        return false;
    }

    public static boolean noTrackPlaying(AudioPlayer audioPlayer, TextChannel textChannel) {
        if (audioPlayer.getPlayingTrack() == null) {
            textChannel.sendMessage("There is no track playing currently!").queue();
            return true;
        }
        return false;
    }

    public static boolean queueIsEmpty(BlockingQueue<AudioTrack> queue, TextChannel textChannel) {
        if (queue.isEmpty()) {
            textChannel.sendMessage("The queue is currently empty").queue();
            return true;
        }
        return false;
    }

}
