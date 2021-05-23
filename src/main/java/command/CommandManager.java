package command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import winston.bot.config.Config;
import winston.commands.misc.BullyNuggs;
import winston.commands.misc.Help;
import winston.commands.misc.Ping;
import winston.commands.music.Queue;
import winston.commands.music.*;
import winston.commands.overwatch.CompInfo;
import winston.commands.overwatch.HeroInfo;
import winston.commands.overwatch.PlayerInfo;
import winston.commands.overwatch.Wednesday;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> allCommands = new ArrayList<>();
    private final Map<String, List<ICommand>> commandsMap;

    public CommandManager() {
        ICommand[] allCommands = new ICommand[]{
                new Ping(), new PlayerInfo(), new CompInfo(),
                new HeroInfo(), new Help(this), new BullyNuggs(),
                new Wednesday(), new Join(), new Play(), new Pause(),
                new Clear(), new Skip(), new NowPlaying(), new Queue(),
                new Repeat(), new Leave(), new Resume(), new Voice(),
                new Shuffle(), new SkipTo(), new Remove(), new Seek()
        };
        initialiseAllCommands(allCommands);
        commandsMap = initialiseCommandMap();
    }

    private void initialiseAllCommands(ICommand... commands) {
        for (ICommand command : commands) {
            boolean nameFound = allCommands.stream().anyMatch(it -> it.getName().equalsIgnoreCase(command.getName()));
            if (nameFound) {
                throw new IllegalArgumentException("Command Already Exists!");
            }
            allCommands.add(command);
        }
    }


    @Nullable
    public ICommand getCommand(String search) {
        String lowerSearch = search.toLowerCase();

        for (ICommand command : allCommands) {
            if (command.getName().equals(lowerSearch) || command.getAliases().contains(lowerSearch)) {
                return command;
            }
        }

        return null;
    }

    public void handle(GuildMessageReceivedEvent event) throws Exception {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("PREFIX")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand command = getCommand(invoke);

        if (command != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);
            CommandContext ctx = new CommandContext(event, args);
            command.handle(ctx);
        }
    }

    private Map<String, List<ICommand>> initialiseCommandMap() {
        Map<String, List<ICommand>> commandMap = new HashMap<>();

        for (ICommand newCommand : allCommands) {
            String commandPackage = newCommand.getPackage();
            commandMap.computeIfAbsent(commandPackage, command -> new ArrayList<>());
            commandMap.get(commandPackage).add(newCommand);
        }

        return commandMap;
    }

    public List<ICommand> getAllCommands() {
        return allCommands;
    }

    public Map<String, List<ICommand>> getCommandsMap() {
        return commandsMap;
    }
}
