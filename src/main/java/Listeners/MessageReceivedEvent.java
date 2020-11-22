package Listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MessageReceivedEvent extends ListenerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final List<String> voiceLines = new ArrayList<>();

    public MessageReceivedEvent() {
        Collections.addAll(
                voiceLines,
                "Are you with me?",
                "Is this on?",
                "How Embarrassing!",
                "Oh Yeah!"
        );
    }

    @Override
    public void onMessageReceived(net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        int random = new Random().nextInt(voiceLines.size());
        String message = event.getMessage().getContentRaw();
        String randomVoiceLine = voiceLines.get(random);

        if (message.toLowerCase().contains("winston")) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + randomVoiceLine).queue();
            LOGGER.info("Voice Line Sent! | Voice Line That Was Chosen: '{}'", randomVoiceLine);
        }
    }
}
