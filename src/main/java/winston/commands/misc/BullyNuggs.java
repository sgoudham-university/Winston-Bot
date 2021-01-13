package winston.commands.misc;

import command.CommandContext;
import command.ICommand;
import listeners.MessageReceivedEvent;
import winston.bot.config.Config;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BullyNuggs implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();

        if (Objects.requireNonNull(ctx.getEvent().getMessage().getMember()).getIdLong() == Long.parseLong(Config.get("OWNER_ID"))) {
            if (args.get(0).equalsIgnoreCase("on")) {
                MessageReceivedEvent.bullyNuggs = true;
            } else if (args.get(0).equalsIgnoreCase("off")) {
                MessageReceivedEvent.bullyNuggs = false;
            }
        }
    }

    @Override
    public String getName() {
        return "bullynuggs";
    }

    @Override
    public String getHelp() {
        return "Bullies a poor Nuggs by deleting all his messages";
    }

    @Override
    public String getUsage() {
        return "`!bullynuggs <on> | <off>`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("nuggs");
    }
}
