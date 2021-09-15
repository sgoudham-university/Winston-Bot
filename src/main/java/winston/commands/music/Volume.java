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
import static winston.commands.music.common.Validation.cantPerformOperation;
import static winston.commands.music.common.Validation.numberFormatInvalid;

public class Volume implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (cantPerformOperation(ctx)) {
            return;
        }

        if (args.isEmpty()) {
            textChannel.sendMessageEmbeds(buildSimpleInfo("Usage -> " + getUsage(), Color.YELLOW)).queue();
            return;
        }

        String newVolume = args.get(0);
        if (numberFormatInvalid(newVolume, textChannel)) {
            return;
        }

        int oldVolume = audioPlayer.getVolume();
        audioPlayer.setVolume(Integer.parseInt(newVolume));
        textChannel.sendMessage("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`").queue();
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getHelp() {
        return "Sets the volume level of the Bot";
    }

    @Override
    public String getUsage() {
        return "`volume <level>`";
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
