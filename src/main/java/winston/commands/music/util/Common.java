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

        if (bot.hasPermission(Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(authorVoiceChannel);
            textChannel.sendMessage("Connected to `\uD83D\uDD0A` **#" + authorVoiceChannel.getName() + "** `\uD83D\uDD0A`").queue();
        } else {
            textChannel.sendMessage("I need Voice Permissions To Join '" + authorVoiceChannel.getName() + "'").queue();
        }
    }

    public static void displayNowPlayingSong(CommandContext ctx, AudioPlayer audioPlayer) {
        TextChannel textChannel = ctx.getChannel();

        AudioTrack track = audioPlayer.getPlayingTrack();
        AudioTrackInfo trackInfo = track.getInfo();
        String title = trackInfo.title;
        String url = trackInfo.uri;
        String position = formatTime(track.getPosition());
        String duration = formatTime(track.getDuration());
        String progress = "**[" + position + "s / " + duration + "s]**";

        MessageEmbed nowPlayingEmbed = buildNowPlayingEmbed(ctx, title, url, progress);
        textChannel.sendMessage(nowPlayingEmbed).queue();
    }

    public static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private static EmbedBuilder getBaseEmbed(CommandContext ctx) {
        User author = ctx.getAuthor();

        return new EmbedBuilder()
                .setAuthor("Now Playing")
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private static MessageEmbed buildNowPlayingEmbed(CommandContext ctx, String title, String url, String progress) {
        URI uri = URI.create(url);
        String videoID = uri.getQuery().split("v=")[1];
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoID + "/0.jpg";

        return getBaseEmbed(ctx)
                .setTitle(title, url)
                .setDescription(progress)
                .setThumbnail(thumbnailUrl)
                .setColor(Color.RED)
                .build();
    }
}
