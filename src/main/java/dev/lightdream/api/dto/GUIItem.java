package dev.lightdream.api.dto;

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
    public boolean repeated;
    public List<Integer> nextSlots;

    @SuppressWarnings("unused")
    public GUIItem(Item item, GUIItemArgs args) {
        this.item = item;
        this.args = args;
        this.repeated = false;
        this.nextSlots = new ArrayList<>();
    }

    public List<String> getFunctions() {
        return args.getFunctions();
    }

    public MessageBuilder getFunctionArgs(String function) {
        return args.getFunctionArgs(function);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public GUIItem clone() {
        return new GUIItem(item.clone(), args.clone(), repeated, nextSlots);
    }

    @Override
    public String toString() {
        return "GUIItem{" +
                "item=" + item +
                ", args=" + args +
                ", repeatedItem=" + repeated +
                '}';
    }

    public static class GUIItemArgs {
        public HashMap<MessageBuilder, MessageBuilder> functions;

        @SuppressWarnings("unused")
        public GUIItemArgs() {
            this.functions = new HashMap<>();
        }

        public GUIItemArgs(HashMap<Object, Object> functions) {
            HashMap<MessageBuilder, MessageBuilder> f = new HashMap<>();
            functions.forEach((function, arg) -> {
                if (function instanceof String) {
                    if (arg instanceof String) {
                        f.put(new MessageBuilder((String) function), new MessageBuilder((String) arg));
                    } else if (arg instanceof MessageBuilder) {
                        f.put(new MessageBuilder((String) function), (MessageBuilder) arg);
                    }
                } else if (function instanceof MessageBuilder) {
                    if (arg instanceof String) {
                        f.put((MessageBuilder) function, new MessageBuilder((String) arg));
                    } else if (arg instanceof MessageBuilder) {
                        f.put((MessageBuilder) function, (MessageBuilder) arg);
                    }
                }
            });
            this.functions = f;
        }

        public List<String> getFunctions() {
            List<String> functions = new ArrayList<>();
            for (MessageBuilder message : this.functions.keySet()) {
                if (!message.isList()) {
                    functions.add((String) message.getBase());
                }
            }
            return functions;
        }

        public MessageBuilder getFunctionArgs(String function) {
            return functions.get(new MessageBuilder(function));
        }

        @SuppressWarnings({"MethodDoesntCallSuperMethod"})
        public GUIItemArgs clone() {
            HashMap<Object, Object> functions = new HashMap<>();
            this.functions.forEach((function, value) -> functions.put(function.clone(), value.clone()));
            return new GUIItemArgs(functions);
        }

        public GUIItemArgs parse(BiConsumer<MessageBuilder, MessageBuilder> parser) {
            HashMap<Object, Object> functions = new HashMap<>();
            this.functions.forEach((function, arg) -> parser.andThen(functions::put).accept(function, arg));
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
