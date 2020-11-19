import Listeners.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Winston {
    private JDA jda;

    public Winston() throws LoginException {
        JDABuilder.createDefault(Config.get("TOKEN"))
                .setActivity(Activity.playing("Overwatch"))
                .addEventListeners(new Listener())
                .setEnabledIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_EMOJIS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .build();
    }

    public Winston(JDA jda) {
        this.jda = jda;
    }

    public JDA getJDA() {
        return jda;
    }
}
