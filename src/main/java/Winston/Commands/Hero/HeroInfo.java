package Winston.Commands.Hero;

import Command.CommandContext;
import Command.ICommand;
import Exceptions.HeroNotFoundException;
import Listeners.Listener;
import Models.Hero.Ability;
import Models.Hero.Hero;
import Winston.Bot.Overwatch;
import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HeroInfo implements ICommand {

    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();
        Map<String, Hero> allHeroes = Overwatch.getAllHeroes();
        ArrayList<Page> pages = new ArrayList<>();

        try {

            Hero hero = allHeroes.get(args.get(0).toLowerCase());

            pages.add(new Page(PageType.EMBED, buildHeroInfoEmbed(hero, ctx, hero.getAbilities().size() + 1)));
            for (int i = 0; i < hero.getAbilities().size(); i++) {
                pages.add(new Page(PageType.EMBED, buildHeroAbilitiesEmbeds(hero, hero.getAbilities().get(i), ctx, i, hero.getAbilities().size() + 1)));
            }

            ctx.getEvent().getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS, 2));
            LOGGER.info("Overwatch Hero: " + hero.getName() + " Information Sent!");

        } catch (NullPointerException e) {
            throw new HeroNotFoundException("Hero Not Found!");
        } catch (IndexOutOfBoundsException e) {
            throw new HeroNotFoundException("Arguments Invalid / Not Provided!");
        } catch (Exception e) {
            throw new Exception("Unknown Exception Occurred" + e);
        }


    }

    private EmbedBuilder getBaseInfoEmbed(Hero hero, CommandContext ctx, int index, int totalPages) {
        return new EmbedBuilder()
                .setAuthor(hero.getName() + " | Role: " + hero.getRole() + " | Page " + index + "/" + totalPages)
                .setFooter("Powered By Swagger", ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private MessageEmbed buildHeroInfoEmbed(Hero hero, CommandContext ctx, int totalPages) {
        return getBaseInfoEmbed(hero, ctx, 1, totalPages)
                .setTitle("Hero Information")
                .setThumbnail(hero.getPortraitUrl())
                .setDescription("**Real Name: **" + hero.getStory().getBiography().getRealName()
                        + "\n**Age: **" + hero.getStory().getBiography().getAge()
                        + "\n**Catchphrase: **" + hero.getStory().getCatchPhrase()
                        + "\n**Occupation: **" + hero.getStory().getBiography().getOccupation()
                        + "\n**Base Of Operations: **" + hero.getStory().getBiography().getBaseOfOperations()
                        + "\n**Affiliation: **" + hero.getStory().getBiography().getAffiliation())
                .setColor(Color.ORANGE)
                .build();
    }

    private MessageEmbed buildHeroAbilitiesEmbeds(Hero hero, Ability ability, CommandContext ctx, int index, int totalPages) {
        return getBaseInfoEmbed(hero, ctx, index + 2, totalPages)
                .setTitle(ability.getName())
                .setThumbnail(ability.getIcon())
                .setDescription(ability.getDescription())
                .setColor(Color.RED)
                .build();
    }

    @Override
    public String getName() {
        return "hero";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("character", "char");
    }
}
