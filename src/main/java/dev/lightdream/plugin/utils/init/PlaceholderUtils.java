package dev.lightdream.plugin.utils.init;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.databases.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlaceholderUtils {

    public static Main plugin = null;

    public static void init(Main main) {
        plugin = main;
    }

    public static String parse(String raw, User user) {
        String parsed = raw;

        if (user != null) {
            parsed = parsed.replace("%player%", user.name);
        }

        return parsed;
    }

    public static List<String> parse(List<String> raw, User user) {
        List<String> parsed = new ArrayList<>();

        for (String line : raw) {
            parsed.add(parse(line, user));
        }

        return parsed;
    }

}
