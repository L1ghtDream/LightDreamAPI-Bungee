package dev.lightdream.api.files.dto.jda;

import dev.lightdream.api.files.dto.Position;
import dev.lightdream.api.files.dto.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class JdaEmbed implements Serializable {

    public Long channel;
    public int red;
    public int green;
    public int blue;
    public String title;
    public String thumbnail;
    public String description;
    public List<JdaField> fields;

    @SuppressWarnings("unused")
    public void parse(String target, String replacement) {
        fields.forEach(field -> field.content = field.content.replace(target, replacement));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public JdaEmbed clone() {
        List<JdaField> fields = new ArrayList<>();
        this.fields.forEach(field -> fields.add(field.clone()));
        return new JdaEmbed(channel, red, green, blue, title, thumbnail, description, fields);
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

    @Override
    public Object deserialize(String serialized) {
        return null;
    }

    @Override
    public Constructor<?> getMainConstructor() {
        return null;
    }

    @Override
    public List<String> getParameterNames() {
        return null;
    }

}
