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
import java.util.Collections;
import java.util.List;

import static winston.commands.music.util.Common.buildSimpleInfo;
import static winston.commands.music.util.Common.displayNowPlaying;
import static winston.commands.music.util.Validation.*;

@SuppressWarnings("ConstantConditions")
public class NowPlaying implements ICommand {

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
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        if (!audioPlayer.getPlayingTrack().getInfo().title.equals("Unknown title")) {
            displayNowPlaying(ctx, audioPlayer);
        } else {
            textChannel.sendMessage(buildSimpleInfo("Winston Voice Line Currently Playing", Color.BLUE)).queue();
        }

    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "Shows the current song being played";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("np");
    }
}
