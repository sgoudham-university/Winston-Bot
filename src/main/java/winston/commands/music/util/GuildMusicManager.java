package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}
