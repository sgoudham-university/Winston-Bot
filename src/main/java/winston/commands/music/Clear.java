package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.*;

@SuppressWarnings("ConstantConditions")
public class Clear implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.getScheduler();

        if (botNotInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel)) {
            return;
        }

        scheduler.getPlayer().stopTrack();
        scheduler.getQueue().clear();

        textChannel.sendMessage(buildSimpleInfo("Queue Has Been Cleared ✔", Color.GREEN)).queue();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "Clears the current queue and stops playing the current song";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("stop");
    }

    @Override
    public String getPackage() { return "Music"; }
}
