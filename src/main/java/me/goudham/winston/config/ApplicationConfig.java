package me.goudham.winston.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Order;
import io.micronaut.inject.ExecutableMethod;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.command.CommandHandler;
import me.goudham.winston.domain.QueuePages;
import me.goudham.winston.listener.ButtonClickListener;
import me.goudham.winston.listener.OnReadyListener;
import me.goudham.winston.listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Factory
public class ApplicationConfig {
    private final String token;
    private final String guildId;

    @Inject
    public ApplicationConfig(@Value("${bot.token}") String token, @Value("${bot.guild.id}") String guildId) {
        this.token = token;
        this.guildId = guildId;
    }

    @Singleton
    public Map<String, Pair<Object, ExecutableMethod<Object, Object>>> commandMap() {
        return new HashMap<>();
    }

    @Singleton
    public Map<String, QueuePages> queueEmbeds() {
        return new HashMap<>();
    }

    @Singleton
    @Order(1)
    public JDA jda(CommandHandler commandHandler, Map<String, QueuePages> queueEmbeds) throws LoginException, InterruptedException {
        return JDABuilder
                .createDefault(token)
                .setActivity(Activity.playing("Overwatch"))
                .addEventListeners(
                        new OnReadyListener(),
                        new ButtonClickListener(queueEmbeds),
                        new SlashCommandListener(commandHandler)
                )
                .enableIntents(
                        List.of(
                                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                                GatewayIntent.GUILD_VOICE_STATES,
                                GatewayIntent.GUILD_PRESENCES,
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.GUILD_EMOJIS
                        )
                )
                .enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ONLINE_STATUS,
                        CacheFlag.VOICE_STATE,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE
                )
                .build()
                .awaitReady();
    }

    @Singleton
    @Order(2)
    public Guild ownerGuild(JDA jda) {
        Guild ownerGuild = jda.getGuildById(guildId);
        if (ownerGuild == null) {
            throw new RuntimeException("Owner Guild Not Found");
        }
        return ownerGuild;
    }
}
