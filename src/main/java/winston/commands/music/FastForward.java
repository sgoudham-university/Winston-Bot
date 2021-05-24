package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Display.displayFastForwarding;
import static winston.commands.music.common.Validation.*;

public class FastForward implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        TrackScheduler scheduler = musicManager.getScheduler();
        AudioTrack playingTrack = scheduler.getPlayer().getPlayingTrack();

        if (cantPerformOperation(ctx) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        if (playingTrack.isSeekable()) {
            if (args.isEmpty()) {
                textChannel.sendMessage(buildSimpleInfo("Please Specify (In Seconds) How Much You Want To Fast Forward By", Color.RED)).queue();
            } else {
                String fastForwardPosition = args.get(0);
                if (numberFormatInvalid(fastForwardPosition, textChannel)) {
                    return;
                }

                long fastForwardPositionMilliseconds = convertToMilliseconds(Integer.parseInt(fastForwardPosition));
                if (fastForwardInvalid(playingTrack, fastForwardPositionMilliseconds, textChannel)) {
                    return;
                }

                playingTrack.setPosition(playingTrack.getPosition() + fastForwardPositionMilliseconds);
                displayFastForwarding(ctx, audioPlayer);
            }
        } else {
            textChannel.sendMessage(buildSimpleInfo("Cannot Fast Forward On This Track!", Color.RED)).queue();
        }
    }

    @Override
    public String getName() {
        return "fastforward";
    }

    @Override
    public String getHelp() {
        return "Fast forward specific amount (seconds) within the current track";
    }

    @Override
    public String getUsage() {
        return "`fastforward <position>`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fw", "ff", "forward");
    }

    @Override
    public String getPackage() {
        return "Music";
    }
}
