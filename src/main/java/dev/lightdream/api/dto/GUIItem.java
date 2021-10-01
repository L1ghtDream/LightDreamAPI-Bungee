package dev.lightdream.api.dto;

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

    public List<String> getFunctionArgs(String function) {
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

    @NoArgsConstructor
    public static class GUIItemArg extends Serializable implements java.io.Serializable {
        public String function;
        public List<String> args;

        public GUIItemArg(String function, String arg) {
            this.function = function;
            this.args = new ArrayList<>();
            this.args.add(arg);
            /*
            if (args instanceof String) {
                this.args = new MessageBuilder((String) args);
            } else {
                this.args = new MessageBuilder((List<String>) args);
            }
             */
        }

        public GUIItemArg(String function, List<String> args) {
            this.function = function;
            this.args = args;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public GUIItemArg clone() {
            return new GUIItemArg(function, new ArrayList<>(args));
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

        @SuppressWarnings({"unused", "unchecked"})
        public GUIItemArgs(HashMap<String, Object> functions) {
            functions.forEach((function, args) -> {
                if (args instanceof String) {
                    this.args.add(new GUIItemArg(function, (String) args));
                } else {
                    this.args.add(new GUIItemArg(function, (List<String>) args));
                }
            });

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
                functions.add(arg.function);
            }
            return functions;
        }

        public List<String> getFunctionArgs(String function) {
            for (GUIItemArg guiItemArg : this.args) {
                if (guiItemArg.function.equals(function)) {
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

        public GUIItemArgs parse(BiConsumer<String, List<String>> parser) {
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
