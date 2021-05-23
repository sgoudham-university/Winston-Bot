package winston.commands.music.common;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.TrackScheduler;

import java.awt.*;
import java.util.List;
import java.util.concurrent.BlockingDeque;

import static winston.commands.music.common.Common.buildSimpleInfo;

public class Validation {
    private static final Color colour = Color.RED;

    @SuppressWarnings("ConstantConditions")
    public static boolean cantPerformOperation(CommandContext ctx) {
        TextChannel textChannel = ctx.getChannel();
        GuildVoiceState authorVoiceState = ctx.getMember().getVoiceState();
        GuildVoiceState botVoiceState = ctx.getSelfMember().getVoiceState();

        return botNotInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel);
    }

    public static boolean botInVoiceChannel(GuildVoiceState botVoiceState, TextChannel textChannel) {
        if (botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage(buildSimpleInfo("I am already in a voice channel!", colour)).queue();
            return true;
        }
        return false;
    }

    private static boolean botNotInVoiceChannel(GuildVoiceState botVoiceState, TextChannel textChannel) {
        if (!botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage(buildSimpleInfo("I need to be in a voice channel to use this command!", colour)).queue();
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean bothPartiesInDiffVoiceChannels(GuildVoiceState botVoiceState, GuildVoiceState authorVoiceState, TextChannel textChannel) {
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

    public static boolean dequeIsEmpty(BlockingDeque<AudioTrack> deque, TextChannel textChannel) {
        if (deque.isEmpty()) {
            textChannel.sendMessage(buildSimpleInfo("The queue is currently empty", colour)).queue();
            return true;
        }
        return false;
    }

    public static boolean trackIndexInvalid(TrackScheduler scheduler, List<String> args, TextChannel textChannel) {
        BlockingDeque<AudioTrack> deque = scheduler.getDeque();
        int index;

        try {
            index = Integer.parseInt(args.get(0));
        } catch (NumberFormatException nfe) {
            textChannel.sendMessage(buildSimpleInfo("Please Enter A Valid Number!", colour)).queue();
            return true;
        }

        if (index < 1 || index > deque.size()) {
            textChannel.sendMessage(buildSimpleInfo("Please Enter Index That Is Valid For Current Queue!", colour)).queue();
            return true;
        }

        return false;
    }
}
