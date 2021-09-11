package dev.lightdream.api.utils;

import java.util.*;

public class MessageBuilder {

    private String base;
    private List<String> baseList;
    private List<String> placeholders = new ArrayList<>();
    private List<String> values = new ArrayList<>();

    public MessageBuilder(String base) {
        this.base = base;
    }

    @SuppressWarnings("unused")
    public MessageBuilder(List<String> baseList) {
        this.baseList = baseList;
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

    public boolean isList() {
        return baseList != null;
    }

    public Object parse() {
        if (isList()) {
            List<String> parsedList = baseList;

            parsedList.forEach(line -> {
                String parsed = base;

                for (int i = 0; i < Math.min(placeholders.size(), values.size()); i++) {
                    parsed = parsed.replace("%" + placeholders.get(i) + "%", values.get(i));
                }

                parsedList.add(parsed);
            });

            this.baseList = parsedList;
            return parsedList;
        }
        String parsed = base;

        for (int i = 0; i < Math.min(placeholders.size(), values.size()); i++) {
            parsed = parsed.replace("%" + placeholders.get(i) + "%", values.get(i));
        }

        this.base = parsed;
        return parsed;
    }

    public Object getBase() {
        return isList() ? baseList : base;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MessageBuilder setBase(String base) {
        this.base = base;
        this.baseList = null;
        return this;
    }

    @SuppressWarnings("unchecked")
    public MessageBuilder setBase(Object base) {
        if (base instanceof String) {
            return setBase((String) base);
        } else if (base instanceof List) {
            return setBase((List<String>) base);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public MessageBuilder setBase(List<String> baseList) {
        this.baseList = baseList;
        this.base = null;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public MessageBuilder clone() {
        return new MessageBuilder(this.base, this.placeholders, this.values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageBuilder that = (MessageBuilder) o;
        return Objects.equals(base, that.base);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base);
    }

    @SuppressWarnings("unused")
    public MessageBuilder parseAndGet() {
        parse();
        return this;
    }

    @SuppressWarnings("unused")
    public List<String> split(String separator) {
        List<String> parts = new ArrayList<>();

        if (isList()) {
            baseList.forEach(line -> parts.addAll(Arrays.asList(line.split(separator))));

            return parts;
        }

        parts.addAll(Arrays.asList(base.split(separator)));
        return parts;
    }

    @Override
    public String toString() {
        return "MessageBuilder{" +
                "base='" + base + '\'' +
                ", baseList=" + baseList +
                ", placeholders=" + placeholders +
                ", values=" + values +
                '}';
    }
}
