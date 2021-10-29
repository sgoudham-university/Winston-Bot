package me.goudham.winston.domain.music;

import java.util.List;

public record Album(String artists, String name, String image, List<Track> tracks) {
}
