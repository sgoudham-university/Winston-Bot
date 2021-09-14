package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.cantPerformOperation;

public class Clear implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer player = musicManager.getScheduler().getPlayer();
        BlockingDeque<AudioTrack> deque = musicManager.getScheduler().getDeque();

        if (cantPerformOperation(ctx)) {
            return;
        }

        player.stopTrack();
        deque.clear();

        textChannel.sendMessageEmbeds(buildSimpleInfo("Queue Has Been Cleared ✔", Color.GREEN)).queue();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "Clears the current queue and stops playing the current track";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("stop");
    }

    @Override
    public String getPackage() { return "Music"; }
}
