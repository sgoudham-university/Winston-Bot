package Winston.Bot;

import Listeners.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Winston {
    private JDA jda;
    private GatewayIntent[] gatewayIntents;

    public Winston() {
        gatewayIntents = new GatewayIntent[]{
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS};
    }

    public void start(String token) throws LoginException {
        JDABuilder.createDefault(token)
                .setActivity(Activity.playing("Overwatch"))
                .addEventListeners(new Listener())
                .enableIntents(Arrays.asList(gatewayIntents))
                .build();
    }

    public Winston(JDA jda) {
        this.jda = jda;
    }

    public JDA getJDA() {
        return jda;
    }
}
