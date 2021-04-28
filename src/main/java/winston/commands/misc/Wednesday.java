package winston.commands.misc;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static util.Constants.RESOURCES_PATH;

public class Wednesday implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        String messageBody = "**It's ~~White Girl~~ Winston Wednesday!**";
        String winstonWednesdayPath = Paths.get(RESOURCES_PATH.toString(), "winstonWednesday.mp4").toString();

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
        return "`!wednesday`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("winstonwednesday", "ww");
    }
}
