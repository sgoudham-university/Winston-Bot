package winston.commands.misc;

import command.CommandContext;
import command.ICommand;
import listener.MyMessageReceivedEvent;
import winston.bot.config.Config;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BullyNuggs implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();

        if (Objects.requireNonNull(ctx.getEvent().getMessage().getMember()).getIdLong() == Long.parseLong(Config.get("OWNER_ID"))) {
            if (args.get(0).equalsIgnoreCase(BullyNuggsStates.ON.toString())) {
                MyMessageReceivedEvent.bullyNuggs = BullyNuggsStates.ON;
                ctx.getEvent().getChannel().sendMessage("Mode Changed To: " + MyMessageReceivedEvent.bullyNuggs.toString()).queue();
            } else if (args.get(0).equalsIgnoreCase(BullyNuggsStates.OFF.toString())) {
                MyMessageReceivedEvent.bullyNuggs = BullyNuggsStates.OFF;
                ctx.getEvent().getChannel().sendMessage("Mode Changed To: " + MyMessageReceivedEvent.bullyNuggs.toString()).queue();
            } else if (args.get(0).equalsIgnoreCase(BullyNuggsStates.STOP.toString())) {
                MyMessageReceivedEvent.bullyNuggs = BullyNuggsStates.STOP;
                ctx.getEvent().getChannel().sendMessage("Mode Changed To: " + MyMessageReceivedEvent.bullyNuggs.toString()).queue();
            } else if (args.get(0).equalsIgnoreCase("mode")) {
                ctx.getEvent().getChannel().sendMessage("Current Mode: " + MyMessageReceivedEvent.bullyNuggs.toString()).queue();
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
        return "`!bullynuggs <on> | <off> | <stop> | <mode>`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("nuggs");
    }

    @Override
    public String getPackage() { return "Miscellaneous"; }
}
