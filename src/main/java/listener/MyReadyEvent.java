package listener;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import winston.bot.config.Logger;

public class MyReadyEvent extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        Logger.LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }
}
