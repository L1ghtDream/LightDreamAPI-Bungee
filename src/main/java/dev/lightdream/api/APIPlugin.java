package dev.lightdream.api;

import dev.lightdream.api.managers.MessageManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class APIPlugin extends LightDreamPlugin {

    public API api;

    @Override
    public void onEnable() {
        this.api = new API(this);
    }

    @Override
    public void onDisable() {
        api.onDisable();
    }

    @Override
    public @NotNull String parsePapi(OfflinePlayer player, String identifier) {
        return api.parsePapi(player, identifier);
    }

    @Override
    public void loadBaseCommands() {
        api.loadBaseCommands();
    }

    @Override
    public MessageManager instantiateMessageManager() {
        return api.instantiateMessageManager();
    }

    @Override
    public void registerLangManager() {
        api.registerLangManager();
    }
}
