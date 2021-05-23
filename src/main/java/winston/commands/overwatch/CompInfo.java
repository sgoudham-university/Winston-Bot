package winston.commands.overwatch;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import command.CommandContext;
import command.ICommand;
import models.Player.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.text.WordUtils;
import winston.bot.Overwatch;
import winston.bot.config.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class CompInfo implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();
        User author = ctx.getAuthor();
        ArrayList<Page> pages = new ArrayList<>();

        Player player = new Overwatch().getPlayerStats(args);
        pages.add(new Page(buildTankEmbed(player, ctx)));
        pages.add(new Page(buildDamageEmbed(player, ctx)));
        pages.add(new Page(buildSupportEmbed(player, ctx)));

        Object embedContent = pages.get(0).getContent();
        ctx.getEvent().getChannel().sendMessage((MessageEmbed) embedContent).queue(success ->
                Pages.paginate(success, pages, 120, TimeUnit.SECONDS, Predicate.isEqual(author))
        );
        Logger.LOGGER.info("Player Competitive Statistics Sent For {}!", args);

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
    public String getUsage() {
        return "`comp <platform> <region> <playerName>`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("competitive", "ranked");
    }

    @Override
    public String getPackage() { return "Overwatch"; }
}
