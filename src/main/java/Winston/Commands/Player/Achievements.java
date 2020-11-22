package Winston.Commands.Player;

import Command.CommandContext;
import Command.ICommand;
import Listeners.Listener;
import Models.Player.Achievement.*;
import Models.Player.Player;
import Winston.Bot.Overwatch;
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

public class Achievements implements ICommand {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) throws Exception {

        JDA bot = ctx.getJDA();
        List<String> args = ctx.getArgs();
        ArrayList<Page> pages = new ArrayList<>();
        Pages.activate(bot);

        List<String> playerArgs = args.subList(1, args.size());
        Player player = new Overwatch().getPlayerStats(playerArgs);

        buildAchievementEmbeds(player, pages, ctx, args.get(0));

        ctx.getEvent().getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS, 1));
        LOGGER.info("Player General Achievements Sent For {}!", args);

    }

    private void buildAchievementEmbeds(Player player, ArrayList<Page> pages, CommandContext ctx, String typeOfAchievement) {

        switch (typeOfAchievement.toLowerCase()) {
            case "general":
                for (General general : player.getAchievements().getGeneral())
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, general, ctx)));
                break;
            case "damage":
                for (Damage damage : player.getAchievements().getDamage())
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, damage, ctx)));
                break;
            case "map":
                for (Map map : player.getAchievements().getMaps())
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, map, ctx)));
                break;
            case "special":
                for (Special special : player.getAchievements().getSpecial())
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, special, ctx)));
                break;
            case "support":
                for (Support support : player.getAchievements().getSupport())
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, support, ctx)));
                break;
            case "tank":
                for (Tank tank : player.getAchievements().getTank())
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, tank, ctx)));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeOfAchievement.toLowerCase());
        }
    }

    private EmbedBuilder getBaseEmbed(Player player, CommandContext ctx) {
        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue())
                .setColor(Color.YELLOW)
                .setTimestamp(new Date().toInstant())
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl());
    }

    private MessageEmbed getAchievementEmbed(Player player, General general, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setTitle("General Achievements")
                .setThumbnail(general.getImage())
                .addField(general.getTitle(), general.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Damage damage, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setTitle("Damage Achievements")
                .setThumbnail(damage.getImage())
                .addField(damage.getTitle(), damage.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Map map, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setTitle("Map Achievements")
                .setThumbnail(map.getImage())
                .addField(map.getTitle(), map.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Special special, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setTitle("Special Achievements")
                .setThumbnail(special.getImage())
                .addField(special.getTitle(), special.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Support support, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setTitle("Support Achievements")
                .setThumbnail(support.getImage())
                .addField(support.getTitle(), support.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Tank tank, CommandContext ctx) {
        return getBaseEmbed(player, ctx)
                .setTitle("Tank Achievements")
                .setThumbnail(tank.getImage())
                .addField(tank.getTitle(), tank.getDescription(), true)
                .build();
    }

    @Override
    public String getName() {
        return "achievement";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("achievements", "achieve");
    }
}
