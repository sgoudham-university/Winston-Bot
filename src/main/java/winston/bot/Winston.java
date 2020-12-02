package winston.bot;

import com.github.ygimenez.method.Pages;
import listeners.GuildMessageReceivedEvent;
import listeners.Listener;
import listeners.MessageReceivedEvent;
import listeners.ReadyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

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
                GatewayIntent.GUILD_MESSAGE_REACTIONS};
    }

    public void startupCache() throws IOException {
        overwatch.startup();
    }

    public void start(String token) throws LoginException {
        Pages.activate(JDABuilder.createDefault(token)
                .setActivity(Activity.playing("Overwatch"))
                .addEventListeners(new Listener(),
                        new ReadyEvent(),
                        new GuildMessageReceivedEvent(),
                        new MessageReceivedEvent())
                .enableIntents(Arrays.asList(gatewayIntents))
                .build()
        );
    }

    public Winston(JDA jda) {
        this.jda = jda;
    }

    public JDA getJDA() {
        return jda;
    }
}