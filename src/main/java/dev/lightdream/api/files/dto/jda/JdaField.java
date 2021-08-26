package dev.lightdream.api.files.dto.jda;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JdaField {

    public String title;
    public String content;
    public boolean inline;

    public JdaField clone() {
        return new JdaField(title, content, inline);
    }

}
