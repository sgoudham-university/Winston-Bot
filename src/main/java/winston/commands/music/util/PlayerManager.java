package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static winston.commands.music.common.Display.displayAddedToQueue;

public class PlayerManager {
    private static PlayerManager instance;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private PlayerManager() {
        musicManagers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerLocalSource(audioPlayerManager);
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
    }

    public static PlayerManager getInstance() {
        return instance == null ? instance = new PlayerManager() : instance;
    }

    public void loadAndPlay(CommandContext ctx, String trackUrl, boolean isLocalFile) {
        TextChannel textChannel = ctx.getChannel();
        GuildMusicManager musicManager = getMusicManager(textChannel.getGuild());
        TrackScheduler scheduler = musicManager.getScheduler();

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                scheduler.queue(audioTrack, true);
                if (!isLocalFile) {
                    displayAddedToQueue(ctx, audioTrack);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (audioPlaylist.isSearchResult()) {
                    trackLoaded(audioPlaylist.getTracks().get(0));
                } else {
                    List<AudioTrack> allTracks = audioPlaylist.getTracks();

                    textChannel.sendMessage("Added `")
                            .append(String.valueOf(allTracks.size()))
                            .append("` Tracks From Playlist `")
                            .append(audioPlaylist.getName())
                            .append("`")
                            .queue();

                    allTracks.forEach(track -> scheduler.queue(track, false));
                }
            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("Nothing Found For: '" + String.join(" ", ctx.getArgs()) + "'").queue();
            }

            @Override
            public void loadFailed(FriendlyException fde) {
                textChannel.sendMessage("Could Not Play: " + fde.getMessage()).queue();
            }
        });
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildID) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }
}
