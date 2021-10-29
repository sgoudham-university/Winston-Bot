package me.goudham.winston.bot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.micronaut.context.annotation.Executable;
import jakarta.inject.Inject;
import me.goudham.winston.bot.command.music.audio.GuildMusicManager;
import me.goudham.winston.bot.command.music.audio.PlayerManager;
import me.goudham.winston.bot.command.music.audio.QueueEmbedInfo;
import me.goudham.winston.command.annotation.SlashCommand;
import me.goudham.winston.domain.QueueButton;
import me.goudham.winston.domain.QueuePages;
import me.goudham.winston.service.EmbedService;
import me.goudham.winston.service.ValidationService;
import me.goudham.winston.service.util.TimeUtils;
import me.goudham.winston.service.util.TitleUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;

@SlashCommand(name = "queue", description = "Displays the current queue of songs")
public class Queue {
    private final ValidationService validationService;
    private final EmbedService embedService;
    private final PlayerManager playerManager;
    private final TitleUtils titleUtils;
    private final TimeUtils timeUtils;
    private final Map<String, QueueButton> queueEmbeds;

    @Inject
    public Queue(ValidationService validationService, EmbedService embedService, PlayerManager playerManager, TitleUtils titleUtils, TimeUtils timeUtils, Map<String, QueueButton> queueEmbeds) {
        this.validationService = validationService;
        this.embedService = embedService;
        this.titleUtils = titleUtils;
        this.timeUtils = timeUtils;
        this.playerManager = playerManager;
        this.queueEmbeds = queueEmbeds;
    }

    @Executable
    public void handle(SlashCommandEvent slashCommandEvent) {
        List<MessageEmbed> pages = new ArrayList<>();
        User author = slashCommandEvent.getUser();
        GuildMusicManager musicManager = playerManager.getMusicManager(slashCommandEvent);
        BlockingDeque<AudioTrack> deque = musicManager.getTrackScheduler().getDeque();

        if (validationService.dequeIsEmpty(deque, slashCommandEvent)) {
            return;
        }

        List<AudioTrack> trackList = new ArrayList<>(deque);
        QueueEmbedInfo embedInfo = new QueueEmbedInfo(deque, trackList);

        int songsRemaining = 1;
        while (songsRemaining > 0) {
            QueueEmbedInfo queueEmbedInfo = readSongs(trackList, author, embedInfo);
            pages.add(queueEmbedInfo.getQueueMessageEmbed());
            queueEmbedInfo.setCurrentPage(queueEmbedInfo.getCurrentPage() + 1);
            songsRemaining = trackList.size() - queueEmbedInfo.getTrackSize();
            queueEmbedInfo.setTrackSize(queueEmbedInfo.getTrackSize() + Math.min(songsRemaining, 10));
        }

        QueueButton queueButton = new QueueButton();
        String uuidAsString = UUID.randomUUID().toString();
        slashCommandEvent.replyEmbeds(pages.get(0))
                .addActionRow(
                        Button.secondary(uuidAsString + "_previousByTwo", Emoji.fromMarkdown("⏮")).withDisabled(pages.size() <= 2),
                        Button.secondary(uuidAsString + "_previous", Emoji.fromMarkdown("⬅")).withDisabled(pages.size() == 1),
                        Button.danger(uuidAsString + "_delete", Emoji.fromMarkdown("\uD83D\uDEAE")),
                        Button.secondary(uuidAsString + "_forward", Emoji.fromMarkdown("➡")).withDisabled(pages.size() == 1),
                        Button.secondary(uuidAsString + "_forwardByTwo", Emoji.fromMarkdown("⏭")).withDisabled(pages.size() <= 2)
                )
                .queue(success -> queueButton.setInteractionHook(success.getInteraction().getHook()));
        QueuePages queuePages = new QueuePages(0, pages);
        queueButton.setQueuePages(queuePages);
        queueEmbeds.put(uuidAsString, queueButton);
    }

    private QueueEmbedInfo readSongs(List<AudioTrack> trackList, User author, QueueEmbedInfo embedInfo) {
        int songsRead = embedInfo.getSongsRead();
        int trackSize = embedInfo.getTrackSize();
        int currPage = embedInfo.getCurrentPage();
        int totalPages = embedInfo.getTotalPages();
        EmbedBuilder queueEmbed = embedService.getQueueEmbed(author, currPage, totalPages);

        for (int i = songsRead; i < trackSize; i++) {
            AudioTrack track = trackList.get(i);
            AudioTrackInfo trackInfo = track.getInfo();
            String trackIndex = "**" + (i + 1) + ")**  ";
            String duration = "`[" + timeUtils.formatTime(track.getDuration()) + "]`  ";
            String title = titleUtils.getTrimmedTitle(trackInfo.title, 40);

            queueEmbed.appendDescription(trackIndex + duration + title + "\n");
            embedInfo.setSongsRead(songsRead += 1);
        }
        embedInfo.setQueueMessageEmbed(queueEmbed.build());

        return embedInfo;
    }
}
