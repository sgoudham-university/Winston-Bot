package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static winston.commands.music.util.Common.joinVoiceChannel;
import static winston.commands.music.util.Validation.memberNotInVoiceChannel;

@SuppressWarnings("ConstantConditions")
public class Play implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (memberNotInVoiceChannel(authorVoiceState, textChannel)) {
            return;
        }

        if (!botVoiceState.inVoiceChannel()) {
            joinVoiceChannel(ctx);
        }

        if (ctx.getArgs().isEmpty()) {
            if (audioPlayer.isPaused()) {
                audioPlayer.setPaused(false);

                String title = audioPlayer.getPlayingTrack().getInfo().title;
                String artist = audioPlayer.getPlayingTrack().getInfo().author;
                textChannel.sendMessage("Resuming `" + title + "` by `" + artist + "`").queue();

                return;
            } else {
                textChannel.sendMessage("Usage Is: " + getUsage()).queue();
            }
        }

        String link = String.join(" ", ctx.getArgs());
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(textChannel, link);
    }

    private boolean isUrl(String link) {
        try {
            new URL(link).toURI();
        } catch (URISyntaxException | MalformedURLException exp) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays the given song";
    }

    @Override
    public String getUsage() {
        return "`!play <link | text>`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("p");
    }
}
