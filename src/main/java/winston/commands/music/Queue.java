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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static winston.commands.music.common.Common.formatTime;
import static winston.commands.music.common.Display.buildQueueEmbed;
import static winston.commands.music.common.Display.getTrimmedTitle;
import static winston.commands.music.common.Validation.dequeIsEmpty;

public class Queue implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<Page> pages = new ArrayList<>();
        TextChannel textChannel = ctx.getChannel();
        User author = ctx.getAuthor();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx);
        BlockingDeque<AudioTrack> deque = musicManager.getScheduler().getDeque();

        if (dequeIsEmpty(deque, textChannel)) {
            return;
        }

        List<AudioTrack> trackList = new ArrayList<>(deque);
        QueueEmbedInfo embedInfo = new QueueEmbedInfo(deque, trackList);

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

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Shows the current tracks within the queue";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("q");
    }

    @Override
    public String getPackage() { return "Music"; }
}
