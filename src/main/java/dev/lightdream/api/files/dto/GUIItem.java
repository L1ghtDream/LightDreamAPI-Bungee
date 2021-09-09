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
    //public String args;

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

    @SuppressWarnings("InnerClassMayBeStatic")
    @AllArgsConstructor
    public class GUIItemArgs {
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

            this.functions.forEach((function, arg) -> parser.andThen(functions::put).accept(function, arg));

            return new GUIItemArgs(functions);
        }
    }
}
