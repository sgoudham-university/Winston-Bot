package listeners;

import command.CommandManager;
import exceptions.HeroNotFoundException;
import exceptions.PlayerNotFoundException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import winston.bot.Config;

public class GuildMessageReceivedEvent extends ListenerAdapter {

    private final CommandManager commandManager = new CommandManager();

    @Override
    public void onGuildMessageReceived(net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(Config.get("PREFIX"))) {
            try {
                commandManager.handle(event);
            } catch (PlayerNotFoundException | HeroNotFoundException e) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + e.getMessage()).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
