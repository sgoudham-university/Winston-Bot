package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    GuildMusicManager(AudioPlayerManager manager) {
        audioPlayer = manager.createPlayer();
        scheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(scheduler);
        sendHandler = new AudioPlayerSendHandler(audioPlayer);
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
    AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
    public TrackScheduler getScheduler() { return scheduler; }
}
