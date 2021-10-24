package dev.lightdream.api.dto.jda;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class JdaField {

    public String title;
    public String content;
    public boolean inline;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public JdaField clone() {
        return new JdaField(title, content, inline);
    }

    public JdaField parse(String target, String replacement) {
        JdaField jdaField = clone();
        jdaField.title = jdaField.title.replace("%" + target + "%", replacement);
        jdaField.content = jdaField.content.replace("%" + target + "%", replacement);
        return jdaField;
    }

}
