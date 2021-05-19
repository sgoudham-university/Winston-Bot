package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Common {
    @SuppressWarnings("ConstantConditions")
    public static void joinVoiceChannel(CommandContext ctx) {
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        VoiceChannel authorVoiceChannel = ctx.getMember().getVoiceState().getChannel();
        Member bot = ctx.getSelfMember();
        TextChannel textChannel = ctx.getChannel();

        String message;
        Color colour;
        if (bot.hasPermission(Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(authorVoiceChannel);
            message = "\uD83D\uDD0A Connected to #" + authorVoiceChannel.getName() + " \uD83D\uDD0A";
            colour = Color.GREEN;
        } else {
            message = "I need Voice Permissions To Join '" + authorVoiceChannel.getName() + "'";
            colour = Color.RED;
        }
        textChannel.sendMessage(buildSimpleInfo(message, colour)).queue();
    }

    static void displayAddedToQueue(CommandContext ctx, AudioTrack track) {
        String duration = formatTime(track.getDuration());

        String embedTitle = "Added To Queue";
        String embedDesc = "**[" + duration + "s]**";

        displaySong(ctx, track, embedTitle, embedDesc);
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

        String title = trackInfo.title;
        String url = trackInfo.uri;
        int titleSize = Math.min(title.length(), 70);
        String suffix = titleSize < title.length() ? "..." : "";
        String newTitle = title.substring(0, titleSize) + suffix;

        MessageEmbed nowPlayingEmbed = buildNowPlayingEmbed(ctx, newTitle, url, embedAuthor, embedDesc);
        textChannel.sendMessage(nowPlayingEmbed).queue();
    }

    public static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static MessageEmbed buildSimpleInfo(String message, Color colour) {
        return new EmbedBuilder()
                .setAuthor(message)
                .setColor(colour)
                .build();
    }

    private static EmbedBuilder getBaseEmbed(CommandContext ctx, String embedAuthor) {
        User author = ctx.getAuthor();

        return new EmbedBuilder()
                .setAuthor(embedAuthor)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private static MessageEmbed buildNowPlayingEmbed(CommandContext ctx, String title, String url, String embedAuthor, String embedDesc) {
        URI uri = URI.create(url);
        String videoID = uri.getQuery().split("v=")[1];
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoID + "/0.jpg";

        return getBaseEmbed(ctx, embedAuthor)
                .setTitle(title, url)
                .setDescription(embedDesc)
                .setThumbnail(thumbnailUrl)
                .setColor(Color.RED)
                .build();
    }
}
