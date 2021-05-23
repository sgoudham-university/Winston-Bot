package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.joinVoiceChannel;
import static winston.commands.music.common.Validation.botInVoiceChannel;
import static winston.commands.music.common.Validation.memberNotInVoiceChannel;

@SuppressWarnings({"ConstantConditions"})
public class Join implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel textChannel = ctx.getChannel();
        GuildVoiceState authorVoiceState = ctx.getMember().getVoiceState();
        GuildVoiceState botVoiceState = ctx.getSelfMember().getVoiceState();

        if (botInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)) {
            return;
        }

        joinVoiceChannel(ctx);
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

    @Override
    public String getPackage() { return "Music"; }
}
