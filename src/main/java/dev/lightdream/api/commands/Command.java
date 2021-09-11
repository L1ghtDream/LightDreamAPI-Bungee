package dev.lightdream.api.commands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.utils.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    public final @NotNull List<String> aliases;
    public final @NotNull String description;
    public final @NotNull String permission;
    public final boolean onlyForPlayers;
    public final boolean onlyForConsole;
    public final String usage;
    public final IAPI api;

    public Command(@NotNull IAPI api, @NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.api = api;
        this.aliases = new ArrayList<>();
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        this.description = description;
        if (permission.equals("")) {
            this.permission = api.getProjectID() + "." + aliases.get(0);
        } else {
            this.permission = api.getProjectID() + "." + permission;
        }
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = "/" + api.getProjectID() + " " + aliases.get(0) + " " + usage;
    }

    public abstract void execute(CommandSender sender, List<String> args);

    public abstract List<String> onTabComplete(CommandSender sender, List<String> args);

    @SuppressWarnings("unused")
    public void sendUsage(CommandSender sender) {
        api.getMessageManager().sendMessage(sender, new MessageBuilder(usage));
    }
}
