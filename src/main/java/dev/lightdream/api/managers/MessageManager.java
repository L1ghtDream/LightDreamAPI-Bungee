package dev.lightdream.api.managers;

import dev.lightdream.api.API;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unused"})
public class MessageManager {

    private final IAPI api;
    private final Class<?> clazz;

    public MessageManager(IAPI api, Class<?> clazz) {
        this.api = api;
        this.clazz = clazz;
    }

    public void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, new MessageBuilder(message));
    }

    public void sendMessage(CommandSender sender, MessageBuilder builder) {
        if (sender instanceof ProxiedPlayer) {
            User user = api.getDatabaseManager().getUser((ProxiedPlayer) sender);
            sendMessage(user, builder);
        } else {
            String message = getMessage(builder, api.getSettings().baseLang);
            sender.sendMessage(Utils.color(message));
        }
    }

    @SuppressWarnings("unchecked")
    public String getMessage(MessageBuilder builder, String lang) {
        MessageBuilder builderClone = builder.clone();
        StringBuilder message = new StringBuilder();
        if (builder.isList()) {
            ((List<String>) (builder.changeBase(API.instance.langManager.getString(clazz, builder, lang)).parse())).forEach(message::append);
        } else {
            message.append((String) (builder.changeBase(API.instance.langManager.getString(clazz, builder, lang)).parse()));
        }
        if (message.toString().equals("")) {
            if (builderClone.isList()) {
                ((List<String>) builderClone.parse()).forEach(message::append);
            } else {
                message.append((String) builderClone.parse());
            }
        }
        return message.toString();
    }

    public void sendMessage(String target, String message) {
        sendMessage(api.getDatabaseManager().getUser(target), new MessageBuilder(message));
    }

    public void sendMessage(String target, MessageBuilder builder) {
        sendMessage(api.getDatabaseManager().getUser(target), builder);
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
        ProxiedPlayer player = api.getPlugin().getProxy().getPlayer(target);
        if (player != null) {
            if (player.isConnected()) {
                sendMessage(player, new MessageBuilder(message), lang);
            }
        }
    }

    public void sendMessage(UUID target, MessageBuilder builder, String lang) {
        ProxiedPlayer player = api.getPlugin().getProxy().getPlayer(target);
        if (player != null) {
            if (player.isConnected()) {
                sendMessage(player, builder, lang);
            }
        }
    }

    public void sendMessage(ProxiedPlayer target, String message, String lang) {
        sendMessage(target, new MessageBuilder(message), lang);
    }

    public void sendMessage(ProxiedPlayer target, MessageBuilder builder, String lang) {
        target.sendMessage(Utils.color(getMessage(builder, lang)));
    }

    public void broadcast(String message) {

        api.getPlugin().getProxy().broadcast(Utils.color(message));
    }

    public void sendAll(MessageBuilder message) {
        api.getPlugin().getProxy().getPlayers().forEach(player -> sendMessage(player, message));
    }
}
