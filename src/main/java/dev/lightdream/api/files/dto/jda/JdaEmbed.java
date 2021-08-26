package dev.lightdream.api.files.dto.jda;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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

    public void parse(String target, String replacement) {
        fields.forEach(field -> field.content = field.content.replace(target, replacement));
    }

}
