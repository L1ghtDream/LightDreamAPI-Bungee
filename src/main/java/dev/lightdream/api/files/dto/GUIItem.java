package dev.lightdream.api.files.dto;

import dev.lightdream.api.utils.MessageBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

@NoArgsConstructor
@AllArgsConstructor
public class GUIItem {

    public Item item;
    public GUIItemArgs args;

    public List<String> getFunctions() {
        return args.getFunctions();
    }

    public Object getFunctionArgs(String function) {
        return args.getFunctionArgs(function);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public GUIItem clone() {
        return new GUIItem(item.clone(), args.clone());
    }

    @Override
    public String toString() {
        return "GUIItem{" +
                "item=" + item +
                ", args=" + args +
                '}';
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GUIItemArgs {
        public HashMap<MessageBuilder, MessageBuilder> functions;

        public List<String> getFunctions() {
            List<String> functions = new ArrayList<>();
            for (MessageBuilder message : this.functions.keySet()) {
                functions.add(message.getBase());
            }
            return functions;
        }

        public Object getFunctionArgs(String function) {
            return functions.get(new MessageBuilder(function));
        }

        @SuppressWarnings({"MethodDoesntCallSuperMethod", "unchecked"})
        public GUIItemArgs clone() {
            return new GUIItemArgs((HashMap<MessageBuilder, MessageBuilder>) functions.clone());
        }

        public GUIItemArgs parse(BiConsumer<MessageBuilder, MessageBuilder> parser) {
            HashMap<MessageBuilder, MessageBuilder> functions = new HashMap<>();
            this.functions.forEach((function, arg) -> {
                parser.andThen((f, a) -> {
                    functions.put(f, a);
                }).accept(function, arg);
            });
            return new GUIItemArgs(functions);
        }

        @Override
        public String toString() {
            return "GUIItemArgs{" +
                    "functions=" + functions +
                    '}';
        }
    }
}
