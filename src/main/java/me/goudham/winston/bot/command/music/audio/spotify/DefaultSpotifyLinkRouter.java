package me.goudham.winston.bot.command.music.audio.spotify;

import java.util.regex.Pattern;

public class DefaultSpotifyLinkRouter implements SpotifyLinkRouter {
    private static final String SPOTIFY_SEARCH_PREFIX = "sptsearch:";
    private static final String YOUTUBE_SEARCH_PREFIX = "ytsearch:";
    private static final String YOUTUBE_SEARCH_MUSIC_PREFIX = "ytmsearch:";

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

        String[] split = link.split("\\?si=")[0].split("/");
        String spotifyId = split[split.length - 1];
        if (link.startsWith(SPOTIFY_SEARCH_PREFIX)) {
            return routes.search(link.substring(SPOTIFY_SEARCH_PREFIX.length()).trim());
        } else if (link.contains("playlist")) {
            return routes.playlist(spotifyId);
        } else if (link.contains("track")) {
            return routes.track(spotifyId);
        }

        return null;
    }

    private record Extractor(Pattern pattern, ExtractorRouter router) {

    }

    private interface ExtractorRouter {
        <T> T extract(Routes<T> routes, String url);
    }
}
