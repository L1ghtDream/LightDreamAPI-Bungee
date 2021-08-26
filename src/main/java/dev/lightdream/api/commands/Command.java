package dev.lightdream.api.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.utils.MessageUtils;
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
    private final LightDreamPlugin plugin;

    public Command(@NotNull LightDreamPlugin plugin, @NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.plugin = plugin;
        this.aliases = aliases;
        this.description = description;
        if (permission.equals("")) {
            this.permission = plugin.projectID + "." + aliases.get(0);
        } else {
            this.permission = plugin.projectID + "." + permission;
        }
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = "/" + plugin.projectID + " " + aliases.get(0) + " " + usage;
    }

    public abstract void execute(CommandSender sender, List<String> args);

    public abstract List<String> onTabComplete(CommandSender sender, List<String> args);

    public void sendUsage(CommandSender sender) {
        MessageUtils.sendMessage(sender, usage);
    }
}
