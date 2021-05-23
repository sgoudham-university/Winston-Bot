package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Display.displayAlreadyPaused;
import static winston.commands.music.common.Validation.cantPerformOperation;

public class Pause implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (cantPerformOperation(ctx)) {
            return;
        }

        if (audioPlayer.isPaused()) {
            displayAlreadyPaused(ctx, audioPlayer);
        } else if (audioPlayer.getPlayingTrack() != null) {
            audioPlayer.setPaused(true);
        } else {
            textChannel.sendMessage(buildSimpleInfo("No Song To Pause!", Color.RED)).queue();
        }
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp() {
        return "Pauses current song being played";
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
    public String getPackage() { return "Music"; }
}
