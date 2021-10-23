package me.goudham.winston.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface EmbedService {
    EmbedBuilder getBaseEmbed();
    EmbedBuilder getBaseEmbed(SlashCommandEvent event, String status);
    EmbedBuilder getBaseEmbedWithPageCounter(User author, SlashCommandEvent event, int currPage, int totalPages);
    EmbedBuilder getUserEmbed(Member member);
    EmbedBuilder getQueueEmbed(User author, SlashCommandEvent event, int currPage, int totalPages);
    MessageEmbed getSearchEmbed(SlashCommandEvent event, List<AudioTrack> searchResults, int numberOfTracksReturned);
    MessageEmbed getNowPlayingEmbed(SlashCommandEvent event, String title, String url, String status, String trackPos);
    MessageEmbed getSimpleInfoEmbed(String message, Color colour);
    MessageEmbed getSimpleInfoEmbedWithDesc(String message, Color colour);
}
