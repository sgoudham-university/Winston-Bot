package winston.commands.music.common;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import command.CommandContext;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import static winston.commands.music.common.Common.buildNowPlayingEmbed;
import static winston.commands.music.common.Common.formatTime;

public class Display {
    public static void displayAddedToQueue(CommandContext ctx, AudioTrack track) {
        String duration = formatTime(track.getDuration());

        String embedAuthor = "Added To Queue";
        String embedDesc = "**[" + duration + "s]**";

        displaySong(ctx, track, embedAuthor, embedDesc);
    }

    public static void displayNowPlaying(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Now Playing");
    }

    public static void displayResuming(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Resuming");
    }

    public static void displayAlreadyPaused(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Already Paused");
    }

    public static void displayPausing(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Pausing");
    }

    private static void mergeSongInfo(CommandContext ctx, AudioPlayer audioPlayer, String status) {
        AudioTrack track = audioPlayer.getPlayingTrack();
        String position = formatTime(track.getPosition());
        String duration = formatTime(track.getDuration());

        String embedDesc = "**[" + position + "s / " + duration + "s]**";

        displaySong(ctx, track, status, embedDesc);
    }

    private static void displaySong(CommandContext ctx, AudioTrack track, String embedAuthor, String embedDesc) {
        TextChannel textChannel = ctx.getChannel();
        AudioTrackInfo trackInfo = track.getInfo();

        String title = getTrimmedTitle(trackInfo.title, 70);
        String url = trackInfo.uri;

        MessageEmbed nowPlayingEmbed = buildNowPlayingEmbed(ctx, title, url, embedAuthor, embedDesc);
        textChannel.sendMessage(nowPlayingEmbed).queue();
    }

    public static String getTrimmedTitle(String title, int minSize) {
        int titleSize = Math.min(title.length(), minSize);
        String suffix = titleSize < title.length() ? "..." : "";
        return title.substring(0, titleSize) + suffix;
    }
}
