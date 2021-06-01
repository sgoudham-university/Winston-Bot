package winston.commands.music.util;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static winston.commands.music.common.Common.buildSimpleInfo;
import static winston.commands.music.common.Display.buildSearchEmbed;
import static winston.commands.music.common.Display.displayAddedToQueue;
import static winston.commands.music.common.Validation.numberFormatInvalid;

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

    public void searchAndPlay(CommandContext ctx, EventWaiter eventWaiter, String userSearch) {
        TextChannel textChannel = ctx.getChannel();
        updateTrackScheduler(ctx);
        GuildMusicManager musicManager = getMusicManager(ctx);
        TrackScheduler scheduler = musicManager.getScheduler();

        audioPlayerManager.loadItemOrdered(musicManager, userSearch, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                displayAddedToQueue(ctx, audioTrack);
                scheduler.queue(audioTrack, false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                int numberOfTracksToLoad = Math.min(audioPlaylist.getTracks().size(), 10);
                MessageEmbed searchEmbed = buildSearchEmbed(ctx, audioPlaylist.getTracks(), numberOfTracksToLoad);

                textChannel.sendMessage(searchEmbed).queue((message -> eventWaiter.waitForEvent(
                        GuildMessageReceivedEvent.class,
                        (event) -> event.getAuthor() == ctx.getAuthor(),
                        (event) -> {
                            String userSearch = event.getMessage().getContentRaw();

                            if (userSearch.equalsIgnoreCase("exit")) {
                                textChannel.sendMessage(buildSimpleInfo("Exited", Color.GREEN)).queue();
                            }

                            if (numberFormatInvalid(userSearch, textChannel)) {
                                return;
                            }

                            int trackIndexInt = Integer.parseInt(userSearch);
                            trackLoaded(audioPlaylist.getTracks().get(trackIndexInt - 1));
                        }
                )));
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

    public void loadAndPlay(CommandContext ctx, String trackUrl, boolean isLocalFile) {
        TextChannel textChannel = ctx.getChannel();
        updateTrackScheduler(ctx);
        GuildMusicManager musicManager = getMusicManager(ctx);
        TrackScheduler scheduler = musicManager.getScheduler();

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                if (!isLocalFile) {
                    displayAddedToQueue(ctx, audioTrack);
                }
                scheduler.queue(audioTrack, isLocalFile);
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

    public GuildMusicManager getMusicManager(CommandContext ctx) {
        return musicManagers.computeIfAbsent(ctx.getGuild().getIdLong(), (guildID) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager, ctx);
            ctx.getGuild().getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    private void updateTrackScheduler(CommandContext newCtx) {
        musicManagers.computeIfPresent(newCtx.getGuild().getIdLong(), (guildId, ctx) -> {
            GuildMusicManager guildMusicManager = musicManagers.get(guildId);
            guildMusicManager.getScheduler().setCtx(newCtx);
            return guildMusicManager;
        });
    }
}
