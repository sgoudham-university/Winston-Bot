package winston.commands;

import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.bot.Config;

import java.util.List;

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
            return;
        }

        String search = args.get(0);
        ICommand command = commandManager.getCommand(search);

        if (command == null) {
            textChannel.sendMessage("No Command Found For: " + search).queue();
        } else {
            textChannel.sendMessage(command.getHelp()).queue();
        }
    }

    private void addFields(List<ICommand> allCommands, EmbedBuilder helpEmbed) {
        allCommands.forEach(command -> {
            helpEmbed.addField(Config.get("PREFIX") + command.getName(), command.getHelp(), false);
        });
    }

    private MessageEmbed buildHelpEmbed(List<ICommand> allCommands, CommandContext ctx) {
        EmbedBuilder helpEmbed = new EmbedBuilder();
        helpEmbed.setAuthor(ctx.getSelfMember().getEffectiveName(), ctx.getSelfMember().getUser().getAvatarUrl(), ctx.getSelfMember().getUser().getAvatarUrl());
        helpEmbed.setTitle("Winston Commands");
        addFields(allCommands, helpEmbed);
        return helpEmbed.build();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows this command!";
    }
}
