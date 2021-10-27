package me.goudham.winston.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.service.util.TimeUtils;
import me.goudham.winston.service.util.TitleUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.Color;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@Singleton
public class EmbedServiceImpl implements EmbedService {
    private final Random random = new Random();
    private final String youtubeLogo;
    private final String spotifyLogo;
    private final TimeUtils timeUtils;
    private final TitleUtils titleUtils;

    @Inject
    public EmbedServiceImpl(
            @Value("${youtubeLogo}") String youtubeLogo,
            @Value("${spotifyLogo}") String spotifyLogo,
            TimeUtils timeUtils,
            TitleUtils titleUtils
    ) {
        this.youtubeLogo = youtubeLogo;
        this.spotifyLogo = spotifyLogo;
        this.timeUtils = timeUtils;
        this.titleUtils = titleUtils;
    }

    @Override
    public EmbedBuilder getBaseEmbed() {
        return new EmbedBuilder()
                .setColor(getRandomColour())
                .setTimestamp(Instant.now())
                .setFooter("Feeling powerful!");
    }

    @Override
    public EmbedBuilder getBaseEmbed(SlashCommandEvent slashCommandEvent, String status) {
        User author = slashCommandEvent.getUser();
        return new EmbedBuilder()
                .setAuthor(status, null, youtubeLogo)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
    }

    @Override
    public EmbedBuilder getBaseEmbedWithPageCounter(User author, SlashCommandEvent slashCommandEvent, int currPage, int totalPages) {
        return new EmbedBuilder()
                .setAuthor("Page " + currPage + " / " + totalPages)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), slashCommandEvent.getUser().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
    }

    @Override
    public EmbedBuilder getUserEmbed(Member member) {
        Color userColour = member.getColor() == null ? getRandomColour() : member.getColor();
        return getBaseEmbed().setColor(userColour);
    }

    @Override
    public EmbedBuilder getQueueEmbed(User author, SlashCommandEvent slashCommandEvent, int currPage, int totalPages) {
        return getBaseEmbedWithPageCounter(author, slashCommandEvent, currPage, totalPages)
                .setThumbnail(null)
                .setTitle("Current Tracks in Queue")
                .setColor(Color.BLUE);
    }

    @Override
    public MessageEmbed getSearchEmbed(SlashCommandEvent slashCommandEvent, List<AudioTrack> searchResults, int numberOfTracksReturned) {
        EmbedBuilder searchEmbed = getBaseEmbed(slashCommandEvent, "Search Results");
        searchEmbed.setThumbnail(null);
        searchEmbed.setTitle(numberOfTracksReturned + " Results Found  |  :exclamation: Type 'exit' To Exit");
        searchEmbed.setColor(Color.red);

        for (int i = 0; i < numberOfTracksReturned; i++) {
            AudioTrack track = searchResults.get(i);
            AudioTrackInfo trackInfo = track.getInfo();
            String trackIndex = "**" + (i + 1) + ")**  ";
            String duration = "`[" + timeUtils.formatTime(track.getDuration()) + "]`  ";
            String title = titleUtils.getTrimmedTitle(trackInfo.title, 40);

            searchEmbed.appendDescription(trackIndex + duration + title + "\n");
        }

        return searchEmbed.build();
    }

    @Override
    public MessageEmbed getNowPlayingEmbed(SlashCommandEvent slashCommandEvent, String title, String url, String image, String status, String trackPos) {
        String thumbnailUrl;
        String iconUrl;

        // This check doubles as a isSpotifyLink check (have fun future Goudham)
        if (!image.isBlank()) {
            thumbnailUrl = image;
            iconUrl = spotifyLogo;
        } else {
            URI uri = URI.create(url);
            String videoID = uri.getQuery().split("v=")[1];
            thumbnailUrl = "https://img.youtube.com/vi/" + videoID + "/0.jpg";
            iconUrl = youtubeLogo;
        }

        return getBaseEmbed(slashCommandEvent, status)
                .setAuthor(status, null, iconUrl)
                .setTitle(title, url)
                .setDescription(trackPos)
                .setThumbnail(thumbnailUrl)
                .setColor(Color.RED)
                .build();
    }

    @Override
    public MessageEmbed getSimpleInfoEmbed(String message, Color colour) {
        return new EmbedBuilder()
                .setAuthor(message)
                .setColor(colour)
                .build();
    }

    @Override
    public MessageEmbed getSimpleInfoEmbedWithDesc(String message, Color colour) {
        return new EmbedBuilder()
                .setDescription(message)
                .setColor(colour)
                .build();
    }

    private Color getRandomColour() {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }
}
