package winston.commands.music.util;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class QueueEmbedInfo {
    private int songsRead;
    private int trackSize;
    private int currentPage;
    private int totalPages;
    private MessageEmbed queueMessageEmbed;

    public QueueEmbedInfo(int songsRead, int trackSize, int currentPage, int totalPages) {
        this.songsRead = songsRead;
        this.trackSize = trackSize;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
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
