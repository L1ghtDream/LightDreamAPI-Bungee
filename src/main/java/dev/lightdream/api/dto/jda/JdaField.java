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

    public void parse(String target, String replacement){
        title = title.replace("%"+target+"%", replacement);
        content = content.replace("%"+target+"%", replacement);
    }

}
