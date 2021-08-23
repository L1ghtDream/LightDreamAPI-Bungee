package dev.lightdream.plugin.managers;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.events.BalanceChangeEvent;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("FieldCanBeLocal")
public class SchedulerManager {

    private final Main plugin;
    public final HashMap<UUID, Double> balance = new HashMap<>();

    public SchedulerManager(Main plugin){
        this.plugin = plugin;
        registerBalanceChangeEvent();
    }


    public void registerBalanceChangeEvent() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                double finalBalance = plugin.economy.getBalance(player);
                double initialBalance = balance.get(player.getUniqueId());

                if (initialBalance != finalBalance) {
                    BalanceChangeEvent event = new BalanceChangeEvent(player, initialBalance, finalBalance);
                    Bukkit.getPluginManager().callEvent(event);

                    balance.put(player.getUniqueId(), event.getFinalBalance());
                    if (event.getFinalBalance() > finalBalance) {
                        plugin.economy.depositPlayer(player, event.getFinalBalance() - finalBalance);
                    } else {
                        plugin.economy.withdrawPlayer(player, finalBalance - event.getFinalBalance());
                    }
                }
            });

        }, 1, 1);
    }

}
