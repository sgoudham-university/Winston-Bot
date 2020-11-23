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

        List<String> args = ctx.getArgs();
        ArrayList<Page> pages = new ArrayList<>();

        List<String> playerArgs = args.subList(1, args.size());
        Player player = new Overwatch().getPlayerStats(playerArgs);

        buildAchievementEmbeds(player, pages, ctx, args.get(0));
        ctx.getEvent().getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS, 1));

        LOGGER.info("Player Achievements Sent For {}!", args);

    }

    private void buildAchievementEmbeds(Player player, ArrayList<Page> pages, CommandContext ctx, String typeOfAchievement) {

        switch (typeOfAchievement.toLowerCase()) {
            case "general":
                List<General> generalAchievements = player.getAchievements().getGeneral();
                for (int i = 0; i < generalAchievements.size(); i++)
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, generalAchievements.get(i), ctx, i)));
                break;
            case "damage":
                List<Damage> damageAchievements = player.getAchievements().getDamage();
                for (int i = 0; i < damageAchievements.size(); i++)
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, damageAchievements.get(i), ctx, i)));
                break;
            case "map":
                List<Map> mapAchievements = player.getAchievements().getMaps();
                for (int i = 0; i < mapAchievements.size(); i++)
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, mapAchievements.get(i), ctx, i)));
                break;
            case "special":
                List<Special> specialAchievements = player.getAchievements().getSpecial();
                for (int i = 0; i < specialAchievements.size(); i++)
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, specialAchievements.get(i), ctx, i)));
                break;
            case "support":
                List<Support> supportAchievements = player.getAchievements().getSupport();
                for (int i = 0; i < supportAchievements.size(); i++)
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, supportAchievements.get(i), ctx, i)));
                break;
            case "tank":
                List<Tank> tankAchievements = player.getAchievements().getTank();
                for (int i = 0; i < tankAchievements.size(); i++)
                    pages.add(new Page(PageType.EMBED, getAchievementEmbed(player, tankAchievements.get(i), ctx, i)));
                break;
            default:
                throw new IllegalStateException("Unexpected Achievement: " + typeOfAchievement.toLowerCase());
        }
    }

    private EmbedBuilder getBaseEmbed(Player player, CommandContext ctx, int pageNum) {
        return new EmbedBuilder()
                .setAuthor(player.getUsername() + " | Level: " + player.getLevel().getValue() + " | Page: " + (pageNum + 1))
                .setColor(Color.YELLOW)
                .setTimestamp(new Date().toInstant())
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl());
    }

    private MessageEmbed getAchievementEmbed(Player player, General general, CommandContext ctx, int pageNum) {
        return getBaseEmbed(player, ctx, pageNum)
                .setTitle("General Achievements")
                .setThumbnail(general.getImage())
                .addField(general.getTitle(), general.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Damage damage, CommandContext ctx, int pageNum) {
        return getBaseEmbed(player, ctx, pageNum)
                .setTitle("Damage Achievements")
                .setThumbnail(damage.getImage())
                .addField(damage.getTitle(), damage.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Map map, CommandContext ctx, int pageNum) {
        return getBaseEmbed(player, ctx, pageNum)
                .setTitle("Map Achievements")
                .setThumbnail(map.getImage())
                .addField(map.getTitle(), map.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Special special, CommandContext ctx, int pageNum) {
        return getBaseEmbed(player, ctx, pageNum)
                .setTitle("Special Achievements")
                .setThumbnail(special.getImage())
                .addField(special.getTitle(), special.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Support support, CommandContext ctx, int pageNum) {
        return getBaseEmbed(player, ctx, pageNum)
                .setTitle("Support Achievements")
                .setThumbnail(support.getImage())
                .addField(support.getTitle(), support.getDescription(), true)
                .build();
    }

    private MessageEmbed getAchievementEmbed(Player player, Tank tank, CommandContext ctx, int pageNum) {
        return getBaseEmbed(player, ctx, pageNum)
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
