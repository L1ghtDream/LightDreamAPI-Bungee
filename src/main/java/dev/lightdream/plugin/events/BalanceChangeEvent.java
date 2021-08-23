package dev.lightdream.plugin.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BalanceChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final double initialBalance;
    @Getter
    @Setter
    private double finalBalance;

    public BalanceChangeEvent(Player player, double initialBalance, double finalBalance) {
        this.player = player;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
