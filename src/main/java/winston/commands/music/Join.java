package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

@SuppressWarnings({"ConstantConditions"})
public class Join implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();

        if (botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("I am already in a voice channel!").queue();
            return;
        }

        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        if (!authorVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("You need to be in a voice channel to use this command!").queue();
            return;
        }

        AudioManager audioManager = ctx.getGuild().getAudioManager();
        VoiceChannel authorVoiceChannel = authorVoiceState.getChannel();
        if (bot.hasPermission(Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(authorVoiceChannel);
            textChannel.sendMessage("Connecting to `\uD83D\uDD0A` **" + authorVoiceChannel.getName() + "** `\uD83D\uDD0A`").queue();
        } else {
            textChannel.sendMessage("I need Voice Permissions To Join '" + authorVoiceChannel.getName() + "'").queue();
        }
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Makes Winston Join the Specified Voice Channel";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }
}
