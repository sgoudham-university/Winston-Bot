package winston.commands.player;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import command.CommandContext;
import command.ICommand;
import listeners.Listener;
import models.Player.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import winston.bot.Overwatch;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CompInfo implements ICommand {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();
        ArrayList<Page> pages = new ArrayList<>();

        Player player = new Overwatch().getPlayerStats(args);
        pages.add(new Page(PageType.EMBED, buildTankEmbed(player, ctx)));
        pages.add(new Page(PageType.EMBED, buildDamageEmbed(player, ctx)));
        pages.add(new Page(PageType.EMBED, buildSupportEmbed(player, ctx)));

        ctx.getEvent().getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS));
        LOGGER.info("Player Competitive Statistics Sent For {}!", args);

    }

    private EmbedBuilder getBaseEmbed(Player player, CommandContext ctx) {
        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue() + " | Profile: " + WordUtils.capitalize(player.getPrivacy()))
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private MessageEmbed buildTankEmbed(Player player, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setThumbnail(player.getCompetitive().getTank().getRank())
                .setTitle("Tank Information")
                .addField("Tank SR", getRankValue(String.valueOf(player.getCompetitive().getTank().getSkillRating())), true)
                .setColor(Color.MAGENTA)
                .build();
    }

    private MessageEmbed buildDamageEmbed(Player player, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setThumbnail(player.getCompetitive().getDamage().getRank())
                .setTitle("Damage Information")
                .addField("Damage SR", getRankValue(String.valueOf(player.getCompetitive().getDamage().getSkillRating())), true)
                .setColor(Color.ORANGE)
                .build();
    }

    private MessageEmbed buildSupportEmbed(Player player, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setThumbnail(player.getCompetitive().getSupport().getRank())
                .setTitle("Support Information")
                .addField("Support SR", getRankValue(String.valueOf(player.getCompetitive().getSupport().getSkillRating())), true)
                .setColor(Color.CYAN)
                .build();
    }

    private String getRankValue(String skillRating) {
        return !skillRating.equals("null") ? skillRating : "**N/A**";
    }

    @Override
    public String getName() {
        return "comp";
    }

    @Override
    public String getHelp() {
        return "Returns the Skill Rating of the Player requested";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("competitive", "ranked");
    }
}
