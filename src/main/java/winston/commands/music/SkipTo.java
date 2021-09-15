package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.awt.*;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.trackIndexInvalid;

public class SkipTo implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        TrackScheduler scheduler = musicManager.getScheduler();

        if (cantPerformOperation(ctx)) {
            return;
        }

        if (args.isEmpty()) {
            textChannel.sendMessageEmbeds(buildSimpleInfo("Please Specify Track Index To Skip To!", Color.RED)).queue();
        } else {
            if (trackIndexInvalid(scheduler, args, textChannel)) {
                return;
            }
            int index = Integer.parseInt(args.get(0));
            scheduler.skipTo(index - 1);
            scheduler.nextTrack();
        }
    }

    @Override
    public String getName() { return "skipto"; }

    @Override
    public String getHelp() { return "Skip to specific track within the queue"; }

    @Override
    public String getUsage() { return "`skipto <index>`"; }

    @Override
    public List<String> getAliases() { return ICommand.super.getAliases(); }

    @Override
    public String getPackage() { return "Music"; }
}
