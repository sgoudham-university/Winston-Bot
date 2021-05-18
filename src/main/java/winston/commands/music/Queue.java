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

import static winston.commands.music.util.Common.formatTime;

public class Queue implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<Page> pages = new ArrayList<>();
        TextChannel textChannel = ctx.getChannel();
        User author = ctx.getAuthor();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.getScheduler().getQueue();

        List<AudioTrack> trackList = new ArrayList<>(queue);
        QueueEmbedInfo embedInfo = new QueueEmbedInfo(queue, trackList);

        int songsRemaining = 1;
        while (songsRemaining > 0) {
            QueueEmbedInfo queueEmbedInfo = readSongs(trackList, author, ctx, embedInfo);
            pages.add(new Page(PageType.EMBED, queueEmbedInfo.getQueueMessageEmbed()));
            queueEmbedInfo.setCurrentPage(queueEmbedInfo.getCurrentPage() + 1);
            songsRemaining = trackList.size() - queueEmbedInfo.getTrackSize();
            queueEmbedInfo.setTrackSize(queueEmbedInfo.getTrackSize() + Math.min(songsRemaining, 10));
        }

        textChannel.sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> Pages.paginate(success, pages, 120, TimeUnit.SECONDS, 5));
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
            int titleSize = Math.min(title.length(), 40);
            String suffix = titleSize < title.length() ? "..." : "";

            queueEmbed.appendDescription("**" + (i + 1) + ")**  ")
                    .appendDescription("`[" + duration + "]`  ")
                    .appendDescription(title.substring(0, titleSize) + suffix)
                    .appendDescription("\n");

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
                .setThumbnail(null)
                .setTitle("Current Songs in Queue")
                .setColor(Color.BLUE);
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
