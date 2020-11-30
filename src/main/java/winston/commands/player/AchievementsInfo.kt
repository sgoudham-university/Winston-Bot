package winston.commands.player

import com.github.ygimenez.method.Pages
import com.github.ygimenez.model.Page
import com.github.ygimenez.type.PageType
import command.CommandContext
import command.ICommand
import listeners.Listener
import models.Player.Achievement.*
import models.Player.Achievement.Map
import models.Player.Player
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import org.slf4j.LoggerFactory
import winston.bot.Overwatch
import java.awt.Color
import java.util.*
import java.util.concurrent.TimeUnit

class AchievementsInfo : ICommand {
    private val LOGGER = LoggerFactory.getLogger(Listener::class.java)

    @Throws(Exception::class)
    override fun handle(ctx: CommandContext) {
        val args = ctx.args
        val pages = ArrayList<Page>()
        val playerArgs: List<String> = args.subList(1, args.size)
        val player = Overwatch().getPlayerStats(playerArgs)

        buildAchievementEmbeds(player, pages, ctx, args[0])

        ctx.event.channel.sendMessage((pages[0].content as MessageEmbed)).queue { success: Message? -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS, 2) }
        LOGGER.info("Player Achievements Sent For {}!", args)
    }

    private fun buildAchievementEmbeds(player: Player, pages: ArrayList<Page>, ctx: CommandContext, typeOfAchievement: String) {
        when (typeOfAchievement.toLowerCase()) {
            "general" -> {
                player.achievements.general.forEachIndexed { index, achievement ->
                    run {
                        pages.add(Page(PageType.EMBED, getAchievementEmbed(player, achievement, ctx, index, player.achievements.general.size)))
                    }
                }
            }
            "damage" -> {
                player.achievements.damage.forEachIndexed { index, achievement ->
                    run {
                        pages.add(Page(PageType.EMBED, getAchievementEmbed(player, achievement, ctx, index, player.achievements.damage.size)))
                    }
                }
            }
            "map" -> {
                player.achievements.maps.forEachIndexed { index, achievement ->
                    run {
                        pages.add(Page(PageType.EMBED, getAchievementEmbed(player, achievement, ctx, index, player.achievements.maps.size)))
                    }
                }
            }
            "special" -> {
                player.achievements.special.forEachIndexed { index, achievement ->
                    run {
                        pages.add(Page(PageType.EMBED, getAchievementEmbed(player, achievement, ctx, index, player.achievements.special.size)))
                    }
                }
            }
            "support" -> {
                player.achievements.support.forEachIndexed { index, achievement ->
                    run {
                        pages.add(Page(PageType.EMBED, getAchievementEmbed(player, achievement, ctx, index, player.achievements.support.size)))
                    }
                }
            }
            "tank" -> {
                player.achievements.tank.forEachIndexed { index, achievement ->
                    run {
                        pages.add(Page(PageType.EMBED, getAchievementEmbed(player, achievement, ctx, index, player.achievements.tank.size)))
                    }
                }
            }
            else -> throw IllegalStateException("Unexpected Achievement: " + typeOfAchievement.toLowerCase())
        }
    }

    private fun getBaseEmbed(player: Player, ctx: CommandContext, pageNum: Int, totalPages: Int): EmbedBuilder {
        return EmbedBuilder()
                .setAuthor(player.username + " | Level: " + player.level.value + " | Page: " + (pageNum + 1) + "/" + totalPages)
                .setColor(Color.YELLOW)
                .setTimestamp(Date().toInstant())
                .setFooter("Powered By Swagger", ctx.selfUser.avatarUrl)
    }

    private fun getAchievementEmbed(player: Player, general: General, ctx: CommandContext, pageNum: Int, totalPages: Int): MessageEmbed {
        return getBaseEmbed(player, ctx, pageNum, totalPages)
                .setTitle("General Achievements")
                .setThumbnail(general.image)
                .addField(general.title, general.description, true)
                .build()
    }

    private fun getAchievementEmbed(player: Player, damage: Damage, ctx: CommandContext, pageNum: Int, totalPages: Int): MessageEmbed {
        return getBaseEmbed(player, ctx, pageNum, totalPages)
                .setTitle("Damage Achievements")
                .setThumbnail(damage.image)
                .addField(damage.title, damage.description, true)
                .build()
    }

    private fun getAchievementEmbed(player: Player, map: Map, ctx: CommandContext, pageNum: Int, totalPages: Int): MessageEmbed {
        return getBaseEmbed(player, ctx, pageNum, totalPages)
                .setTitle("Map Achievements")
                .setThumbnail(map.image)
                .addField(map.title, map.description, true)
                .build()
    }

    private fun getAchievementEmbed(player: Player, special: Special, ctx: CommandContext, pageNum: Int, totalPages: Int): MessageEmbed {
        return getBaseEmbed(player, ctx, pageNum, totalPages)
                .setTitle("Special Achievements")
                .setThumbnail(special.image)
                .addField(special.title, special.description, true)
                .build()
    }

    private fun getAchievementEmbed(player: Player, support: Support, ctx: CommandContext, pageNum: Int, totalPages: Int): MessageEmbed {
        return getBaseEmbed(player, ctx, pageNum, totalPages)
                .setTitle("Support Achievements")
                .setThumbnail(support.image)
                .addField(support.title, support.description, true)
                .build()
    }

    private fun getAchievementEmbed(player: Player, tank: Tank, ctx: CommandContext, pageNum: Int, totalPages: Int): MessageEmbed {
        return getBaseEmbed(player, ctx, pageNum, totalPages)
                .setTitle("Tank Achievements")
                .setThumbnail(tank.image)
                .addField(tank.title, tank.description, true)
                .build()
    }

    override fun getName(): String {
        return "achievement"
    }

    override fun getHelp(): String {
        return "Returns a list of achievements that the player has"
    }

    override fun getAliases(): List<String> {
        return listOf("achievements", "achieve")
    }
}