package winston.commands;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.JDA;
import winston.bot.config.Logger;

public class Ping implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                ping -> ctx.getChannel()
                        .sendMessageFormat("Reset ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
        );
        Logger.LOGGER.info("Ping Command Sent!");
    }

    @Override
    public String getHelp() {
        return "Shows the ping of bot to the discord servers";
    }

    @Override
    public String getUsage() {
        return "`!ping`";
    }

    @Override
    public String getName() {
        return "ping";
    }
}