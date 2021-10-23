package me.goudham.winston.service;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface MusicService {
    MessageEmbed joinVoiceChannel(SlashCommandEvent slashCommandEvent);
    MessageEmbed leaveVoiceChannel(SlashCommandEvent slashCommandEvent);
}
