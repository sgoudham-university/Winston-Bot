package winston.commands.music;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.QueueEmbedInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static winston.commands.music.util.Validation.queueIsEmpty;

public class Queue implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<Page> pages = new ArrayList<>();
        TextChannel textChannel = ctx.getChannel();
        User author = ctx.getAuthor();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queueIsEmpty(queue, textChannel)) {
            return;
        }

        int songsRead = 0;
        int songsRemaining = 1;
        int trackSize = Math.min(queue.size(), 10);
        int currPage = 1;
        List<AudioTrack> trackList = new ArrayList<>(queue);
        int totalPages = (int) Math.ceil((double) trackList.size() / 10);

        QueueEmbedInfo embedInfo = new QueueEmbedInfo(songsRead, trackSize, currPage, totalPages);

        while (songsRemaining > 0) {
            QueueEmbedInfo queueEmbedInfo = readSongs(trackList, author, ctx, embedInfo);
            pages.add(new Page(PageType.EMBED, queueEmbedInfo.getQueueMessageEmbed()));
            queueEmbedInfo.setCurrentPage(queueEmbedInfo.getCurrentPage() + 1);
            songsRemaining = trackList.size() - queueEmbedInfo.getTrackSize();
            queueEmbedInfo.setTrackSize(queueEmbedInfo.getTrackSize() + Math.min(songsRemaining, 10));
        }

        textChannel.sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 60, TimeUnit.SECONDS, 2));
    }

    private QueueEmbedInfo readSongs(List<AudioTrack> trackList, User author, CommandContext ctx, QueueEmbedInfo embedInfo) {
        int songsRead = embedInfo.getSongsRead();
        int trackSize = embedInfo.getTrackSize();
        int currPage = embedInfo.getCurrentPage();
        int totalPages = embedInfo.getTotalPages();

        EmbedBuilder queueEmbed = buildQueueEmbed(author, ctx, currPage, totalPages);

        for (int i = songsRead; i < trackSize; i++) {
            AudioTrack track = trackList.get(i);
            AudioTrackInfo trackInfo = track.getInfo();

            String title = trackInfo.title;
            String duration = formatTime(track.getDuration());

            String description = "**" + (i + 1) + ")**  " + title + " | `[" + duration + "]`\n";
            queueEmbed.appendDescription(description);
            embedInfo.setSongsRead(songsRead += 1);
        }
        embedInfo.setQueueMessageEmbed(queueEmbed.build());
        return embedInfo;
    }

    private EmbedBuilder getBaseEmbed(User author, CommandContext ctx, int currPage, int totalPages) {
        return new EmbedBuilder()
                .setAuthor("Page " + currPage + " / " + totalPages)
                .setThumbnail(author.getEffectiveAvatarUrl())
                .setFooter("Requested By " + author.getName(), ctx.getSelfUser().getAvatarUrl())
                .setTimestamp(new Date().toInstant());
    }

    private EmbedBuilder buildQueueEmbed(User author, CommandContext ctx, int currPage, int totalPages) {
        return getBaseEmbed(author, ctx, currPage, totalPages)
                .setTitle("Current Songs in Queue")
                .setColor(Color.BLUE);
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Shows the current songs within the queue";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("q");
    }
}
