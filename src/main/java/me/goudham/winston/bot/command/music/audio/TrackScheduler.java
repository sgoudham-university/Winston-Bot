package me.goudham.winston.bot.command.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;
import me.goudham.winston.service.Display;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackScheduler extends AudioEventAdapter {
    private SlashCommandEvent event;
    private final AudioPlayer player;
    private final Display display;
    private final BlockingDeque<AudioTrack> deque = new LinkedBlockingDeque<>();
    private AtomicReference<Long> previousEmbedMessageId = null;
    private boolean repeating = false;
    private boolean isSlashCommand = false;
    private static final Logger logger = LoggerFactory.getLogger(TrackScheduler.class);

    public TrackScheduler(AudioPlayer player, SlashCommandEvent event, Display display) {
        this.player = player;
        this.event = event;
        this.display = display;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if (!isSlashCommand) {
            if (previousEmbedMessageId != null && previousEmbedMessageId.get() != null) {
                event.getChannel().deleteMessageById(previousEmbedMessageId.get()).queue(
                        message -> {},
                        failure -> {
                            if (failure instanceof ErrorResponseException ere) {
                                logger.info(ere.getMessage());
                            }
                        }
                );
            }
        }

        previousEmbedMessageId = display.displayNowPlaying(event, player, isSlashCommand);
        isSlashCommand = false;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (repeating) {
                this.player.startTrack(track.makeClone(), false);
            } else {
                nextTrack(false);
            }
        }
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            deque.offer(track);
        }
    }

    public void nextTrack(boolean isSlashCommand) {
        if (isSlashCommand) {
            this.isSlashCommand = true;
        }
        player.startTrack(deque.poll(), false);
    }

    public AudioTrack removeTrack() {
        return deque.removeFirst();
    }

    public void shuffle() {
        List<AudioTrack> trackList = getDequeAsList();
        Collections.shuffle(trackList);
        setListAsDeque(trackList);
    }

    public void skipTo(int index) {
        List<AudioTrack> trackList = getDequeAsList();
        trackList.subList(0, index).clear();
        setListAsDeque(trackList);
    }

    public AudioTrack removeTrack(int index) {
        List<AudioTrack> trackList = getDequeAsList();
        AudioTrack removedTrack = trackList.remove(index);
        setListAsDeque(trackList);
        return removedTrack;
    }

    private List<AudioTrack> getDequeAsList() {
        List<AudioTrack> trackList = new ArrayList<>();
        deque.drainTo(trackList);
        return trackList;
    }

    private void setListAsDeque(List<AudioTrack> trackList) {
        deque.addAll(trackList);
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public BlockingDeque<AudioTrack> getDeque() {
        return deque;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setEvent(SlashCommandEvent event) {
        this.event = event;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }
}
