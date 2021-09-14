package winston.commands.music;

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
import java.util.concurrent.BlockingDeque;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Display.displayRemoved;
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.trackIndexInvalid;

public class Remove implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        TrackScheduler scheduler = musicManager.getScheduler();
        BlockingDeque<AudioTrack> deque = scheduler.getDeque();

        if (cantPerformOperation(ctx)) {
            return;
        }

        if (deque.isEmpty()) {
            textChannel.sendMessageEmbeds(buildSimpleInfo("No Tracks In Queue To Remove!", Color.RED)).queue();
        } else {
            AudioTrack removedTrack;

            if (args.isEmpty()) {
                removedTrack = scheduler.removeTrack();
            } else {
                if (trackIndexInvalid(scheduler, args, textChannel)) {
                    return;
                }
                int index = Integer.parseInt(args.get(0));
                removedTrack = scheduler.removeTrack(index - 1);
            }

            displayRemoved(ctx, removedTrack);
        }
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getHelp() {
        return "Removes a track from the queue (default is the first track within the queue)";
    }

    @Override
    public String getUsage() {
        return "`remove [index]`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("delete");
    }

    @Override
    public String getPackage() {
        return "Music";
    }
}
