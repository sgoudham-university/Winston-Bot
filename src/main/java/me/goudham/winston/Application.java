package me.goudham.winston;

import io.micronaut.context.ApplicationContext;
import me.goudham.winston.bot.Winston;

public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = ApplicationContext.run(args);
        Winston winston = applicationContext.getBean(Winston.class);
        winston.run();
    }
}
