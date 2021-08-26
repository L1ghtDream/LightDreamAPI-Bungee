package dev.lightdream.api.utils;

import dev.lightdream.api.files.dto.jda.JdaEmbed;
import net.dv8tion.jda.api.EmbedBuilder;

public class JdaUtils {

    public static EmbedBuilder createEmbed(JdaEmbed jdaEmbed) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setThumbnail(jdaEmbed.thumbnail);
        jdaEmbed.fields.forEach(field -> embed.addField(field.title, field.content, field.inline));
        embed.setTitle(jdaEmbed.title, null);
        embed.setColor(new java.awt.Color(jdaEmbed.red, jdaEmbed.green, jdaEmbed.blue));
        embed.setDescription(jdaEmbed.description);
        embed.setFooter("Author: LightDream#4379");

        return embed;
    }
}
