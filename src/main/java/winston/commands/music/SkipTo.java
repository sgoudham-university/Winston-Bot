package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.awt.*;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Display.displayNowPlaying;
import static winston.commands.music.common.Validation.*;

@SuppressWarnings("ConstantConditions")
public class SkipTo implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.getScheduler();
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (botNotInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel)) {
            return;
        }

        if (args.isEmpty()) {
            textChannel.sendMessage(buildSimpleInfo("Please Specify Song Index To Skip To!", Color.RED)).queue();
        } else {
            int index;

            try {
                index = Integer.parseInt(args.get(0));
            } catch (NumberFormatException nfe) {
                textChannel.sendMessage(buildSimpleInfo("Please Enter A Valid Number!", Color.RED)).queue();
                return;
            }

            if (index < 1 || index > scheduler.getDeque().size()) {
                textChannel.sendMessage(buildSimpleInfo("Please Enter Index That Is Valid For Current Queue!", Color.RED)).queue();
            } else {
                scheduler.skipTo(index - 1);
                scheduler.nextTrack();
                displayNowPlaying(ctx, audioPlayer);
            }
        }
    }

    @Override
    public String getName() { return "skipto"; }

    @Override
    public String getHelp() { return "Skip to specific song within the queue"; }

    @Override
    public String getUsage() { return "`!skipto <index>`"; }

    @Override
    public List<String> getAliases() { return ICommand.super.getAliases(); }

    @Override
    public String getPackage() { return "Music"; }
}
