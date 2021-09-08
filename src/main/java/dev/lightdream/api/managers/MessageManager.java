package dev.lightdream.api.managers;

import de.themoep.minedown.MineDown;
import dev.lightdream.api.API;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class MessageManager {

    private final LightDreamPlugin plugin;
    private final boolean useMineDown;

    public MessageManager(LightDreamPlugin plugin) {
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

    @Deprecated
    public void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            User user = plugin.databaseManager.getUser((Player) sender);
            sendMessage(user, message);
        } else {
            sender.sendMessage(Utils.color(API.instance.langManager.getString(plugin, message, plugin.baseConfig.baseLang)));
        }
    }

    public void sendMessage(CommandSender sender, MessageBuilder builder) {
        if (sender instanceof Player) {
            User user = plugin.databaseManager.getUser((Player) sender);
            sendMessage(user, builder);
        } else {
            sender.sendMessage(Utils.color(builder.setBase(API.instance.langManager.getString(plugin, builder, plugin.baseConfig.baseLang)).parse()));
        }
    }

    @Deprecated
    public void sendMessage(String target, String message) {
        sendMessage(plugin.databaseManager.getUser(target), message);
    }

    public void sendMessage(String target, MessageBuilder builder) {
        sendMessage(plugin.databaseManager.getUser(target), builder);
    }

    @Deprecated
    public void sendMessage(User user, String message) {
        if (user != null) {
            sendMessage(user.uuid, message, user.lang);
        }
    }

    public void sendMessage(User user, MessageBuilder builder) {
        if (user != null) {
            sendMessage(user.uuid, builder, user.lang);
        }
    }

    @Deprecated
    public void sendMessage(UUID target, String message, String lang) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player != null) {
            if (player.isOnline()) {
                sendMessage((Player) player, message, lang);
            }
        }
    }

    public void sendMessage(UUID target, MessageBuilder builder, String lang) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player != null) {
            if (player.isOnline()) {
                sendMessage((Player) player, builder, lang);
            }
        }
    }

    @Deprecated
    public void sendMessage(Player target, String message, String lang) {
        if (useMineDown) {
            target.spigot().sendMessage(new MineDown(message).toComponent());
        } else {
            target.sendMessage(Utils.color(message));
        }
    }

    public void sendMessage(Player target, MessageBuilder builder, String lang) {
        MessageBuilder builderClone = builder.clone();
        String message = builder.setBase(API.instance.langManager.getString(plugin, builder, lang)).parse();
        if (message.equals("")) {
            message = builderClone.parse();
        }

        if (useMineDown) {
            target.spigot().sendMessage(new MineDown(message).toComponent());
        } else {
            target.sendMessage(Utils.color(message));
        }
    }

    public void broadcast(String message) {
        if (useMineDown) {
            Bukkit.broadcastMessage(Arrays.toString(new MineDown(message).toComponent()));
        } else {
            Bukkit.broadcastMessage(Utils.color(message));
        }
    }
}
