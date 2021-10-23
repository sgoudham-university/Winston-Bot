package me.goudham.winston.bot.command.music.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class QueueEmbedInfo {
    private int songsRead;
    private int trackSize;
    private int currentPage;
    private int totalPages;
    private MessageEmbed queueMessageEmbed;

    public QueueEmbedInfo(BlockingQueue<AudioTrack> queue, List<AudioTrack> trackList) {
        songsRead = 0;
        trackSize = Math.min(queue.size(), 10);
        currentPage = 1;
        totalPages = (int) Math.ceil((double) trackList.size() / 10);
    }

    public MessageEmbed getQueueMessageEmbed() {
        return queueMessageEmbed;
    }

    public void setQueueMessageEmbed(MessageEmbed queueMessageEmbed) {
        this.queueMessageEmbed = queueMessageEmbed;
    }

    public int getSongsRead() {
        return songsRead;
    }

    public void setSongsRead(int songsRead) {
        this.songsRead = songsRead;
    }

    public int getTrackSize() {
        return trackSize;
    }

    public void setTrackSize(int trackSize) {
        this.trackSize = trackSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
