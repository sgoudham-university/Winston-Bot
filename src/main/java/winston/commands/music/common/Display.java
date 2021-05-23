package winston.commands.music.common;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.text.WordUtils;

import java.awt.*;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static winston.commands.music.common.Common.formatTime;

public class Display {
    public static void displayAddedToQueue(CommandContext ctx, AudioTrack track) {
        String duration = formatTime(track.getDuration());

        String embedAuthor = "Added To Queue";
        String embedDesc = "**[" + duration + "s]**";

        displaySong(ctx, track, embedAuthor, embedDesc);
    }

    public static AtomicReference<Long> displayNowPlaying(CommandContext ctx, AudioPlayer audioPlayer) {
        return mergeSongInfo(ctx, audioPlayer, "Now Playing");
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

    public static void displayRemoved(CommandContext ctx, AudioTrack removedTrack) {
        mergeSongInfo(ctx, removedTrack);
    }

    private static AtomicReference<Long> mergeSongInfo(CommandContext ctx, AudioPlayer audioPlayer, String status) {
        AudioTrack track = audioPlayer.getPlayingTrack();
        String position = formatTime(track.getPosition());
        String duration = formatTime(track.getDuration());

        String embedDesc = "**[" + position + "s / " + duration + "s]**";

        return displaySong(ctx, track, status, embedDesc);
    }

    private static void mergeSongInfo(CommandContext ctx, AudioTrack removedTrack) {
        String position = formatTime(removedTrack.getPosition());
        String duration = formatTime(removedTrack.getDuration());

        String trackPos = "**[" + position + "s / " + duration + "s]**";

        displaySong(ctx, removedTrack, "Removed", trackPos);
    }

    private static AtomicReference<Long> displaySong(CommandContext ctx, AudioTrack track, String status, String trackPos) {
        TextChannel textChannel = ctx.getChannel();
        AudioTrackInfo trackInfo = track.getInfo();

        String title = getTrimmedTitle(trackInfo.title, 70);
        String url = trackInfo.uri;
        MessageEmbed nowPlayingEmbed = buildNowPlayingEmbed(ctx, title, url, status, trackPos);

        AtomicReference<Long> messageID = new AtomicReference<>();
        textChannel.sendMessage(nowPlayingEmbed).queue(message -> messageID.set(message.getIdLong()));
        return messageID;
    }

    private static EmbedBuilder getBaseEmbed(CommandContext ctx, String status) {
        User author = ctx.getAuthor();
        return new EmbedBuilder()
                .setAuthor(status)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private static MessageEmbed buildNowPlayingEmbed(CommandContext ctx, String title, String url, String status, String trackPos) {
        String thumbnailUrl = ctx.getSelfUser().getEffectiveAvatarUrl();

        if (url.endsWith(".mp3")) {
            title = WordUtils.capitalize(url.split("audio")[1].substring(1).replace("_", " "));
            url = "";
        } else {
            URI uri = URI.create(url);
            String videoID = uri.getQuery().split("v=")[1];
            thumbnailUrl = "https://img.youtube.com/vi/" + videoID + "/0.jpg";
        }

        return getBaseEmbed(ctx, status)
                .setTitle(title, url)
                .setDescription(trackPos)
                .setThumbnail(thumbnailUrl)
                .setColor(Color.RED)
                .build();
    }

    public static String getTrimmedTitle(String title, int minSize) {
        int titleSize = Math.min(title.length(), minSize);
        String suffix = titleSize < title.length() ? "..." : "";
        return title.substring(0, titleSize) + suffix;
    }
}
