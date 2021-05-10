package winston;

import exception.FileReaderException;
import winston.bot.Winston;
import winston.bot.config.Config;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws LoginException, IOException, FileReaderException {
        Winston winston = new Winston();
        winston.start(Config.get("TOKEN"));
        winston.startupCache();
    }
}
