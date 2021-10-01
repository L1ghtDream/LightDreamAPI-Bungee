package dev.lightdream.api.dto;

import dev.lightdream.api.utils.MessageBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

@NoArgsConstructor
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

    @SuppressWarnings("unused")
    public GUIItem(Item item) {
        this.item = item;
        this.args = new GUIItemArgs();
        this.repeated = false;
        this.nextSlots = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public GUIItem(Item item, GUIItemArgs args, List<Integer> nextSlots) {
        this.item = item;
        this.args = args;
        this.repeated = true;
        this.nextSlots = nextSlots;
    }

    @SuppressWarnings("unused")
    public GUIItem(Item item, List<Integer> nextSlots) {
        this.item = item;
        this.repeated = true;
        this.nextSlots = nextSlots;
    }

    private GUIItem(Item item, GUIItemArgs args, boolean repeated, List<Integer> nextSlots) {
        this.item = item;
        this.args = args;
        this.repeated = repeated;
        this.nextSlots = nextSlots;
    }

    public List<String> getFunctions() {
        return args.getArgs();
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

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GUIItemArg extends Serializable implements java.io.Serializable {
        public MessageBuilder function;
        public MessageBuilder args;

        @SuppressWarnings("unchecked")
        public GUIItemArg(String function, Object args) {
            this.function = new MessageBuilder(function);
            if (args instanceof String) {
                this.args = new MessageBuilder((String) args);
            } else {
                this.args = new MessageBuilder((List<String>) args);
            }
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public GUIItemArg clone() {
            return new GUIItemArg(function.clone(), args.clone());
        }

        @Override
        public String toString() {
            return "GUIItemArg{" +
                    "function=" + function +
                    ", args=" + args +
                    '}';
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class GUIItemArgs extends Serializable implements java.io.Serializable {
        public List<GUIItemArg> args = new ArrayList<>();

        @SuppressWarnings("unused")
        public GUIItemArgs(HashMap<String, Object> functions) {
            functions.forEach((function, args) -> this.args.add(new GUIItemArg(function, args)));

            /*
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
            */
        }

        public List<String> getArgs() {
            List<String> functions = new ArrayList<>();
            for (GUIItemArg arg : this.args) {
                if (!arg.function.isList()) {
                    functions.add((String) arg.function.getBase());
                }
            }
            return functions;
        }

        public MessageBuilder getFunctionArgs(String function) {
            for (GUIItemArg guiItemArg : this.args) {
                if (guiItemArg.function.equals(new MessageBuilder(function))) {
                    return guiItemArg.args;
                }
            }
            return null;
        }

        @SuppressWarnings({"MethodDoesntCallSuperMethod"})
        public GUIItemArgs clone() {
            List<GUIItemArg> args = new ArrayList<>();

            this.args.forEach(arg -> args.add(arg.clone()));

            return new GUIItemArgs(args);
        }

        public GUIItemArgs parse(BiConsumer<MessageBuilder, MessageBuilder> parser) {
            //HashMap<Object, Object> functions = new HashMap<>();
            List<GUIItemArg> args = new ArrayList<>();
            this.args.forEach(arg -> parser.andThen((k, v) -> args.add(new GUIItemArg(k, v))).accept(arg.function, arg.args));
            return new GUIItemArgs(args);
        }

        @Override
        public String toString() {
            return "GUIItemArgs{" +
                    "functions=" + args +
                    '}';
        }
    }
}
