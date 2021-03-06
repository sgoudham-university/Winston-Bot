package winston.bot;

import com.github.ygimenez.exception.InvalidHandlerException;
import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Paginator;
import com.github.ygimenez.model.PaginatorBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import exception.FileReaderException;
import listener.Listener;
import listener.MyGuildMessageReceivedEvent;
import listener.MyMessageReceivedEvent;
import listener.MyReadyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Arrays;

public class Winston {
    private JDA jda;
    private GatewayIntent[] gatewayIntents;
    private final Overwatch overwatch = new Overwatch();

    public Winston() {
        gatewayIntents = new GatewayIntent[]{
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS
        };
    }

    public void startupCache() throws IOException {
        overwatch.startup();
    }

    public void start(String token) throws LoginException, FileReaderException, InvalidHandlerException {
        EventWaiter eventWaiter = new EventWaiter();

        JDA bot = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("Overwatch"))
                .addEventListeners(
                        new Listener(),
                        new MyReadyEvent(),
                        new MyGuildMessageReceivedEvent(eventWaiter),
                        new MyMessageReceivedEvent(),
                        eventWaiter
                )
                .enableIntents(Arrays.asList(gatewayIntents))
                .enableCache(CacheFlag.VOICE_STATE)
                .build();

        Paginator paginator = PaginatorBuilder.createPaginator()
                .setHandler(bot)
                .setDeleteOnCancel(false)
                .shouldRemoveOnReact(true)
                .build();

        Pages.activate(paginator);
    }

    public Winston(JDA jda) {
        this.jda = jda;
    }

    JDA getJDA() {
        return jda;
    }
}
