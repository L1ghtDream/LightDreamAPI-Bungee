package dev.lightdream.api.managers;

import de.themoep.minedown.MineDown;
import dev.lightdream.api.API;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.managers.local.LocalDatabaseManager;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unused"})
public class MessageManager {

    private final IAPI api;
    private final Class<?> clazz;
    private final boolean useMineDown;

    public MessageManager(IAPI api, Class<?> clazz) {
        this.api = api;
        this.clazz = clazz;

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

    public void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, new MessageBuilder(message));
    }

    public void sendMessage(CommandSender sender, MessageBuilder builder) {
        String message = getMessage(builder, api.getSettings().baseLang);

        if (sender instanceof Player) {
            User user = ((LocalDatabaseManager) api.getDatabaseManager()).getUser((Player) sender);
            sendMessage(user, builder);
        } else {
            sender.sendMessage(Utils.color(message));
        }
    }

    @SuppressWarnings("unchecked")
    public String getMessage(MessageBuilder builder, String lang) {
        StringBuilder message = new StringBuilder();
        if (builder.isList()) {
            ((List<String>) (builder.setBase(API.instance.langManager.getString(clazz, builder, lang)).parse())).forEach(message::append);
        } else {
            message.append((String) (builder.setBase(API.instance.langManager.getString(clazz, builder, lang)).parse()));
        }
        System.out.println(message);
        if (message.toString().equals("")) {
            if (builder.isList()) {
                ((List<String>) builder.getBase()).forEach(message::append);
            } else {
                message.append((String) builder.getBase());
            }
        }
        System.out.println(message);
        return message.toString();
    }

    public void sendMessage(String target, String message) {
        sendMessage(((LocalDatabaseManager) api.getDatabaseManager()).getUser(target), new MessageBuilder(message));
    }

    public void sendMessage(String target, MessageBuilder builder) {
        sendMessage(((LocalDatabaseManager) api.getDatabaseManager()).getUser(target), builder);
    }

    public void sendMessage(User user, String message) {
        if (user != null) {
            sendMessage(user.uuid, new MessageBuilder(message), user.lang);
        }
    }

    public void sendMessage(User user, MessageBuilder builder) {
        if (user != null) {
            sendMessage(user.uuid, builder, user.lang);
        }
    }

    public void sendMessage(UUID target, String message, String lang) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player != null) {
            if (player.isOnline()) {
                sendMessage((Player) player, new MessageBuilder(message), lang);
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

    public void sendMessage(Player target, String message, String lang) {
        sendMessage(target, new MessageBuilder(message), lang);
    }

    public void sendMessage(Player target, MessageBuilder builder, String lang) {
        String message = getMessage(builder, lang);

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

    public void sendAll(MessageBuilder message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, message));
    }
}
