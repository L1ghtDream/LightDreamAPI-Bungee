package dev.lightdream.plugin.managers;

import dev.lightdream.plugin.Main;
import org.bukkit.event.Listener;

public class EventManager implements Listener {

    public final Main plugin;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


}
