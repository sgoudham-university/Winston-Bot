package me.goudham.winston.bot.command.fun;

import io.micronaut.context.annotation.Executable;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.io.scan.DefaultClassPathResourceLoader;
import me.goudham.winston.command.annotation.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.io.InputStream;

@SlashCommand(name = "winstonwednesday", description = "Display the very epic winston wednesday video")
public class WinstonWednesday {
    private final ResourceLoader resourceLoader = new DefaultClassPathResourceLoader(ClassLoader.getSystemClassLoader());

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        slashCommandEvent.deferReply().queue();
        InteractionHook hook = slashCommandEvent.getHook();
        InputStream resource = resourceLoader.getResourceAsStream("classpath:winstonWednesday.mp4").orElseThrow();

        hook.sendMessage("**It's ~~White Girl~~ Winston Wednesday!**")
                .addFile(resource, "Winston.mp4")
                .queue();
    }
}
