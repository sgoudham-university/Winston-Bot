package me.goudham.winston.domain;

import net.dv8tion.jda.api.interactions.InteractionHook;

public class QueueButton {
    private QueuePages queuePages;
    private InteractionHook interactionHook;

    public QueuePages getQueuePages() {
        return queuePages;
    }

    public void setQueuePages(QueuePages queuePages) {
        this.queuePages = queuePages;
    }

    public InteractionHook getInteractionHook() {
        return interactionHook;
    }

    public void setInteractionHook(InteractionHook interactionHook) {
        this.interactionHook = interactionHook;
    }
}
