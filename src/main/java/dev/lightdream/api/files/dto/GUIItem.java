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

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GUIItemArgs {
        public HashMap<String, Object> functions;

        public List<String> getFunctions() {
            return new ArrayList<>(this.functions.keySet());
        }

        public Object getFunctionArgs(String function) {
            return functions.get(function);
        }

        @SuppressWarnings({"unchecked", "MethodDoesntCallSuperMethod"})
        public GUIItemArgs clone() {
            return new GUIItemArgs((HashMap<String, Object>) functions.clone());
        }

        public GUIItemArgs parse(BiConsumer<MessageBuilder, MessageBuilder> parser) {
            HashMap<MessageBuilder, MessageBuilder> functions = new HashMap<>();
            System.out.println("1 " + functions);
            new HashMap<MessageBuilder, MessageBuilder>(){{
                put(new MessageBuilder("function - %test%"), new MessageBuilder("arg - %test%"));
            }}.forEach((function, arg) -> {
                System.out.println("2 " + arg);
                parser.andThen((f,a)->{
                    System.out.println("3 " + f);
                    System.out.println("4 " + a);
                    functions.put(f,a);
                }).accept(function, arg);
                System.out.println("5 " + arg);
            });
            System.out.println("6 " + functions);
            return this;
        }

        @Override
        public String toString() {
            return "GUIItemArgs{" +
                    "functions=" + functions +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GUIItem{" +
                "item=" + item +
                ", args=" + args +
                '}';
    }
}
