package winston.commands.overwatch;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static util.Constants.WINSTON_WEDNESDAY;

public class Wednesday implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        String messageBody = "**It's ~~White Girl~~ Winston Wednesday!**";
        String winstonWednesdayPath = WINSTON_WEDNESDAY.toString();

        textChannel.sendMessage(messageBody).addFile(new File(winstonWednesdayPath)).queue();
    }

    @Override
    public String getName() {
        return "wednesday";
    }

    @Override
    public String getHelp() {
        return "Displays the Winston Wednesday Video";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("winstonwednesday", "ww");
    }

    @Override
    public String getPackage() { return "Overwatch"; }
}
