package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Common.joinVoiceChannel;
import static winston.commands.music.common.Validation.isUrl;
import static winston.commands.music.common.Validation.memberNotInVoiceChannel;

@SuppressWarnings("ConstantConditions")
public class Play implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        GuildVoiceState authorVoiceState = ctx.getMember().getVoiceState();
        GuildVoiceState botVoiceState = ctx.getSelfMember().getVoiceState();

        if (memberNotInVoiceChannel(authorVoiceState, textChannel)) {
            return;
        }

        if (args.isEmpty()) {
            textChannel.sendMessage(buildSimpleInfo("Usage -> " + getUsage(), Color.YELLOW)).queue();
            return;
        }

        if (!botVoiceState.inVoiceChannel()) {
            joinVoiceChannel(ctx);
        }

        String link = String.join(" ", args);
        link = !isUrl(link) ? "ytsearch:" + link : link;

        PlayerManager.getInstance().loadAndPlay(ctx, link, false);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays the given track";
    }

    @Override
    public String getUsage() {
        return "`play <link | text>`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("p");
    }

    @Override
    public String getPackage() { return "Music"; }
}
