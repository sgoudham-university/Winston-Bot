package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.List;

import static winston.commands.music.common.Display.displayResuming;
import static winston.commands.music.util.Common.buildSimpleInfo;
import static winston.commands.music.util.Validation.*;

@SuppressWarnings("ConstantConditions")
public class Resume implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (botNotInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel)) {
            return;
        }

        if (audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
            displayResuming(ctx, audioPlayer);
        } else {
            textChannel.sendMessage(buildSimpleInfo("No Song To Resume!", Color.YELLOW)).queue();
        }
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return "Continues playing the previously paused song";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }
}
