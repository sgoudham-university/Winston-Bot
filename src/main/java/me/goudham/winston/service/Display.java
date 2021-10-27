package me.goudham.winston.service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.domain.music.TrackMetaData;
import me.goudham.winston.service.util.TimeUtils;
import me.goudham.winston.service.util.TitleUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class Display {
    private final TimeUtils timeUtils;
    private final TitleUtils titleUtils;
    private final EmbedService embedService;

    @Inject
    public Display(TimeUtils timeUtils, TitleUtils titleUtils, EmbedService embedService) {
        this.timeUtils = timeUtils;
        this.titleUtils = titleUtils;
        this.embedService = embedService;
    }

    public void displayAddedToQueue(SlashCommandEvent slashCommandEvent, AudioTrack track, boolean isSlashCommand) {
        String duration = timeUtils.formatTime(track.getDuration());

        String embedAuthor = "Added To Queue";
        String embedDesc = "**[" + duration + "s]**";

        displaySong(slashCommandEvent, track, embedAuthor, embedDesc, isSlashCommand);
    }

    public AtomicReference<Long> displayNowPlaying(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        return mergeSongInfo(slashCommandEvent, audioPlayer, "Now Playing", isSlashCommand);
    }

    public void displayResuming(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        mergeSongInfo(slashCommandEvent, audioPlayer, "Resuming", isSlashCommand);
    }

    public void displayAlreadyPaused(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        mergeSongInfo(slashCommandEvent, audioPlayer, "Already Paused", isSlashCommand);
    }

    public void displayPausing(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        mergeSongInfo(slashCommandEvent, audioPlayer, "Pausing", isSlashCommand);
    }

    public void displayFastForwarding(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        mergeSongInfo(slashCommandEvent, audioPlayer, "Fast Forwarding", isSlashCommand);
    }

    public void displayRewinding(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        mergeSongInfo(slashCommandEvent, audioPlayer, "Rewinding", isSlashCommand);
    }

    public void displayRestarting(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, boolean isSlashCommand) {
        mergeSongInfo(slashCommandEvent, audioPlayer, "Restarting", isSlashCommand);
    }

    public void displayRemoved(SlashCommandEvent event, AudioTrack removedTrack, boolean isSlashCommand) {
        mergeSongInfo(event, removedTrack, isSlashCommand);
    }

    private AtomicReference<Long> mergeSongInfo(SlashCommandEvent slashCommandEvent, AudioPlayer audioPlayer, String status, boolean isSlashCommand) {
        AudioTrack track = audioPlayer.getPlayingTrack();
        String position = timeUtils.formatTime(track.getPosition());
        String duration = timeUtils.formatTime(track.getDuration());

        String embedAuthor = status + "   |   [" + position + "s / " + duration + "s]";
        String embedDesc = getProgressBar(audioPlayer, track);

        return displaySong(slashCommandEvent, track, embedAuthor, embedDesc, isSlashCommand);
    }

    private void mergeSongInfo(SlashCommandEvent event, AudioTrack removedTrack, boolean isSlashCommand) {
        String position = timeUtils.formatTime(removedTrack.getPosition());
        String duration = timeUtils.formatTime(removedTrack.getDuration());

        String trackPos = "**[" + position + "s / " + duration + "s]**";

        displaySong(event, removedTrack, "Removed", trackPos, isSlashCommand);
    }

    private AtomicReference<Long> displaySong(SlashCommandEvent slashCommandEvent, AudioTrack track, String status, String trackPos, boolean isSlashCommand) {
        AudioTrackInfo trackInfo = track.getInfo();
        TrackMetaData userData = track.getUserData(TrackMetaData.class);
        String title = titleUtils.getTrimmedTitle(trackInfo.title, 70);
        String url = userData == null ? trackInfo.uri : userData.uri();
        String image = userData == null ? "" : userData.image();
        MessageEmbed nowPlayingEmbed = embedService.getNowPlayingEmbed(slashCommandEvent, title, url, image, status, trackPos);

        AtomicReference<Long> messageID = new AtomicReference<>();
        if (isSlashCommand) {
            if (!slashCommandEvent.isAcknowledged()) {
                slashCommandEvent.replyEmbeds(nowPlayingEmbed).queue();
            }
        } else {
            MessageChannel textChannel = slashCommandEvent.getChannel();
            textChannel.sendMessageEmbeds(nowPlayingEmbed).queue(message -> messageID.set(message.getIdLong()));
        }
        return messageID;
    }

    private String getProgressBar(AudioPlayer audioPlayer, AudioTrack track) {
        float percentage = (100f / track.getDuration() * track.getPosition());
        int barLength = (int) Math.round((double) percentage / 3.33);
        String playButton = "` ▶️ ";
        String trackLine = "⎯";
        String circle = audioPlayer.isPaused() ? "\uD83D\uDD35" : "\uD83D\uDD34";

        return playButton
                + trackLine.repeat(barLength)
                + circle
                + trackLine.repeat(30 - barLength)
                + "  "
                + String.format("%.2f%%", percentage)
                + "`";
    }
}
