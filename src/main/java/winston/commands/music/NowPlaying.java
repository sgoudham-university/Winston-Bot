package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Display.displayNowPlaying;
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.noTrackPlaying;

public class NowPlaying implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (cantPerformOperation(ctx) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        displayNowPlaying(ctx, audioPlayer);
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

    @Override
    public String getPackage() { return "Music"; }
}
