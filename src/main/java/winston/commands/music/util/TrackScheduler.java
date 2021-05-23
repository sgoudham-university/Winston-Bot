package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import command.CommandContext;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import winston.bot.config.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

import static winston.commands.music.common.Display.*;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingDeque<AudioTrack> deque;
    private CommandContext ctx;
    private AtomicReference<Long> previousEmbedMessageId;

    private boolean repeating = false;
    private boolean readyToPlayFile = false;
    private AudioTrack currentTack;
    private Long currentPos;

    TrackScheduler(AudioPlayer player, CommandContext ctx) {
        this.player = player;
        this.ctx = ctx;

        previousEmbedMessageId = null;
        deque = new LinkedBlockingDeque<>();
    }

    @Override
    public void onPlayerPause(AudioPlayer player) { displayPausing(ctx, player); }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        displayResuming(ctx, player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if (previousEmbedMessageId != null) {
            ctx.getChannel().deleteMessageById(previousEmbedMessageId.get()).queue(message -> {},
                    failure -> {
                        if (failure instanceof ErrorResponseException) {
                            ErrorResponseException ex = (ErrorResponseException) failure;
                            Logger.LOGGER.info(ex.getMessage());
                        }
            });
        }
        previousEmbedMessageId = displayNowPlaying(ctx, player);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (isRepeating()) {
                this.player.startTrack(track.makeClone(), false);
            } else if (currentTack != null) {
                this.player.startTrack(currentTack.makeClone(), false);
                this.player.getPlayingTrack().setPosition(currentPos);

                currentTack = null;
                currentPos = null;
                readyToPlayFile = false;
            } else {
                nextTrack();
            }
        }
    }

    public void queue(AudioTrack track, boolean isLocalFile) {
        if (!player.startTrack(track, true)) {
            if (isLocalFile) {
                deque.addFirst(track);
                readyToPlayFile = true;
            } else {
                deque.offer(track);
            }
        }
    }

    public void nextTrack() {
        player.startTrack(deque.poll(), false);
    }

    public AudioTrack removeTrack() {
        return deque.removeFirst();
    }

    public void shuffle() {
        List<AudioTrack> trackList = getQueueAsList();
        Collections.shuffle(trackList);
        setListAsDeque(trackList);
    }

    public void skipTo(int index) {
        List<AudioTrack> trackList = getQueueAsList();
        trackList.subList(0, index).clear();
        setListAsDeque(trackList);
    }

    public AudioTrack removeTrack(int index) {
        List<AudioTrack> trackList = getQueueAsList();
        AudioTrack removedTrack = trackList.remove(index);
        setListAsDeque(trackList);
        return removedTrack;
    }

    private List<AudioTrack> getQueueAsList() {
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
    public AudioTrack getCurrentTack() {
        return currentTack;
    }
    public void setCurrentTack(AudioTrack currentTack) {
        this.currentTack = currentTack;
    }
    public Long getCurrentPos() {
        return currentPos;
    }
    public void setCurrentPos(Long currentPos) {
        this.currentPos = currentPos;
    }
    public boolean isReadyToPlayFile() {
        return readyToPlayFile;
    }
    public void setReadyToPlayFile(boolean readyToPlayFile) {
        this.readyToPlayFile = readyToPlayFile;
    }
    public void setCtx(CommandContext ctx) { this.ctx = ctx; }
    public CommandContext getCtx() { return ctx; }
}
