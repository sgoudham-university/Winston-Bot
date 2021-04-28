package listener;

import command.CommandManager;
import exception.HeroNotFoundException;
import exception.PlayerNotFoundException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import winston.bot.config.Config;

public class MyGuildMessageReceivedEvent extends ListenerAdapter {

    private final CommandManager commandManager = new CommandManager();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User author = event.getAuthor();
        Message guildMessage = event.getMessage();

        if (author.isBot() || event.isWebhookMessage()) {
            return;
        }

        if (guildMessage.getContentRaw().startsWith(Config.get("PREFIX"))) {
            try {
                commandManager.handle(event);
            } catch (PlayerNotFoundException | HeroNotFoundException exp) {
                event.getChannel().sendMessage(author.getAsMention() + " " + exp.getMessage()).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
