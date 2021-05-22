package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Common.joinVoiceChannel;
import static winston.commands.music.common.Validation.memberNotInVoiceChannel;

@SuppressWarnings("ConstantConditions")
public class Play implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();

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

    private boolean isUrl(String link) {
        try {
            new URL(link).toURI();
        } catch (URISyntaxException | MalformedURLException exp) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays the given song";
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
