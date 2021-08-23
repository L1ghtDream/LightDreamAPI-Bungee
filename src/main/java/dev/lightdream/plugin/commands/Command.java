package dev.lightdream.plugin.commands;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.utils.init.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command {

    public final @NotNull List<String> aliases;
    public final @NotNull String description;
    public final @NotNull String permission;
    public final boolean onlyForPlayers;
    public final boolean onlyForConsole;
    public final String usage;
    public final Main plugin;

    public Command(@NotNull Main plugin, @NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.plugin = plugin;
        this.aliases = aliases;
        this.description = description;
        if (permission.equals("")) {
            this.permission = Main.PROJECT_ID + "." + aliases.get(0);
        } else {
            this.permission = Main.PROJECT_ID + "." + permission;
        }
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = "/" + Main.PROJECT_ID + " " + aliases.get(0) + " " + usage;
    }

    public abstract void execute(CommandSender sender, List<String> args);

    public abstract List<String> onTabComplete(CommandSender sender, List<String> args);

    public void sendUsage(CommandSender sender) {
        MessageUtils.sendMessage(sender, usage);
    }
}
