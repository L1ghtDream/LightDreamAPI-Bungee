package dev.lightdream.plugin.commands;

import dev.lightdream.plugin.Main;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
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
        this.permission = Main.PROJECT_ID + "." + permission;
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = Main.PROJECT_ID + " " + usage;
    }

    public abstract void execute(Object sender, List<String> args);

    public abstract List<String> onTabComplete(Object commandSender, List<String> args);

    public void sendUsage(Object sender) {
        plugin.getMessageManager().sendMessage(sender, usage);
    }
}
