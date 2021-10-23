package me.goudham.winston.domain;

import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class QueuePages {
    private int currentIndex;
    private final List<MessageEmbed> messageEmbeds;

    public QueuePages(int currentIndex, List<MessageEmbed> messageEmbeds) {
        this.currentIndex = currentIndex;
        this.messageEmbeds = messageEmbeds;
    }

    public void incrementIndexByOne() {
        currentIndex++;
        if (currentIndex >= messageEmbeds.size()) {
            currentIndex = currentIndex - messageEmbeds.size();
        }
    }

    public void incrementIndexByTwo() {
        currentIndex += 2;
        if (currentIndex >= messageEmbeds.size()) {
            currentIndex = currentIndex - messageEmbeds.size();
        }
    }

    public void decrementIndexByOne() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = messageEmbeds.size() + currentIndex;
        }
    }

    public void decrementIndexByTwo() {
        currentIndex -= 2;
        if (currentIndex < 0) {
            currentIndex = messageEmbeds.size() + currentIndex;
        }
    }

    public MessageEmbed getEmbedAtCurrentIndex() {
        return messageEmbeds.get(currentIndex);
    }

    public int getMessageEmbedSize() {
        return messageEmbeds.size();
    }
}
