package dev.lightdream.api.managers;

import dev.lightdream.api.API;
import dev.lightdream.api.LightDreamPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class PAPI extends PlaceholderExpansion {

    private final API api;

    public PAPI(API api) {
        this.api = api;
    }

    public static String parse(OfflinePlayer player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "L1ghtDream";
    }

    @Override
    public @NotNull String getIdentifier() {
        return API.instance.projectName.toLowerCase();
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) {
            return null;
        }

        for (LightDreamPlugin plugin : api.plugins) {
            String parse = plugin.parsePapi(player, identifier);
            if (!parse.equals("")) {
                return parse;
            }
        }

        return null;
    }
}
