package me.goudham.winston.bot.command.music.audio.spotify;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeHttpContextFilter;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchMusicProvider;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchMusicResultLoader;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchResultLoader;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSignatureCipherManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSignatureResolver;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeTrackDetailsLoader;
import com.sedmelluq.discord.lavaplayer.tools.ExceptionTools;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.http.ExtendedHttpConfigurable;
import com.sedmelluq.discord.lavaplayer.tools.http.MultiHttpConfigurable;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpConfigurable;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterface;
import com.sedmelluq.discord.lavaplayer.tools.io.HttpInterfaceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.BasicAudioPlaylist;
import jakarta.inject.Inject;
import me.goudham.winston.domain.music.Album;
import me.goudham.winston.domain.music.Playlist;
import me.goudham.winston.domain.music.Track;
import me.goudham.winston.domain.music.TrackMetaData;
import me.goudham.winston.service.SpotifyService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpotifyAudioSourceManager implements AudioSourceManager, HttpConfigurable {
    private static final Logger log = LoggerFactory.getLogger(SpotifyAudioSourceManager.class);

    private final YoutubeSignatureResolver signatureResolver;
    private final HttpInterfaceManager httpInterfaceManager;
    private final ExtendedHttpConfigurable combinedHttpConfiguration;
    private final boolean allowSearch;
    private final YoutubeTrackDetailsLoader trackDetailsLoader;
    private final YoutubeSearchResultLoader searchResultLoader;
    private final SpotifyLinkRouter linkRouter;
    private final LoadingRoutes loadingRoutes;
    private final SpotifyService spotifyService;

    @Inject
    public SpotifyAudioSourceManager(SpotifyService spotifyService) {
        this(true, spotifyService);
    }

    @Inject
    public SpotifyAudioSourceManager(boolean allowSearch, SpotifyService spotifyService) {
        this(
                allowSearch,
                new DefaultSpotifyTrackDetailsLoader(),
                new YoutubeSearchProvider(),
                new YoutubeSearchMusicProvider(),
                new YoutubeSignatureCipherManager(),
                new DefaultSpotifyLinkRouter(),
                spotifyService);
    }

    @Inject
    private SpotifyAudioSourceManager(
            boolean allowSearch,
            YoutubeTrackDetailsLoader trackDetailsLoader,
            YoutubeSearchResultLoader searchResultLoader,
            YoutubeSearchMusicResultLoader searchMusicResultLoader,
            YoutubeSignatureResolver signatureResolver,
            SpotifyLinkRouter linkRouter,
            SpotifyService spotifyService) {
        httpInterfaceManager = HttpClientTools.createDefaultThreadLocalManager();
        httpInterfaceManager.setHttpContextFilter(new YoutubeHttpContextFilter());

        this.allowSearch = allowSearch;
        this.trackDetailsLoader = trackDetailsLoader;
        this.signatureResolver = signatureResolver;
        this.searchResultLoader = searchResultLoader;
        this.linkRouter = linkRouter;
        this.spotifyService = spotifyService;
        loadingRoutes = new LoadingRoutes();

        combinedHttpConfiguration = new MultiHttpConfigurable(Arrays.asList(
                httpInterfaceManager,
                searchResultLoader.getHttpConfiguration(),
                searchMusicResultLoader.getHttpConfiguration()
        ));
    }

    YoutubeTrackDetailsLoader getTrackDetailsLoader() {
        return trackDetailsLoader;
    }

    YoutubeSignatureResolver getSignatureResolver() {
        return signatureResolver;
    }

    @Override
    public String getSourceName() {
        return "spotify";
    }

    @Override
    public AudioItem loadItem(AudioPlayerManager manager, AudioReference reference) {
        try {
            return loadItemOnce(reference);
        } catch (FriendlyException exception) {
            // In case of a connection reset exception, try once more.
            if (HttpClientTools.isRetriableNetworkException(exception.getCause())) {
                return loadItemOnce(reference);
            } else {
                throw exception;
            }
        }
    }

    @Override
    public boolean isTrackEncodable(AudioTrack track) {
        return true;
    }

    @Override
    public void encodeTrack(AudioTrack track, DataOutput output) {
        // No custom values that need saving
    }

    @Override
    public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) {
        return new SpotifyAudioTrack(trackInfo, this);
    }

    @Override
    public void shutdown() {
        ExceptionTools.closeWithWarnings(httpInterfaceManager);
    }

    /**
     * @return Get an HTTP interface for a playing track.
     */
    HttpInterface getHttpInterface() {
        return httpInterfaceManager.getInterface();
    }

    @Override
    public void configureRequests(Function<RequestConfig, RequestConfig> configurator) {
        combinedHttpConfiguration.configureRequests(configurator);
    }

    @Override
    public void configureBuilder(Consumer<HttpClientBuilder> configurator) {
        combinedHttpConfiguration.configureBuilder(configurator);
    }

    private AudioItem loadItemOnce(AudioReference reference) {
        return linkRouter.route(reference.identifier, loadingRoutes);
    }

    private SpotifyAudioTrack buildTrackFromInfo(AudioTrackInfo info) {
        return new SpotifyAudioTrack(info, this);
    }

    private class LoadingRoutes implements SpotifyLinkRouter.Routes<AudioItem> {

        @Override
        public AudioItem track(String trackId) {
            Track track = spotifyService.getTrack(trackId);
            AudioTrack audioTrack = createAudioTrack(track);
            return populateFirstTrack(audioTrack);
        }

        @Override
        public AudioItem playlist(String playlistId) {
            log.debug("Starting to load playlist with ID {}", playlistId);

            Playlist playlist = spotifyService.getPlaylist(playlistId);
            List<AudioTrack> audioTracks = playlist.tracks().stream()
                    .map(this::createAudioTrack)
                    .collect(Collectors.toList());

            AudioTrack audioTrack = populateFirstTrack(audioTracks.get(0));
            audioTracks.set(0, audioTrack);
            return new BasicAudioPlaylist(playlist.name(), audioTracks, null, false);
        }

        @Override
        public AudioItem album(String albumId) {
            Album album = spotifyService.getAlbum(albumId);
            List<AudioTrack> audioTracks = album.tracks().stream()
                    .map(this::createAudioTrack)
                    .collect(Collectors.toList());

            AudioTrack audioTrack = populateFirstTrack(audioTracks.get(0));
            audioTracks.set(0, audioTrack);
            return new SpotifyAlbum(album.name(), album.artists(), audioTracks, null, false);
        }

        private AudioTrack createAudioTrack(Track track) {
            // You don't really need to return audioTrack here but did so for improved readability
            AudioTrackInfo audioTrackInfo = new AudioTrackInfo(track.artists() + " - " + track.name(), track.artists(), 0L, "", false, track.uri());
            AudioTrack audioTrack = new SpotifyAudioTrack(audioTrackInfo, SpotifyAudioSourceManager.this);
            audioTrack.setUserData(new TrackMetaData(track.name(), track.artists(), track.uri(), track.image(), audioTrack, SpotifyAudioSourceManager.this));
            return audioTrack;
        }

        private AudioTrack populateFirstTrack(AudioTrack audioTrack) {
            TrackMetaData userData = audioTrack.getUserData(TrackMetaData.class);

            AudioPlaylist search = (AudioPlaylist) search(audioTrack.getInfo().title);
            AudioTrack firstAudioTrack = search.getTracks().get(0);
            firstAudioTrack.setUserData(userData);

            return firstAudioTrack;
        }

        @Override
        public AudioItem search(String query) {
            if (allowSearch) {
                return searchResultLoader.loadSearchResult(
                        query,
                        SpotifyAudioSourceManager.this::buildTrackFromInfo
                );
            }

            return null;
        }
    }
}
