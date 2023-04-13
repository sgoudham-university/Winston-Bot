package me.goudham.winston.bot;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.command.CommandManager;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class WinstonBot implements Winston {
    private static final Logger logger = LoggerFactory.getLogger(WinstonBot.class);
    private final Guild guild;
    private final boolean registerCommands;
    private final CommandManager commandManager;

    @Inject
    public WinstonBot(
        Guild guild,
        @Value("${bot.config.registerCommands}") boolean registerCommands,
        CommandManager commandManager
    ) {
        this.guild = guild;
        this.registerCommands = registerCommands;
        this.commandManager = commandManager;
    }

    @Override
    public void run() {
        if (registerCommands) {
            commandManager.registerSlashCommands(guild);
            logger.info("Re-Registering Slash Commands! -> WARNING: Destructive Operation Taking Place");
        } else {
            logger.info("Loading Slash Commands...");
            commandManager.populateCommandMap();
            logger.info("Slash Commands Loaded!");
        }
    }
}
