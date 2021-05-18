package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collections;
import java.util.List;

import static winston.commands.music.util.Validation.botInVoiceChannel;
import static winston.commands.music.util.Validation.memberNotInVoiceChannel;

@SuppressWarnings({"ConstantConditions"})
public class Join implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();

        if (botInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)) {
            return;
        }

        AudioManager audioManager = ctx.getGuild().getAudioManager();
        VoiceChannel authorVoiceChannel = authorVoiceState.getChannel();
        if (bot.hasPermission(Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(authorVoiceChannel);
            textChannel.sendMessage("Connected to `\uD83D\uDD0A` **#" + authorVoiceChannel.getName() + "** `\uD83D\uDD0A`").queue();
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
        return Collections.singletonList("j");
    }
}
