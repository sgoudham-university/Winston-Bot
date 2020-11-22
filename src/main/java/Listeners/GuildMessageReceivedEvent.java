package Listeners;

import Command.CommandManager;
import Winston.Bot.Config;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMessageReceivedEvent extends ListenerAdapter {

    private final CommandManager commandManager = new CommandManager();

    @Override
    public void onGuildMessageReceived(net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;

        if (event.getMessage().getContentRaw().startsWith(Config.get("PREFIX"))) try {
            commandManager.handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
