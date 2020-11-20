package Winston;

import Command.CommandContext;
import Command.ICommand;
import Winston.Commands.Ping;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new Ping());
    }

    private void addCommand(ICommand command) {
        boolean nameFound = this.commands.stream().anyMatch(it -> it.getName().equalsIgnoreCase(command.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("Command Already Exists!");
        }

        commands.add(command);
    }

    @Nullable
    private ICommand getCommand(String search) {
        String lowerSearch = search.toLowerCase();

        for (ICommand command: this.commands) {
            if (command.getName().equals(lowerSearch) || command.getAliases().contains(lowerSearch)) {
                return command;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("PREFIX")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
