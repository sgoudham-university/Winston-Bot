package winston.commands.music;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import winston.commands.music.util.PlayerManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class Play implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel textChannel = ctx.getChannel();
        Member bot = ctx.getSelfMember();
        GuildVoiceState botVoiceState = bot.getVoiceState();

        if (ctx.getArgs().isEmpty()) {
            textChannel.sendMessage("Usage Is: " + getUsage()).queue();
        }

        if (!botVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("I need to be in a voice channel to use this command!").queue();
            return;
        }

        Member author = ctx.getMember();
        GuildVoiceState authorVoiceState = author.getVoiceState();
        if (!authorVoiceState.inVoiceChannel()) {
            textChannel.sendMessage("You need to be in a voice channel to use this command!").queue();
            return;
        }

        if (!authorVoiceState.getChannel().equals(botVoiceState.getChannel())) {
            textChannel.sendMessage("This command requires both parties to be in the same voice channel!").queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(textChannel, link);
    }

    private boolean isUrl(String link) {
        try {
            new URI(link);
        } catch (URISyntaxException use) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays the given song";
    }

    @Override
    public String getUsage() {
        return "`!play <link | text>`";
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }
}
