package dev.lightdream.plugin.utils;

import dev.lightdream.plugin.files.dto.jda.JdaField;
import net.dv8tion.jda.api.EmbedBuilder;

public class JdaUtils {

    public static EmbedBuilder createEmbed(int r, int g, int b, String title, String thumbnail, JdaField... fields) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(title, null);
        embed.setColor(new java.awt.Color(r, g, b));
        for (JdaField field : fields) {
            embed.addField(field.title, field.content, field.inline);
        }
        embed.setThumbnail(thumbnail);

        embed.setFooter("Author: LightDream#4379");
        return embed;
    }

    public static EmbedBuilder createEmbed(int r, int g, int b, String title, String thumbnail, String text) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(title, null);
        embed.setColor(new java.awt.Color(r, g, b));
        embed.setDescription(text);
        embed.setThumbnail(thumbnail);
        embed.setFooter("Author: LightDream#4379");
        return embed;
    }
}
