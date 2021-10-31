package me.goudham.winston.command;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
class SlashCommandManager implements CommandManager {
    private static final Logger logger = LoggerFactory.getLogger(SlashCommandManager.class);
    private final boolean registerCommandsGlobally;
    private final boolean registerCommandsForGuild;
    private final CommandLoader commandLoader;
    private final JDA jda;

    @Inject
    SlashCommandManager(@Value("${bot.config.registerCommandsGlobally}") boolean registerCommandsGlobally,
                        @Value("${bot.config.registerCommandsForGuild}") boolean registerCommandsForGuild,
                        CommandLoader commandLoader,
                        JDA jda) {
        this.registerCommandsGlobally = registerCommandsGlobally;
        this.registerCommandsForGuild = registerCommandsForGuild;
        this.commandLoader = commandLoader;
        this.jda = jda;
    }

    @Override
    public void populateCommandMap() {
        commandLoader.populateCommandMap();
    }

    @Override
    public void registerSlashCommands(Guild guild) {
        CommandListUpdateAction commands = null;
        if (registerCommandsGlobally) {
            commands = jda.updateCommands();
            logger.info("Updating Commands Globally");
        }
        if (registerCommandsForGuild) {
            commands = guild.updateCommands();
            logger.warn("Updating Commands For Guild: " + guild.getName());
        }

        if (commands != null) {
            List<CommandData> registeredSlashCommands = commandLoader.registerSlashCommands();
            commands.addCommands(registeredSlashCommands).queue();
        }
    }
}
