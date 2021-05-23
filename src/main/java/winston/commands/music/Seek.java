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
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Display.displayNowPlaying;
import static winston.commands.music.common.Validation.*;

public class Seek implements ICommand {

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
                textChannel.sendMessage(buildSimpleInfo("Please Specify Position (In Seconds) To Seek To!", Color.RED)).queue();
            } else {
                if (seekPositionInvalid(playingTrack, args, textChannel)) {
                    return;
                }

                String seekPos = args.get(0);
                long seekPosMill = Long.parseLong(seekPos) * 1000;

                playingTrack.setPosition(seekPosMill);
                displayNowPlaying(ctx, audioPlayer);
            }
        } else {
            textChannel.sendMessage(buildSimpleInfo("Cannot Seek On This Track!", Color.RED)).queue();
        }

    }

    @Override
    public String getName() {
        return "seek";
    }

    @Override
    public String getHelp() {
        return "Seek to a specific position (seconds) within the current track";
    }

    @Override
    public String getUsage() {
        return "`seek <position>`";
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }

    @Override
    public String getPackage() {
        return "Music";
    }
}
