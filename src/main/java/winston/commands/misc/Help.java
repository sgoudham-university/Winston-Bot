package winston.commands.misc;

import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import winston.bot.config.Config;
import winston.bot.config.Logger;

import java.awt.*;
import java.util.List;

import static winston.commands.music.util.Common.buildSimpleInfo;

public class Help implements ICommand {

    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();

        if (args.isEmpty()) {
            textChannel.sendMessage(buildHelpEmbed(commandManager.getAllCommands(), ctx)).queue();
            Logger.LOGGER.info("Help For All Commands Sent!");
            return;
        }

        String userSearch = args.get(0);
        ICommand command = commandManager.getCommand(userSearch);

        if (command == null) {
            textChannel.sendMessage(buildSimpleInfo("No Command Found For: '" + String.join(" ", args) + "'", Color.RED)).queue();
        } else {
            textChannel.sendMessage(command.getHelp()).queue();
            Logger.LOGGER.info("Help Sent For Command: " + command.getName());
        }
    }

    private void addFields(List<ICommand> allCommands, EmbedBuilder helpEmbed) {
        for (ICommand command : allCommands) {
            String message = command.getUsage() != null ? command.getName() + " | " + command.getUsage() : command.getName();
            helpEmbed.addField(Config.get("PREFIX") + message, command.getHelp(), false);
        }
    }

    private MessageEmbed buildHelpEmbed(List<ICommand> allCommands, CommandContext ctx) {
        Member selfMember = ctx.getSelfMember();
        User user = selfMember.getUser();

        EmbedBuilder helpEmbed = new EmbedBuilder()
                .setAuthor(selfMember.getEffectiveName(), user.getAvatarUrl(), user.getAvatarUrl())
                .setTitle("Winston Commands")
                .setDescription("`<argument>` This means the argument is **required**\n`[argument]` This means the argument is **optional**")
                .setColor(Color.MAGENTA);
        addFields(allCommands, helpEmbed);

        return helpEmbed.build();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "`!help [command]`";
    }

    @Override
    public String getHelp() {
        return "Shows this command!";
    }
}
