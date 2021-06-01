package winston.commands.music;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.isUrl;

public class Search implements ICommand {
    private final EventWaiter eventWaiter;

    public Search(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();

        if (cantPerformOperation(ctx)) {
            return;
        }

        String link = String.join(" ", args);
        if (isUrl(link)) {
            textChannel.sendMessage(buildSimpleInfo("Links Are Not Accepted. Use The Play Command!", Color.RED)).queue();
        } else {
            PlayerManager.getInstance().searchAndPlay(ctx, eventWaiter, "ytsearch:" + link);
        }
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getHelp() {
        return "Returns up to 10 results from a youtube search";
    }

    @Override
    public String getUsage() {
        return "`search <text>`";
    }

    @Override
    public List<String> getAliases() { return Collections.singletonList("lookup"); }

    @Override
    public String getPackage() {
        return "Music";
    }
}
