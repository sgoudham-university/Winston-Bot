package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.util.List;

import static winston.commands.music.common.Display.displayRestarting;
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.noTrackPlaying;

public class Restart implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (cantPerformOperation(ctx) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        playingTrack.setPosition(0);
        displayRestarting(ctx, audioPlayer);
    }

    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public String getHelp() {
        return "Plays the current track from the beginning";
    }

    @Override
    public String getUsage() {
        return null;
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
