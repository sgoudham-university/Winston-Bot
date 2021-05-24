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
import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.*;

public class Rewind implements ICommand {

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

        if (args.isEmpty()) {
            textChannel.sendMessage(buildSimpleInfo("Please Specify (In Seconds) How Much You Want To Rewind By", Color.RED)).queue();
        } else {
            String rewindPosition = args.get(0);
            if (numberFormatInvalid(rewindPosition, textChannel)) {
                return;
            }

            long rewindPositionMilliseconds = convertToMilliseconds(Integer.parseInt(rewindPosition));
            if (rewindPositionInvalid(playingTrack, rewindPositionMilliseconds, textChannel)) {
                return;
            }

            long currentPosition = playingTrack.getPosition();
            long newPosition = currentPosition - rewindPositionMilliseconds;
            playingTrack.setPosition(newPosition);
        }
    }

    @Override
    public String getName() {
        return "rewind";
    }

    @Override
    public String getHelp() {
        return "Rewinds the track by a certain amount given (in seconds)";
    }

    @Override
    public String getUsage() {
        return "`rewind <index>`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("back");
    }

    @Override
    public String getPackage() {
        return "Music";
    }
}
