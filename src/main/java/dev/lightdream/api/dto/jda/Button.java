package dev.lightdream.api.dto.jda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.lightdream.api.enums.JDAButtonType;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Emoji;

@SuppressWarnings("unused")
@NoArgsConstructor
public class Button {

    public JDAButtonType type;
    public String id;
    public String text;
    public Emoji emoji;

    public Button(JDAButtonType type, String id, String text) {
        this.type = type;
        this.id = id;
        this.text = text;
    }

    public Button(JDAButtonType type, String id, Emoji emoji) {
        this.type = type;
        this.id = id;
        this.emoji = emoji;
    }

    @JsonIgnore
    public net.dv8tion.jda.api.interactions.components.Button getButton() {
        switch (type) {
            case PRIMARY:
                if (text == null) {
                    return net.dv8tion.jda.api.interactions.components.Button.primary(id, emoji);
                }
                return net.dv8tion.jda.api.interactions.components.Button.primary(id, text);
            case SECONDARY:
                if (text == null) {
                    return net.dv8tion.jda.api.interactions.components.Button.secondary(id, emoji);
                }
                return net.dv8tion.jda.api.interactions.components.Button.secondary(id, text);
            case SUCCESS:
                if (text == null) {
                    return net.dv8tion.jda.api.interactions.components.Button.success(id, emoji);
                }
                return net.dv8tion.jda.api.interactions.components.Button.success(id, text);
            case DANGER:
                if (text == null) {
                    return net.dv8tion.jda.api.interactions.components.Button.danger(id, emoji);
                }
                return net.dv8tion.jda.api.interactions.components.Button.danger(id, text);
            case LINK:
                if (text == null) {
                    return net.dv8tion.jda.api.interactions.components.Button.link(id, emoji);
                }
                return net.dv8tion.jda.api.interactions.components.Button.link(id, text);
        }
        return null;
    }

    public void parse(String target, String replacement) {
        id = id.replace("%" + target + "%", replacement);
        text = text.replace("%" + target + "%", replacement);
    }


}
