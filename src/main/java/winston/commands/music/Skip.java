package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.noTrackPlaying;

public class Skip implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        TrackScheduler scheduler = musicManager.getScheduler();

        if (cantPerformOperation(ctx) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        scheduler.nextTrack();
        if (audioPlayer.getPlayingTrack() == null) {
            textChannel.sendMessage(buildSimpleInfo("End of Queue!", Color.YELLOW)).queue();
        }
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current track";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("next", "s");
    }

    @Override
    public String getPackage() { return "Music"; }
}
