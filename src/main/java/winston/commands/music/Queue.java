package winston.commands.music;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
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
import java.util.function.Predicate;

import static winston.commands.music.common.Display.getTrimmedTitle;
import static winston.commands.music.util.Common.formatTime;
import static winston.commands.music.util.Validation.queueIsEmpty;

public class Queue implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<Page> pages = new ArrayList<>();
        TextChannel textChannel = ctx.getChannel();
        User author = ctx.getAuthor();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.getScheduler().getQueue();

        if (queueIsEmpty(queue, textChannel)) {
            return;
        }

        List<AudioTrack> trackList = new ArrayList<>(queue);
        QueueEmbedInfo embedInfo = new QueueEmbedInfo(queue, trackList);

        int songsRemaining = 1;
        while (songsRemaining > 0) {
            QueueEmbedInfo queueEmbedInfo = readSongs(trackList, author, ctx, embedInfo);
            pages.add(new Page(queueEmbedInfo.getQueueMessageEmbed()));
            queueEmbedInfo.setCurrentPage(queueEmbedInfo.getCurrentPage() + 1);
            songsRemaining = trackList.size() - queueEmbedInfo.getTrackSize();
            queueEmbedInfo.setTrackSize(queueEmbedInfo.getTrackSize() + Math.min(songsRemaining, 10));
        }

        Object embedContent = pages.get(0).getContent();
        textChannel.sendMessage((MessageEmbed) embedContent).queue(success ->
                Pages.paginate(success, pages, 120, TimeUnit.SECONDS, 5, true, Predicate.isEqual(author))
        );
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
            String trackIndex = "**" + (i + 1) + ")**  ";
            String duration = "`[" + formatTime(track.getDuration()) + "]`  ";
            String title = getTrimmedTitle(trackInfo.title, 40);

            queueEmbed.appendDescription(trackIndex + duration + title + "\n");
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
