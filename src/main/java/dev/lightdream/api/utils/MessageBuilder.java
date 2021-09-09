package dev.lightdream.api.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MessageBuilder {

    @Getter
    private String base;
    private List<String> placeholders = new ArrayList<>();
    private List<String> values = new ArrayList<>();

    public MessageBuilder(String base) {
        this.base = base;
    }

    private MessageBuilder(String base, List<String> placeholders, List<String> values) {
        this.base = base;
        this.placeholders = placeholders;
        this.values = values;
    }

    @SuppressWarnings("unused")
    public MessageBuilder addPlaceholders(String... placeholders) {
        this.placeholders.addAll(Arrays.asList(placeholders));
        return this;
    }

    public MessageBuilder addPlaceholders(HashMap<String, String> placeholders) {
        placeholders.forEach((placeholder, value) -> {
            this.placeholders.add(placeholder);
            this.values.add(value);
        });

        return this;
    }

    @SuppressWarnings("unused")
    public MessageBuilder addValues(String... values) {
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    public String parse() {
        String parsed = base;

        for (int i = 0; i < Math.min(placeholders.size(), values.size()); i++) {
            parsed = parsed.replace("%" + placeholders.get(i) + "%", values.get(i));
        }

        return parsed;
    }

    public MessageBuilder setBase(String base) {
        this.base = base;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public MessageBuilder clone() {
        return new MessageBuilder(this.base, this.placeholders, this.values);
    }

    @Override
    public String toString() {
        return "MessageBuilder{" +
                "base='" + base + '\'' +
                ", placeholders=" + placeholders +
                ", values=" + values +
                '}';
    }
}
