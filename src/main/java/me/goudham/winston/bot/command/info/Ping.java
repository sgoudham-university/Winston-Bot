package me.goudham.winston.bot.command.info;

import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.service.EmbedService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@SlashCommand(name = "ping", description = "Display the latency of the bot")
public class Ping {
    private final EmbedService embedService;

    @Inject
    public Ping(EmbedService embedService) {
        this.embedService = embedService;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        JDA jda = slashCommandEvent.getJDA();
        jda.getRestPing().queue(ping -> {
            MessageEmbed messageEmbed = embedService.getBaseEmbed()
                    .setDescription("**Rest ping: " + ping + "ms**\n**WS ping: " + jda.getGatewayPing() + "ms**")
                    .build();
            slashCommandEvent.replyEmbeds(messageEmbed).queue();
        });
    }
}