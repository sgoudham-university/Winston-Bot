import Listeners.Listener;
import Listeners.Overwatch;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LoginException, IOException {
        Overwatch overwatch = new Overwatch();
        overwatch.getPlayerStats();
        new Winston();
    }
}
