package dev.lightdream.plugin.managers;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class MessageManager {

    private final Main plugin;

    public MessageManager(Main plugin) {
        this.plugin = plugin;
    }


    public void sendMessage(Object target, String message) {
        if (target instanceof CommandSender) {
            ((CommandSender) target).sendMessage(Utils.color(plugin.getMessages().prefix + message));
        }
    }

    public void sendMessage(UUID target, String message) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player != null) {
            if(player.isOnline()){
                ((Player)player).sendMessage(Utils.color(plugin.getMessages().prefix + message));

            }
        }
    }

    public void sendMessage(String target, String message) {
        Player player = Bukkit.getPlayer(target);
        if (player != null) {
            player.sendMessage(Utils.color(plugin.getMessages().prefix + message));
        }
    }
}
