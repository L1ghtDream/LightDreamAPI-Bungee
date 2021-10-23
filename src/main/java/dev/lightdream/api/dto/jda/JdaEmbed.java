package dev.lightdream.api.dto.jda;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class JdaEmbed {

    public Long channel;
    public int red;
    public int green;
    public int blue;
    public String title;
    public String thumbnail;
    public String description;
    public List<JdaField> fields;
    public List<Button> buttons;

    @SuppressWarnings("unused")
    public JdaEmbed parse(String target, String replacement) {
        JdaEmbed parsed = clone();
        parsed.description = parsed.description.replace("%" + target + "%", replacement);
        parsed.thumbnail = parsed.thumbnail.replace("%" + target + "%", replacement);
        parsed.title = parsed.title.replace("%" + target + "%", replacement);
        parsed.fields.forEach(field -> field.parse(target, replacement));
        parsed.buttons.forEach(button -> button.parse(target, replacement));
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public JdaEmbed clone() {
        List<JdaField> fields = new ArrayList<>();
        this.fields.forEach(field -> fields.add(field.clone()));
        return new JdaEmbed(channel, red, green, blue, title, thumbnail, description, fields, buttons);
    }

    @Override
    public String toString() {
        return "JdaEmbed{" +
                "channel=" + channel +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", fields=" + fields +
                '}';
    }

    public EmbedBuilder build() {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setThumbnail(thumbnail);
        fields.forEach(field -> embed.addField(field.title, field.content, field.inline));
        embed.setTitle(title, null);
        embed.setColor(new java.awt.Color(red, green, blue));
        embed.setDescription(description);
        embed.setFooter("Author: LightDream#4379");

        return embed;
    }

    @SuppressWarnings("unused")
    public MessageAction buildMessageAction(MessageChannel channel) {
        List<net.dv8tion.jda.api.interactions.components.Button> buttons = new ArrayList<>();

        this.buttons.forEach(button -> buttons.add(button.getButton()));

        return channel.sendMessageEmbeds(build().build()).setActionRow(buttons);
    }

}
