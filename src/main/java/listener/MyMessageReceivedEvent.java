package listener;

import exception.FileReaderException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.MyFileReader;
import util.MyTextFileReader;
import winston.bot.config.Logger;
import winston.commands.misc.BullyNuggsStates;

import java.util.List;
import java.util.Random;

public class MyMessageReceivedEvent extends ListenerAdapter {

    private final MyFileReader myTextFileReader = new MyTextFileReader();
    private final List<String> voiceLines;
    public static BullyNuggsStates bullyNuggs = BullyNuggsStates.STOP;

    public MyMessageReceivedEvent() throws FileReaderException {
        voiceLines = myTextFileReader.read();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();

        if (author.isBot() || event.isWebhookMessage()) {
            return;
        }

        int random = new Random().nextInt(voiceLines.size());
        String message = event.getMessage().getContentRaw();
        String randomVoiceLine = voiceLines.get(random);

        if (message.toLowerCase().contains("winston")) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + randomVoiceLine).queue();
            Logger.LOGGER.info("Voice Line Sent! | Voice Line That Was Chosen: '{}'", randomVoiceLine);
        }

        if (event.getAuthor().getIdLong() == 337175192751308801L) {
            if (bullyNuggs == BullyNuggsStates.ON) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage("Stupid " + event.getAuthor().getAsMention() + " Tried To Type In Chat! Ha ha!").queue();
                Logger.LOGGER.info("Nuggs Bullied Successfully!");
            } else if (bullyNuggs == BullyNuggsStates.OFF) {
                event.getChannel().sendMessage("Ha Ha! " + event.getAuthor().getAsMention() + " Typed Cringe!!").queue();
                Logger.LOGGER.info("Nuggs Bullied Successfully! (Cringe Mode)");
            }
        }
    }
}
