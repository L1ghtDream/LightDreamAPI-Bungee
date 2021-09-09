package dev.lightdream.api.files.dto;

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

        public GUIItemArgs parse(BiConsumer<String, Object> parser) {
            HashMap<String, Object> functions = new HashMap<>();
            System.out.println(functions);
            this.functions.forEach((function, arg) -> parser.andThen(functions::put).accept(function, arg));
            System.out.println(functions);
            return new GUIItemArgs(functions);
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
