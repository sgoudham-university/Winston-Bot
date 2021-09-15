package winston.commands.overwatch;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import command.CommandContext;
import command.ICommand;
import exception.HeroNotFoundException;
import models.Hero.Ability;
import models.Hero.Hero;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import winston.bot.Overwatch;
import winston.bot.config.Logger;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class HeroInfo implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();
        User author = ctx.getAuthor();
        Map<String, Hero> allHeroes = Overwatch.getAllHeroes();
        ArrayList<Page> pages = new ArrayList<>();

        try {
            Hero hero = allHeroes.get(args.get(0).toLowerCase());

            pages.add(new Page(buildHeroInfoEmbed(hero, ctx, hero.getAbilities().size() + 1)));
            for (int i = 0; i < hero.getAbilities().size(); i++) {
                pages.add(new Page(buildHeroAbilitiesEmbeds(hero, hero.getAbilities().get(i), ctx, i, hero.getAbilities().size() + 1)));
            }

            Object content = pages.get(0).getContent();
            ctx.getEvent().getChannel().sendMessageEmbeds((MessageEmbed) content).queue(success ->
                    Pages.paginate(success, pages, 120, TimeUnit.SECONDS, 2, true, Predicate.isEqual(author))
            );
            Logger.LOGGER.info("Overwatch Hero: " + hero.getName() + " Information Sent!");

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
    public String getHelp() {
        return "Returns a Specified Overwatch Hero";
    }

    @Override
    public String getUsage() {
        return "`hero <name>`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("character", "char");
    }

    @Override
    public String getPackage() { return "Overwatch"; }
}
