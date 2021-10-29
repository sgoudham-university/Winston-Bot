package me.goudham.winston.bot.command.music.audio.spotify;

import java.util.regex.Pattern;

public class DefaultSpotifyLinkRouter implements SpotifyLinkRouter {
    private static final String SPOTIFY_SEARCH_PREFIX = "sptsearch:";
    private static final String YOUTUBE_SEARCH_PREFIX = "ytsearch:";
    private static final String YOUTUBE_SEARCH_MUSIC_PREFIX = "ytmsearch:";

    private static final String SPOTIFY_TRACK_PATTERN = "[\bhttps://open.\b]*spotify[\b.com\b]*[/:]*track[/:]*[A-Za-z0-9?=_]+";
    private static final String SPOTIFY_PLAYLIST_PATTERN = "[\bhttps://open.\b]*spotify[\b.com\b]*[/:]*playlist[/:]*[A-Za-z0-9?=_]+";
    private static final String SPOTIFY_ALBUM_PATTERN = "[\bhttps://open.\b]*spotify[\b.com\b]*[/:]*album[/:]*[A-Za-z0-9?=_]+";

    private static final String PROTOCOL_REGEX = "(?:http://|https://|)";
    private static final String DOMAIN_REGEX = "(?:www\\.|m\\.|music\\.|)youtube\\.com";
    private static final String SHORT_DOMAIN_REGEX = "(?:www\\.|)youtu\\.be";
    private static final String VIDEO_ID_REGEX = "(?<v>[a-zA-Z0-9_-]{11})";
    private static final String PLAYLIST_ID_REGEX = "(?<list>(PL|LL|FL|UU)[a-zA-Z0-9_-]+)";

    private static final Pattern directVideoIdPattern = Pattern.compile("^" + VIDEO_ID_REGEX + "$");

    private final Extractor[] extractors = new Extractor[]{
            new Extractor(directVideoIdPattern, Routes::track),
            new Extractor(Pattern.compile("^" + PLAYLIST_ID_REGEX + "$"), null),
            new Extractor(Pattern.compile("^" + PROTOCOL_REGEX + DOMAIN_REGEX + "/.*"), null),
            new Extractor(Pattern.compile("^" + PROTOCOL_REGEX + SHORT_DOMAIN_REGEX + "/.*"), null)
    };

    @Override
    public <T> T route(String link, Routes<T> routes) {
        if (link.startsWith(YOUTUBE_SEARCH_PREFIX)) {
            return null;
        } else if (link.startsWith(YOUTUBE_SEARCH_MUSIC_PREFIX)) {
            return null;
        }

        for (Extractor extractor : extractors) {
            if (extractor.pattern.matcher(link).matches()) {
                return null;
            }
        }

        if (link.startsWith(SPOTIFY_SEARCH_PREFIX)) {
            return routes.search(link.substring(SPOTIFY_SEARCH_PREFIX.length()).trim());
        } else if (link.matches(SPOTIFY_TRACK_PATTERN)) {
            String trackId = getSpotifyId(link);
            return routes.track(trackId);
        } else if (link.matches(SPOTIFY_PLAYLIST_PATTERN)) {
            String playlistId = getSpotifyId(link);
            return routes.playlist(playlistId);
        } else if (link.matches(SPOTIFY_ALBUM_PATTERN)) {
            String albumId = getSpotifyId(link);
            return routes.album(albumId);
        }

        return null;
    }

    private String getSpotifyId(String link) {
        String[] split;
        if (link.startsWith("https")) {
            split = link.split("\\?si=")[0].split("/");
        } else {
            split = link.split(":");
        }
        return split[split.length - 1];
    }

    private record Extractor(Pattern pattern, ExtractorRouter router) {

    }

    private interface ExtractorRouter {
        <T> T extract(Routes<T> routes, String url);
    }
}
