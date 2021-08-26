package dev.lightdream.api.managers;

import dev.lightdream.api.API;
import dev.lightdream.api.events.BalanceChangeEvent;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class BalanceChangeEventRunnable {

    public final HashMap<UUID, Double> balance = new HashMap<>();
    private final API api;

    public BalanceChangeEventRunnable(API api) {
        this.api = api;
        registerBalanceChangeEvent();
    }

    public void registerBalanceChangeEvent() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(api, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            double finalBalance = api.economy.getBalance(player);
            double initialBalance = balance.get(player.getUniqueId());

            if (initialBalance != finalBalance) {
                BalanceChangeEvent event = new BalanceChangeEvent(player, initialBalance, finalBalance);
                Bukkit.getPluginManager().callEvent(event);

                balance.put(player.getUniqueId(), event.getFinalBalance());
                if (event.getFinalBalance() > finalBalance) {
                    api.economy.depositPlayer(player, event.getFinalBalance() - finalBalance);
                } else {
                    api.economy.withdrawPlayer(player, finalBalance - event.getFinalBalance());
                }
            }
        }), 1, 1);
    }

}
