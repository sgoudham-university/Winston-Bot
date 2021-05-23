package winston.commands.music.util;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import command.CommandContext;

public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    GuildMusicManager(AudioPlayerManager manager, CommandContext ctx) {
        audioPlayer = manager.createPlayer();
        scheduler = new TrackScheduler(audioPlayer, ctx);
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
