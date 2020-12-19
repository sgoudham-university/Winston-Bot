package winston;

import winston.bot.Winston;
import winston.bot.config.Config;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws LoginException, IOException {
        Winston winston = new Winston();
        winston.start(Config.get("TOKEN"));
        winston.startupCache();
    }
}
