package dev.lightdream.api;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class APIPlugin extends JavaPlugin {

    public API api;

    @Override
    public void onEnable() {
        this.api = new API(this);
    }

    @Override
    public void onDisable() {
        api.getDatabaseManager().save();
    }

}
