package winston.commands.misc;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.JDA;
import winston.bot.config.Logger;

public class Ping implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                ping -> ctx.getChannel().sendMessage("Reset ping: " + ping + "sms\nWS ping: " + jda.getGatewayPing() + "sms").queue()
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