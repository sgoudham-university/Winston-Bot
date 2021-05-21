package winston.commands.player;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class PlayerInfo implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();
        User author = ctx.getAuthor();

        Player player = new Overwatch().getPlayerStats(args);
        MessageEmbed playerInfoMenu = buildMenuEmbed(player, ctx);
        HashMap<String, Page> pages = new HashMap<>();

        pages.put("1️⃣", new Page(buildEndorsementEmbed(player, ctx)));
        pages.put("2️⃣", new Page(buildCompetitiveEmbed(player, ctx)));
        pages.put("3️⃣", new Page(playerInfoMenu));

        ctx.getEvent().getChannel().sendMessage(playerInfoMenu).queue(success ->
                Pages.categorize(success, pages, 120, TimeUnit.SECONDS, Predicate.isEqual(author))
        );
        Logger.LOGGER.info("Player Statistics Sent For {}!", args);
    }

    private EmbedBuilder getBaseEmbed(Player player, CommandContext ctx) {
        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue() + " | Profile: " + WordUtils.capitalize(player.getPrivacy()))
                .setThumbnail(player.getAvatar())
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private MessageEmbed buildMenuEmbed(Player player, CommandContext ctx) {

        return getBaseEmbed(player, ctx)
                .setTitle("Overbuff Information", player.getOverbuffLink())
                .addField(":one:", "**Endorsements**", false)
                .addField(":two:", "**Competitive**", false)
                .addField(":three:", "**Basic Information**", false)
                .setColor(Color.BLUE)
                .build();
    }

    private MessageEmbed buildCompetitiveEmbed(Player player, CommandContext ctx) {

        String tankValue = String.valueOf(player.getCompetitive().getTank().getSkillRating());
        String damageValue = String.valueOf(player.getCompetitive().getDamage().getSkillRating());
        String supportValue = String.valueOf(player.getCompetitive().getSupport().getSkillRating());

        String tankSR = !tankValue.equals("null") ? tankValue : "**N/A**";
        String damageSR = !damageValue.equals("null") ? damageValue : "**N/A**";
        String supportSR = !supportValue.equals("null") ? supportValue : "**N/A**";

        return getBaseEmbed(player, ctx)
                .setTitle("Competitive Information")
                .addField("Tank SR", tankSR, true)
                .addField("Damage SR", damageSR, true)
                .addField("Support SR", supportSR, true)
                .setColor(Color.GREEN)
                .build();
    }

    private MessageEmbed buildEndorsementEmbed(Player player, CommandContext ctx) {

        String sportsmanship = String.valueOf((int) (player.getEndorsement().getDistribution().getSportsmanship() * 100));
        String shotcaller = String.valueOf((int) (player.getEndorsement().getDistribution().getShotcaller() * 100));
        String teammate = String.valueOf((int) (player.getEndorsement().getDistribution().getTeammate() * 100));

        return getBaseEmbed(player, ctx)
                .setTitle("Endorsement Information |  Level: " + player.getEndorsement().getLevel())
                .addField("Sportsmanship", sportsmanship, true)
                .addField("Shotcaller", shotcaller, true)
                .addField("Good Teammate", teammate, true)
                .setColor(Color.PINK)
                .build();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getUsage() {
        return "`!info <platform> <region> <playerName>`";
    }

    @Override
    public String getHelp() {
        return "Returns General Information about the Player requested";
    }
}