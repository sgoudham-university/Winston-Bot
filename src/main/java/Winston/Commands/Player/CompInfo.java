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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CompInfo implements ICommand {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) throws Exception {

        JDA bot = ctx.getJDA();
        List<String> args = ctx.getArgs();
        ArrayList<Page> pages = new ArrayList<>();
        Pages.activate(bot);

        Player player = new Overwatch().getPlayerStats(args);
        pages.add(new Page(PageType.EMBED, buildTankEmbed(player, ctx)));
        pages.add(new Page(PageType.EMBED, buildDamageEmbed(player, ctx)));
        pages.add(new Page(PageType.EMBED, buildSupportEmbed(player, ctx)));

        ctx.getEvent().getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS));
        LOGGER.info("Player Competitive Statistics Sent For {}!", args);

    }

    private MessageEmbed buildTankEmbed(Player player, CommandContext ctx) {
        String tankValue = String.valueOf(player.getCompetitive().getTank().getSkillRating());
        String tankSR = !tankValue.equals("null") ? tankValue : "**N/A**";

        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setThumbnail(player.getCompetitive().getTank().getRank())
                .setTitle("Tank Information")
                .addField("Tank SR", tankSR, true)
                .setColor(Color.MAGENTA)
                .setTimestamp(new Date().toInstant())
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .build();
    }

    private MessageEmbed buildDamageEmbed(Player player, CommandContext ctx) {
        String damageValue = String.valueOf(player.getCompetitive().getDamage().getSkillRating());
        String damageSR = !damageValue.equals("null") ? damageValue : "**N/A**";

        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setThumbnail(player.getCompetitive().getDamage().getRank())
                .setTitle("Damage Information")
                .addField("Damage SR", damageSR, true)
                .setColor(Color.ORANGE)
                .setTimestamp(new Date().toInstant())
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .build();
    }

    private MessageEmbed buildSupportEmbed(Player player, CommandContext ctx) {
        String supportValue = String.valueOf(player.getCompetitive().getSupport().getSkillRating());
        String supportSR = !supportValue.equals("null") ? supportValue : "**N/A**";

        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setThumbnail(player.getCompetitive().getSupport().getRank())
                .setTitle("Support Information")
                .addField("Support SR", supportSR, true)
                .setColor(Color.ORANGE)
                .setTimestamp(new Date().toInstant())
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .build();
    }

    @Override
    public String getName() {
        return "comp";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("competitive", "ranked");
    }
}
