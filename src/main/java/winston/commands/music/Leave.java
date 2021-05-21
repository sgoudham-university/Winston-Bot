package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.*;

@SuppressWarnings("ConstantConditions")
public class Leave implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (botNotInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel)) {
            return;
        }

        musicManager.getScheduler().setRepeating(false);
        musicManager.getScheduler().getPlayer().setPaused(false);
        musicManager.getScheduler().getDeque().clear();
        musicManager.getAudioPlayer().stopTrack();

        AudioManager audioManager = ctx.getGuild().getAudioManager();
        VoiceChannel connectedChannel = audioManager.getConnectedChannel();
        audioManager.closeAudioConnection();

        String message = "\uD83D\uDD0A Leaving #" + connectedChannel.getName() + " \uD83D\uDD0A";
        Color colour = Color.YELLOW;
        textChannel.sendMessage(buildSimpleInfo(message, colour)).queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Leaves the connected voice channel";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("disconnect");
    }

    @Override
    public String getPackage() { return "Music"; }
}
