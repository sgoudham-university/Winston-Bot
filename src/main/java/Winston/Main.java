package Winston;

import Winston.Bot.Config;
import Winston.Bot.Winston;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        Winston winston = new Winston();
        winston.start(Config.get("TOKEN"));
    }
}
