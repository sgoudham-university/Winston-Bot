package listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import winston.bot.config.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MessageReceivedEvent extends ListenerAdapter {

    private final List<String> voiceLines = new ArrayList<>();
    public static boolean bullyNuggs = false;

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
            Logger.LOGGER.info("Voice Line Sent! | Voice Line That Was Chosen: '{}'", randomVoiceLine);
        }

        if (event.getAuthor().getIdLong() == 337175192751308801L) {
            if (bullyNuggs) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage("Stupid " + event.getAuthor().getAsMention() + " Tried To Type In Chat! Ha ha!").queue();
            } else {
                event.getChannel().sendMessage("Ha Ha! " + event.getAuthor().getAsMention() + " Typed Cringe!!").queue();
            }
        }
    }
}
