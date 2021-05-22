package winston.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import util.Constants;
import winston.commands.music.util.GuildMusicManager;
import winston.commands.music.util.PlayerManager;
import winston.commands.music.util.TrackScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static util.Constants.*;
import static winston.commands.music.common.Common.joinVoiceChannel;
import static winston.commands.music.common.Validation.memberNotInVoiceChannel;

@SuppressWarnings("ConstantConditions")
public class Voice implements ICommand {
    private final List<Constants> voiceClips = new ArrayList<>() {{
        add(WINSTON_OH_YEAH);
        add(WINSTON_OH_NO);
        add(WINSTON_HOW_EMBARRASSING);
        add(WINSTON_DONT_GET_ME_ANGRY);
        add(WINSTON_NO_MONKEY_BUSINESS);
        add(WINSTON_SORRY_ABOUT_THAT);
        add(WINSTON_THAT_WAS_AWESOME);
    }};

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        GuildVoiceState botVoiceState = bot.getVoiceState();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.getScheduler();
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();
        AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (memberNotInVoiceChannel(authorVoiceState, textChannel)) {
            return;
        }

        if (!botVoiceState.inVoiceChannel()) {
            joinVoiceChannel(ctx);
        }

        int random = new Random().nextInt(voiceClips.size());
        String randomVoiceClip = voiceClips.get(random).toString();

        if (playingTrack != null) {
            scheduler.setCurrentTack(playingTrack);
            scheduler.setCurrentPos(playingTrack.getPosition());
            PlayerManager.getInstance().loadAndPlay(ctx, randomVoiceClip, true);
            await().atMost(5, SECONDS).until(scheduler::isReadyToPlayFile);
            scheduler.nextTrack();
        } else {
            PlayerManager.getInstance().loadAndPlay(ctx, randomVoiceClip, true);
        }
    }

    @Override
    public String getName() {
        return "voice";
    }

    @Override
    public String getHelp() {
        return "Says a random voice line";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("say");
    }

    @Override
    public String getPackage() { return "Music"; }
}
