package dev.lightdream.plugin.managers;

import de.themoep.minedown.MineDown;
import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class MessageManager {

    private final Main plugin;
    private boolean useMineDown = false;

    public MessageManager(Main plugin) {
        this.plugin = plugin;

        List<String> mineDownVersions = Arrays.asList("1.16", "1.17");
        boolean useMineDown = false;
        for (String version : mineDownVersions) {
            if (Bukkit.getServer().getVersion().contains(version)) {
                useMineDown = true;
                break;
            }
        }
        this.useMineDown = useMineDown;
    }

    public void sendMessage(Object target, String message) {
        if (target instanceof CommandSender) {
            ((CommandSender) target).sendMessage(Utils.color(plugin.getMessages().prefix + message));
        }
    }

    public void sendMessage(UUID target, String message) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player != null) {
            if (player.isOnline()) {
                sendMessage((Player) player, message);
            }
        }
    }

    public void sendMessage(String target, String message) {
        Player player = Bukkit.getPlayer(target);
        if (player != null) {
            sendMessage(player, message);
        }
    }

    private void sendMessage(Player target, String message) {
        if (useMineDown) {
            target.spigot().sendMessage(new MineDown(plugin.getMessages().prefix + message).toComponent());
        } else {
            target.sendMessage(Utils.color(plugin.getMessages().prefix + message));
        }
    }
}
