package me.goudham.winston.bot.command.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.bot.command.music.audio.spotify.SpotifyAlbum;
import me.goudham.winston.bot.command.music.audio.spotify.SpotifyAudioSourceManager;
import me.goudham.winston.domain.music.TrackMetaData;
import me.goudham.winston.domain.music.TrackUser;
import me.goudham.winston.service.Display;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.SpotifyService;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class PlayerManager {
    private final Display display;
    private final EmbedService embedService;
    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    @Inject
    public PlayerManager(Display display, EmbedService embedService, SpotifyService spotifyService) {
        this.display = display;
        this.embedService = embedService;
        playerManager.registerSourceManager(new SpotifyAudioSourceManager(true, spotifyService));
        playerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
    }

    public void searchAndPlay(SlashCommandEvent slashCommandEvent, String userSearch) {
        MessageChannel textChannel = slashCommandEvent.getChannel();
        GuildMusicManager musicManager = getMusicManager(slashCommandEvent);
        TrackScheduler scheduler = musicManager.getTrackScheduler();

        playerManager.loadItemOrdered(musicManager, userSearch, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                display.displayAddedToQueue(slashCommandEvent, audioTrack, false);
                scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                List<AudioTrack> searchResults = audioPlaylist.getTracks();
                int numberOfTracksToLoad = Math.min(searchResults.size(), 10);
                MessageEmbed searchEmbed = embedService.getSearchEmbed(slashCommandEvent, searchResults, numberOfTracksToLoad);

//                textChannel.sendMessage(searchEmbed).queue((message -> eventWaiter.waitForEvent(
//                        GuildMessageReceivedEvent.class,
//                        (event) -> event.getAuthor() == ctx.getAuthor(),
//                        (event) -> {
//                            String userSearch = event.getMessage().getContentRaw();
//
//                            if (userSearch.equalsIgnoreCase("exit")) {
//                                textChannel.deleteMessageById(message.getId()).and(textChannel.deleteMessageById(event.getMessageId())).submit();
//                                return;
//                            }
//
//                            if (numberFormatInvalid(userSearch, textChannel)) {
//                                return;
//                            }
//
//                            int searchIndex = Integer.parseInt(userSearch);
//                            if (searchInvalid(searchIndex, searchResults, textChannel)) {
//                                return;
//                            }
//
//                            trackLoaded(searchResults.get(searchIndex - 1));
//                        }
//                )));
            }

            @Override
            public void noMatches() {
                OptionMapping trackOption = slashCommandEvent.getOption("input");
                String track = trackOption == null ? "Track Unknown" : trackOption.getAsString();
                textChannel.sendMessage("Nothing Found For: '" + track + "'").queue();
            }

            @Override
            public void loadFailed(FriendlyException fde) {
                textChannel.sendMessage("Could Not Play: " + fde.getMessage()).queue();
            }
        });
    }

    public void loadAndPlay(SlashCommandEvent slashCommandEvent, String trackUrl, TrackUser trackUser, boolean isSlashCommand, boolean shuffle) {
        MessageChannel textChannel = slashCommandEvent.getChannel();
        GuildMusicManager musicManager = getMusicManager(slashCommandEvent);
        TrackScheduler scheduler = musicManager.getTrackScheduler();

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                setTrackUser(audioTrack, trackUser);
                display.displayAddedToQueue(slashCommandEvent, audioTrack, isSlashCommand);
                scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (audioPlaylist.isSearchResult()) {
                    trackLoaded(audioPlaylist.getTracks().get(0));
                } else {
                    List<AudioTrack> allTracks = audioPlaylist.getTracks();
                    if (shuffle) {
                        Collections.shuffle(allTracks);
                    }

                    String message;
                    if (audioPlaylist instanceof SpotifyAlbum spotifyAlbum) {
                        message = "**Added** `" + allTracks.size() + "` **Tracks From Album** `" + spotifyAlbum.getName() + "` **By** `" + spotifyAlbum.getArtists() + "`";
                    } else {
                        message = "**Added** `" + allTracks.size() + "` **Tracks From Playlist** `" + audioPlaylist.getName() + "`";
                    }

                    MessageEmbed simpleInfoEmbed = embedService.getSimpleInfoEmbedWithDesc(message, Color.GREEN);
                    if (isSlashCommand) {
                        slashCommandEvent.replyEmbeds(simpleInfoEmbed).queue();
                    } else {
                        textChannel.sendMessageEmbeds(simpleInfoEmbed).queue();
                    }

                    allTracks.forEach(audioTrack -> {
                        setTrackUser(audioTrack, trackUser);
                        scheduler.queue(audioTrack);
                    });
                }
            }

            @Override
            public void noMatches() {
                OptionMapping trackOption = slashCommandEvent.getOption("input");
                String track = trackOption == null ? "Track Unknown" : trackOption.getAsString();
                textChannel.sendMessage("Nothing Found For: '" + track + "'").queue();
            }

            @Override
            public void loadFailed(FriendlyException fe) {
                textChannel.sendMessage("Could Not Play: " + fe.getMessage()).queue();
            }
        });
    }

    private void setTrackUser(AudioTrack audioTrack, TrackUser trackUser) {
        TrackMetaData userData = audioTrack.getUserData(TrackMetaData.class);
        if (userData == null) {
            userData = new TrackMetaData("", "", "", "", null, null);
            audioTrack.setUserData(userData);
        }
        userData.setTrackUser(trackUser);
    }

    public GuildMusicManager getMusicManager(SlashCommandEvent slashCommandEvent) {
        GuildMusicManager guildMusicManager = musicManagers.get(slashCommandEvent.getGuild().getIdLong());
        if (guildMusicManager == null) {
            guildMusicManager = new GuildMusicManager(playerManager, slashCommandEvent, display);
            slashCommandEvent.getGuild().getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
        }
        guildMusicManager.getTrackScheduler().setEvent(slashCommandEvent);
        musicManagers.put(slashCommandEvent.getGuild().getIdLong(), guildMusicManager);
        return guildMusicManager;
    }
}
