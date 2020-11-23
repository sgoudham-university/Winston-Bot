package Winston.Commands.Player;

import Command.CommandContext;
import Command.ICommand;
import Listeners.Listener;
import Models.Player.Player;
import Winston.Bot.Overwatch;
import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerInfo implements ICommand {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();

        Player player = new Overwatch().getPlayerStats(args);
        MessageEmbed playerInfoMenu = buildMenuEmbed(player, ctx);
        HashMap<String, Page> pages = new HashMap<>();

        pages.put("1️⃣", new Page(PageType.EMBED, buildEndorsementEmbed(player, ctx)));
        pages.put("2️⃣", new Page(PageType.EMBED, buildCompetitiveEmbed(player, ctx)));
        pages.put("3️⃣", new Page(PageType.EMBED, playerInfoMenu));

        ctx.getEvent().getChannel().sendMessage(playerInfoMenu).queue(success -> Pages.categorize(success, pages, 120, TimeUnit.SECONDS));
        LOGGER.info("Player Statistics Sent For {}!", args);
    }

    private EmbedBuilder getBaseEmbed(Player player, CommandContext ctx) {
        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setThumbnail(player.getAvatar())
                .setColor(Color.BLUE)
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private MessageEmbed buildMenuEmbed(Player player, CommandContext ctx) {

        return getBaseEmbed(player, ctx)
                .setTitle("Overbuff Information", player.getOverbuffLink())
                .addField(":one:", "**Endorsements**", false)
                .addField(":two:", "**Competitive**", false)
                .addField(":three:", "**Basic Information**", false)
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
                .build();
    }

    @Override
    public String getName() {
        return "info";
    }
}