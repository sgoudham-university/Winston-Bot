package winston.commands.misc;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import winston.bot.config.Config;
import winston.bot.config.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static winston.commands.music.common.Common.buildSimpleInfo;

public class Help implements ICommand {

    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<Page> pages = new ArrayList<>();
        List<String> args = ctx.getArgs();
        User author = ctx.getAuthor();
        TextChannel textChannel = ctx.getChannel();
        Map<String, List<ICommand>> commandsMap = commandManager.getCommandsMap();

        if (args.isEmpty()) {
            int currentPage = 1;
            int totalPages = commandsMap.size();

            for (Map.Entry<String, List<ICommand>> commandEntry : commandsMap.entrySet()) {
                String commandPackage = commandEntry.getKey();
                List<ICommand> commandList = commandEntry.getValue();
                pages.add(new Page(buildPackageEmbed(ctx, commandPackage, commandList, currentPage, totalPages)));
                currentPage += 1;
            }

            Object embedContent = pages.get(0).getContent();
            textChannel.sendMessageEmbeds((MessageEmbed) embedContent).queue(success ->
                    Pages.paginate(success, pages, 120, TimeUnit.SECONDS, 2, true, Predicate.isEqual(author))
            );
            Logger.LOGGER.info("Help For All Commands Sent!");
            return;
        }

        String userSearch = args.get(0).toLowerCase();
        ICommand command = commandManager.getCommand(userSearch);

        if (command == null) {
            textChannel.sendMessageEmbeds(buildSimpleInfo("No Command Found For: '" + String.join(" ", args) + "'", Color.RED)).queue();
        } else {
            textChannel.sendMessage(command.getHelp()).queue();
            Logger.LOGGER.info("Help Sent For Command: " + command.getName());
        }
    }

    private MessageEmbed buildPackageEmbed(CommandContext ctx, String commandPackage, List<ICommand> commandList, int currentPage, int totalPages) {
        EmbedBuilder packageEmbed = new EmbedBuilder()
                .setAuthor("Page " + currentPage + " / " + totalPages)
                .setTitle(commandPackage + " Commands")
                .setDescription("`<argument>` This means the argument is **required**\n`[argument]` This means the argument is **optional**")
                .setFooter("Requested By " + ctx.getAuthor().getName(), ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant())
                .setColor(Color.MAGENTA);
        addFields(commandList, packageEmbed);

        return packageEmbed.build();
    }

    private void addFields(List<ICommand> allCommands, EmbedBuilder helpEmbed) {
        for (ICommand command : allCommands) {
            String message = command.getUsage() != null ? command.getName() + " | " + command.getUsage() : command.getName();
            helpEmbed.addField(Config.get("PREFIX") + message, command.getHelp(), false);
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "`help [command]`";
    }

    @Override
    public String getHelp() {
        return "Shows this command!";
    }

    @Override
    public List<String> getAliases() { return ICommand.super.getAliases(); }

    @Override
    public String getPackage() { return "Miscellaneous"; }
}
