package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingDeque<AudioTrack> queue;
    private boolean repeating = false;
    private boolean readyToPlayFile = false;
    private AudioTrack currentTack;
    private Long currentPos;

    TrackScheduler(AudioPlayer player) {
        this.player = player;
        queue = new LinkedBlockingDeque<>();
    }

    public void queue(AudioTrack track, boolean isLocalFile) {
        if (!player.startTrack(track, true)) {
            if (isLocalFile) {
                queue.addFirst(track);
                setReadyToPlayFile(true);
            } else {
                queue.offer(track);
            }
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (isRepeating()) {
                this.player.startTrack(track.makeClone(), false);
            } else if (currentTack != null) {
                this.player.startTrack(currentTack.makeClone(), false);
                this.player.getPlayingTrack().setPosition(currentPos);

                setCurrentTack(null);
                setCurrentPos(null);
                setReadyToPlayFile(false);
            } else {
                nextTrack();
            }
        }
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    public void removeTrack() {
        queue.removeFirst();
    }

    public void shuffle() {
        List<AudioTrack> trackList = getQueueAsList();
        Collections.shuffle(trackList);
        setListAsQueue(trackList);
    }

    public void skipTo(int index) {
        List<AudioTrack> trackList = getQueueAsList();
        trackList.subList(0, index - 1).clear();
        setListAsQueue(trackList);
    }

    public void removeTrack(int index) {
        List<AudioTrack> trackList = getQueueAsList();
        trackList.remove(index - 1);
        setListAsQueue(trackList);
    }

    private List<AudioTrack> getQueueAsList() {
        List<AudioTrack> trackList = new ArrayList<>();
        queue.drainTo(trackList);
        return trackList;
    }

    private void setListAsQueue(List<AudioTrack> trackList) {
        queue.addAll(trackList);
    }
    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }
    public AudioPlayer getPlayer() {
        return player;
    }
    public BlockingDeque<AudioTrack> getQueue() {
        return queue;
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
    private void setReadyToPlayFile(boolean readyToPlayFile) {
        this.readyToPlayFile = readyToPlayFile;
    }
}
