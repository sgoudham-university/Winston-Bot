package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.util.Collections;
import java.util.List;

import static winston.commands.music.util.Validation.*;

@SuppressWarnings("ConstantConditions")
public class Skip implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        TrackScheduler scheduler = musicManager.getScheduler();

        if (botNotInVoiceChannel(botVoiceState, textChannel) || memberNotInVoiceChannel(authorVoiceState, textChannel)
                || bothPartiesInDiffVoiceChannels(botVoiceState, authorVoiceState, textChannel) || noTrackPlaying(audioPlayer, textChannel)) {
            return;
        }

        scheduler.nextTrack();
        textChannel.sendMessage("Skipped Current Track").queue();

        if (!scheduler.getQueue().isEmpty()) {
            AudioTrack track = audioPlayer.getPlayingTrack();
            AudioTrackInfo trackInfo = track.getInfo();
            String title = trackInfo.title;
            String artist = trackInfo.author;
            String url = trackInfo.uri;

            textChannel.sendMessage("Now Playing `" + title + "` by `" + artist + "` \nLink: <" + url + ">").queue();
        }

    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current song";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("next");
    }
}
