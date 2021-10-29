package me.goudham.winston.bot.command.music.audio.spotify;

public interface SpotifyLinkRouter {
    <T> T route(String link, Routes<T> routes);

    interface Routes<T> {
        T track(String trackId);
        T playlist(String playlistId);
        T album(String albumId);
        T search(String query);
    }
}
