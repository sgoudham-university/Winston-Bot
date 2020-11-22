package Listeners;

import Command.CommandManager;
import Winston.Bot.Config;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Listener extends ListenerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager commandManager = new CommandManager();
    private final List<String> voiceLines = new ArrayList<>();


    public Listener() {
        Collections.addAll(
                voiceLines,
                "Are you with me?",
                "Is this on?",
                "How Embarrassing!",
                "Oh Yeah!"
        );
    }

    @Override
    public void onReady(ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;

        if (event.getMessage().getContentRaw().startsWith(Config.get("PREFIX"))) try {
            commandManager.handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        int random = new Random().nextInt(voiceLines.size());
        String message = event.getMessage().getContentRaw();
        String randomVoiceLine = voiceLines.get(random);

        if (message.toLowerCase().contains("winston")) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + randomVoiceLine).queue();
            LOGGER.info("Voice Line Sent! | Voice Line That Was Chosen: '{}'", randomVoiceLine);
        }
    }
}
