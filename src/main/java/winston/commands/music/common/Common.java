package winston.commands.music.common;

import command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.text.WordUtils;

import java.awt.*;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Common {
    public static String formatTime(long timeInMillis) {
        long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

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

    static MessageEmbed buildNowPlayingEmbed(CommandContext ctx, String title, String url, String embedAuthor, String embedDesc) {
        String thumbnailUrl = ctx.getSelfUser().getEffectiveAvatarUrl();

        if (url.endsWith(".mp3")) {
            title = WordUtils.capitalize(url.split("audio")[1].substring(1).replace("_", " "));
            url = "";
        } else {
            URI uri = URI.create(url);
            String videoID = uri.getQuery().split("v=")[1];
            thumbnailUrl = "https://img.youtube.com/vi/" + videoID + "/0.jpg";
        }

        return getBaseEmbed(ctx, embedAuthor)
                .setTitle(title, url)
                .setDescription(embedDesc)
                .setThumbnail(thumbnailUrl)
                .setColor(Color.RED)
                .build();
    }
}
