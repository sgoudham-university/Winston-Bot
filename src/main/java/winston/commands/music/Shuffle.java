package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.awt.*;
import java.util.List;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Validation.cantPerformOperation;

public class Shuffle implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);

        if (cantPerformOperation(ctx)) {
            return;
        }

        musicManager.getScheduler().shuffle();
        textChannel.sendMessageEmbeds(buildSimpleInfo("Queue Shuffled! ✔", Color.GREEN)).queue();
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getHelp() {
        return "Shuffles the current queue";
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
