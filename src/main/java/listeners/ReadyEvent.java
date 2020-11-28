package listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadyEvent extends ListenerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

}
