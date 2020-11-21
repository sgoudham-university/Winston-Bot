package Winston.Commands.Player;

import Command.CommandContext;
import Command.ICommand;
import Listeners.Listener;
import Listeners.Overwatch;
import Models.Player.Player;
import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
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

        JDA bot = ctx.getJDA();
        Pages.activate(bot);
        List<String> args = ctx.getArgs();

        Player player = new Overwatch().getPlayerStats(args);
        MessageEmbed playerInfoMenu = buildMenuEmbed(player, ctx);
        HashMap<String, Page> pages = new HashMap<>();

        pages.put("1️⃣", new Page(PageType.EMBED, buildEndorsementEmbed(player)));
        pages.put("2️⃣", new Page(PageType.EMBED, buildCompetitiveEmbed(player)));
        pages.put("3️⃣", new Page(PageType.EMBED, playerInfoMenu));

        ctx.getEvent().getChannel().sendMessage(playerInfoMenu).queue(success -> Pages.categorize(success, pages, 60, TimeUnit.SECONDS));
        LOGGER.info("Player Statistics Sent For {}!", args);
    }

    private static MessageEmbed buildMenuEmbed(Player player, CommandContext ctx) {

        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setTitle("Overbuff Information", player.getOverbuffLink())
                .setThumbnail(player.getAvatar())
                .addField(":one:", "**Endorsements**", false)
                .addField(":two:", "**Competitive**", false)
                .addField(":three:", "**Basic Information**", false)
                .setColor(Color.BLUE)
                .setTimestamp(new Date().toInstant())
                .setFooter("Powered by Swagger-OWAPI", ctx.getSelfUser().getAvatarUrl())
                .build();
    }

    private static MessageEmbed buildCompetitiveEmbed(Player player) {

        String tankValue = String.valueOf(player.getCompetitive().getTank().getSkillRating());
        String damageValue = String.valueOf(player.getCompetitive().getDamage().getSkillRating());
        String supportValue = String.valueOf(player.getCompetitive().getSupport().getSkillRating());

        String tankSR = !tankValue.equals("null") ? tankValue : "**N/A**";
        String damageSR = !tankValue.equals("null") ? damageValue : "**N/A**";
        String supportSR = !tankValue.equals("null") ? supportValue : "**N/A**";

        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setTitle("Competitive Information")
                .setThumbnail(player.getAvatar())
                .setTimestamp(new Date().toInstant())
                .addField("Tank SR", tankSR, true)
                .addField("Damage SR", damageSR, true)
                .addField("Support SR", supportSR, true)
                .setColor(Color.PINK)
                .setFooter("Powered By Swagger-OWAPI").build();
    }

    private static MessageEmbed buildEndorsementEmbed(Player player) {

        String sportsmanship = String.valueOf((int) (player.getEndorsement().getDistribution().getSportsmanship() * 100));
        String shotcaller = String.valueOf((int) (player.getEndorsement().getDistribution().getShotcaller() * 100));
        String teammate = String.valueOf((int) (player.getEndorsement().getDistribution().getTeammate() * 100));

        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setTitle("Endorsement Information |  Level: " + player.getEndorsement().getLevel())
                .setThumbnail(player.getAvatar())
                .setTimestamp(new Date().toInstant())
                .addField("Sportsmanship", sportsmanship, true)
                .addField("Shotcaller", shotcaller, true)
                .addField("Good Teammate", teammate, true)
                .setColor(Color.RED)
                .setFooter("Powered By Swagger-OWAPI").build();
    }

    @Override
    public String getName() {
        return "info";
    }
}