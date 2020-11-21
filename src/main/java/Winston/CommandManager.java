package Winston;

import Command.CommandContext;
import Command.ICommand;
import Winston.Commands.Ping;
import Winston.Commands.Player.BasicInfo;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> allCommands = new ArrayList<>();

    public CommandManager() {
        addCommand(new Ping(), new BasicInfo());
    }

    private void addCommand(ICommand... commands) {

        for (ICommand cmd : commands) {
            boolean nameFound = allCommands.stream().anyMatch(it -> it.getName().equalsIgnoreCase(cmd.getName()));
            if (nameFound) throw new IllegalArgumentException("Command Already Exists!");
            allCommands.add(cmd);
        }


    }

    @Nullable
    private ICommand getCommand(String search) {
        String lowerSearch = search.toLowerCase();

        for (ICommand command : allCommands)
            if (command.getName().equals(lowerSearch) || command.getAliases().contains(lowerSearch)) return command;
        return null;
    }

    public void handle(GuildMessageReceivedEvent event) throws Exception {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("PREFIX")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
