package winston.commands.music.common;

import command.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
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
}
