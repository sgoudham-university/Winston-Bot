package listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import winston.bot.config.Logger;

public class ReadyEvent extends ListenerAdapter {

    @Override
    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        Logger.LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

}
