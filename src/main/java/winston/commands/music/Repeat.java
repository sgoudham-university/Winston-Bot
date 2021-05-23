package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.util.Collections;
import java.util.List;

import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.noTrackPlaying;

public class Repeat implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (cantPerformOperation(ctx) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        TrackScheduler scheduler = musicManager.getScheduler();
        scheduler.setRepeating(!scheduler.isRepeating());

        textChannel.sendMessage(scheduler.isRepeating() ? "Looping Current Song!" : "Not Looping Current Song Anymore!").queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Loops the current song";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("loop");
    }

    @Override
    public String getPackage() { return "Music"; }
}
