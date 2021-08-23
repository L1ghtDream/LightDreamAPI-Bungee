package dev.lightdream.plugin.managers;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.utils.init.DatabaseUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {

    public final Main plugin;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInit(PlayerJoinEvent event) {
        DatabaseUtils.getUser(event.getPlayer().getUniqueId());
        plugin.schedulerManager.balance.put(event.getPlayer().getUniqueId(), plugin.economy.getBalance(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDelete(PlayerQuitEvent event) {
        plugin.schedulerManager.balance.remove(event.getPlayer().getUniqueId());
    }
}
