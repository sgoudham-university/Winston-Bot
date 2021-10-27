package me.goudham.winston.listener;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.goudham.winston.domain.QueuePages;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Singleton
public class ButtonClickListener extends ListenerAdapter {
    private final Map<String, QueuePages> queueEmbeds;

    @Inject
    public ButtonClickListener(Map<String, QueuePages> queueEmbeds) {
        this.queueEmbeds = queueEmbeds;
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String[] uuidAndType = event.getComponentId().split("_");
        String uuid = uuidAndType[0];
        String type = uuidAndType[1];
        QueuePages queuePages = queueEmbeds.get(uuid);

        switch (type) {
            case "previousByTwo" -> {
                queuePages.decrementIndexByTwo();
                event.editMessageEmbeds(queuePages.getEmbedAtCurrentIndex()).queue();
            }
            case "previous" -> {
                queuePages.decrementIndexByOne();
                event.editMessageEmbeds(queuePages.getEmbedAtCurrentIndex()).queue();
            }
            case "delete" -> {
                event.getMessage().delete().queue();
                queueEmbeds.remove(uuid);
            }
            case "forward" -> {
                queuePages.incrementIndexByOne();
                event.editMessageEmbeds(queuePages.getEmbedAtCurrentIndex()).queue();
            }
            case "forwardByTwo" -> {
                queuePages.incrementIndexByTwo();
                event.editMessageEmbeds(queuePages.getEmbedAtCurrentIndex()).queue();
            }
        }
    }
}