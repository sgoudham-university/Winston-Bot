package winston.commands.music.common;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.awt.*;
import java.net.URI;
import java.util.Date;
import java.util.List;
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

    public static void displayFastForwarding(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Fast Forwarding");
    }

    public static void displayRewinding(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Rewinding");
    }

    public static void displayRestarting(CommandContext ctx, AudioPlayer audioPlayer) {
        mergeSongInfo(ctx, audioPlayer, "Restarting");
    }

    public static void displayRemoved(CommandContext ctx, AudioTrack removedTrack) {
        mergeSongInfo(ctx, removedTrack);
    }

    private static AtomicReference<Long> mergeSongInfo(CommandContext ctx, AudioPlayer audioPlayer, String status) {
        AudioTrack track = audioPlayer.getPlayingTrack();
        String position = formatTime(track.getPosition());
        String duration = formatTime(track.getDuration());

        String embedAuthor = status + "   |   [" + position + "s / " + duration + "s]";
        String embedDesc = getProgressBar(audioPlayer, track);

        return displaySong(ctx, track, embedAuthor, embedDesc);
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
        textChannel.sendMessageEmbeds(nowPlayingEmbed).queue(message -> messageID.set(message.getIdLong()));
        return messageID;
    }

    private static EmbedBuilder getBaseEmbed(CommandContext ctx, String status) {
        User author = ctx.getAuthor();
        String youtubeLogo = "https://www.vexels.com/media/users/3/137425/isolated/thumb/f2ea1ded4d037633f687ee389a571086-youtube-icon-logo-by-vexels.png";

        return new EmbedBuilder()
                .setAuthor(status, null, youtubeLogo)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), author.getEffectiveAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private static EmbedBuilder getBaseEmbedWithPageCounter(User author, CommandContext ctx, int currPage, int totalPages) {
        return new EmbedBuilder()
                .setAuthor("Page " + currPage + " / " + totalPages)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    public static EmbedBuilder buildQueueEmbed(User author, CommandContext ctx, int currPage, int totalPages) {
        return getBaseEmbedWithPageCounter(author, ctx, currPage, totalPages)
                .setThumbnail(null)
                .setTitle("Current Tracks in Queue")
                .setColor(Color.BLUE);
    }

    public static MessageEmbed buildSearchEmbed(CommandContext ctx, List<AudioTrack> searchResults, int numberOfTracksReturned) {
        EmbedBuilder searchEmbed = getBaseEmbed(ctx, "Search Results");
        searchEmbed.setThumbnail(null);
        searchEmbed.setTitle(numberOfTracksReturned + " Results Found  |  :exclamation: Type 'exit' To Exit");
        searchEmbed.setColor(Color.red);

        for (int i = 0; i < numberOfTracksReturned; i++) {
            AudioTrack track = searchResults.get(i);
            AudioTrackInfo trackInfo = track.getInfo();
            String trackIndex = "**" + (i + 1) + ")**  ";
            String duration = "`[" + formatTime(track.getDuration()) + "]`  ";
            String title = getTrimmedTitle(trackInfo.title, 40);

            searchEmbed.appendDescription(trackIndex + duration + title + "\n");
        }

        return searchEmbed.build();
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

    private static String getProgressBar(AudioPlayer audioPlayer, AudioTrack track) {
        float percentage = (100f / track.getDuration() * track.getPosition());
        int barLength = (int) Math.round((double) percentage / 3.33);
        String playButton = "` ▶️ ";
        String trackLine = "⎯";
        String circle = audioPlayer.isPaused() ? "\uD83D\uDD35" : "\uD83D\uDD34";

        return playButton + StringUtils.repeat(trackLine, barLength) + circle
                + StringUtils.repeat(trackLine, 30 - barLength)
                + "  "
                + String.format("%.2f%%", percentage)
                + "`";
    }

    public static String getTrimmedTitle(String title, int minSize) {
        int titleSize = Math.min(title.length(), minSize);
        String suffix = titleSize < title.length() ? "..." : "";
        return title.substring(0, titleSize) + suffix;
    }
}
