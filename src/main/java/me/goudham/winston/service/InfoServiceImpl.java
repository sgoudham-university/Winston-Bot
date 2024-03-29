package me.goudham.winston.service;

import jakarta.inject.Singleton;
import java.awt.Color;
import java.util.List;
import java.util.StringJoiner;
import me.goudham.winston.domain.Constants;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import org.jetbrains.annotations.NotNull;

@Singleton
public class InfoServiceImpl implements InfoService {

    @Override
    public @NotNull String getColorAsHex(@NotNull Color color) {
        return "#" + Integer.toHexString(color.getRGB()).toUpperCase();
    }

    @Override
    public Color getMemberColour(@NotNull Member member) {
        return member.getColor() == null ? Color.BLACK : member.getColor();
    }

    @Override
    public @NotNull String getMemberBadges(@NotNull Member member) {
        User user = member.getUser();
        String memberBadges = "";

        if (user.isBot()) memberBadges += Constants.BADGE_BOT;
        if (member.getTimeBoosted() != null) memberBadges += Constants.BADGE_SERVER_BOOST;

        memberBadges += user.getFlags().toString()
                .substring(1, user.getFlags().toString().length() - 1)
                .replace(",", "")
                .replace("PARTNER", Constants.BADGE_PARTNER)
                .replace("HYPESQUAD_BRAVERY", Constants.BADGE_BRAVERY)
                .replace("HYPESQUAD_BRILLIANCE", Constants.BADGE_BRILLIANCE)
                .replace("HYPESQUAD_BALANCE", Constants.BADGE_BALANCE)
                .replace("VERIFIED_DEVELOPER", Constants.BADGE_EARLY_VERIFIED_BOT_DEVELOPER)
                .replace("EARLY_SUPPORTER", Constants.BADGE_EARLY_SUPPORTER)
                .replace("SYSTEM", Constants.BADGE_STAFF)
                .replace("BUG_HUNTER_LEVEL_1", Constants.BADGE_BUG_HUNTER)
                .replace("BUG_HUNTER_LEVEL_2", Constants.BADGE_BUG_HUNTER)
                .replace("VERIFIED_BOT", Constants.BADGE_VERIFIED_BOT);

        return memberBadges;
    }

    @Override
    public @NotNull String getMemberOnlineStatus(@NotNull Member member) {
        return member.getOnlineStatus().toString()
                .replace("ONLINE", Constants.STATUS_ONLINE)
                .replace("IDLE", Constants.STATUS_IDLE)
                .replace("DO_NOT_DISTURB", Constants.STATUS_DND)
                .replace("OFFLINE", Constants.STATUS_OFFLINE)
                .replace("STREAMING", Constants.STATUS_STREAMING);
    }

    @Override
    public @NotNull String getTopRole(@NotNull Object object) {
        String topRole = "";

        if (object instanceof Member member) {
            List<Role> memberRoles = member.getRoles();
            if (memberRoles.isEmpty()) return "No Roles";
            topRole =  memberRoles.get(0).getAsMention();
        } else if (object instanceof Guild guild) {
            StringJoiner topRoleJoiner = new StringJoiner("");
            guild.getRoleCache().stream()
                    .findFirst()
                    .ifPresentOrElse(
                            role -> topRoleJoiner.add(role.getAsMention()),
                            () -> topRoleJoiner.setEmptyValue("No Roles")
                    );
            topRole = topRoleJoiner.toString();
        }

        return topRole;
    }

    @Override
    public String getListOfMembers(@NotNull List<Member> members, int limit) {
        StringJoiner memberJoiner = new StringJoiner(" **|** ");
        if (members.isEmpty()) return "No Members";

        members.stream()
                .limit(limit)
                .forEach(member -> memberJoiner.add(member.getAsMention()));

        if (members.size() > limit) {
            int leftOverMembers = members.size() - limit;
            memberJoiner.add(" and ** " + leftOverMembers + " ** more ");
        }

        return memberJoiner.toString();
    }

    @Override
    public String getGuildEmotes(@NotNull Guild guild, int limit) {
        SnowflakeCacheView<Emote> emoteCache = guild.getEmoteCache();

        StringJoiner guildEmoteJoiner = new StringJoiner(" ");
        if (emoteCache.isEmpty()) {
            guildEmoteJoiner.setEmptyValue("No Emojis");
        } else {
            emoteCache.stream()
                    .limit(limit)
                    .forEach(emote -> guildEmoteJoiner.add(emote.getAsMention()));
        }
        if (emoteCache.size() > limit) {
            long leftOverEmojis = emoteCache.size() - limit;
            guildEmoteJoiner.add(" and ** " + leftOverEmojis + " ** more");
        }

        return guildEmoteJoiner.toString();
    }

    @Override
    public String getGuildRoles(@NotNull Guild guild, int limit) {
        SortedSnowflakeCacheView<Role> roleCache = guild.getRoleCache();

        StringJoiner guildRolesJoiner = new StringJoiner(" **|** ");
        if (roleCache.isEmpty()) {
            guildRolesJoiner.setEmptyValue("No Roles");
        } else {
            roleCache.stream()
                    .limit(limit)
                    .forEach(role -> guildRolesJoiner.add(role.getAsMention()));
        }
        if (roleCache.size() > limit) {
            long leftOverRoles = roleCache.size() - limit;
            guildRolesJoiner.add(" and ** " + leftOverRoles + " ** more ");
        }

        return guildRolesJoiner.toString();
    }

    @Override
    public String getMemberRoles(@NotNull Member member, int limit) {
        StringJoiner memberRolesJoiner = new StringJoiner(" **|** ");
        List<Role> memberRoles = member.getRoles();
        if (memberRoles.isEmpty()) return "No Roles";

        memberRoles.stream()
                .limit(limit)
                .forEach(role -> memberRolesJoiner.add(role.getAsMention()));

        if (memberRoles.size() > limit) {
            int leftOverRoles = memberRoles.size() - limit;
            memberRolesJoiner.add(" and ** " + leftOverRoles + " ** more ");
        }

        return memberRolesJoiner.toString();
    }

    @Override
    public long getMemberStatusCount(@NotNull Guild guild, OnlineStatus onlineStatus) {
        return guild.getMemberCache().stream()
                .filter(member -> member.getOnlineStatus() == onlineStatus)
                .count();
    }

    @Override
    public long getChannelCount(@NotNull Guild guild, ChannelType channelType) {
        return guild.getChannels().stream()
                .filter(guildChannel -> guildChannel.getType() == channelType)
                .count();
    }

    @Override
    public @NotNull String getJoinedDate(@NotNull Member member) {
        long timeJoined = member.getTimeJoined().toInstant().getEpochSecond();
        return "<t:" + timeJoined + ":F>";
    }

    @Override
    public @NotNull String getCreationDate(Object object) {
        long time = 0L;

        if (object instanceof Role role) {
            time = role.getTimeCreated().toInstant().getEpochSecond();
        } else if (object instanceof Member member) {
            time = member.getTimeCreated().toInstant().getEpochSecond();
        } else if (object instanceof Guild guild) {
            time = guild.getTimeCreated().toInstant().getEpochSecond();
        } else if (object instanceof VoiceChannel voiceChannel) {
            time = voiceChannel.getTimeCreated().toInstant().getEpochSecond();
        } else if (object instanceof TextChannel textChannel) {
            time = textChannel.getTimeCreated().toInstant().getEpochSecond();
        }

        return "<t:" + time + ":F>";
    }
}
