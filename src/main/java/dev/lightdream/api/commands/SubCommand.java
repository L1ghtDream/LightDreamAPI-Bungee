package dev.lightdream.api.commands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SubCommand {

    public final @NotNull List<String> aliases = new ArrayList<>();
    public final @NotNull String description;
    public final @NotNull String permission;
    public final boolean onlyForPlayers;
    public final boolean onlyForConsole;
    public final String usage;
    public final IAPI api;

    public SubCommand(@NotNull IAPI api, @NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.api = api;
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

    @SuppressWarnings("unused")
    public SubCommand(@NotNull IAPI api, String alias, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.api = api;
        this.aliases.add(alias.toLowerCase());
        this.description = "";
        this.permission = api.getProjectID() + "." + aliases.get(0);
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = "/" + api.getProjectID() + " " + aliases.get(0) + " " + usage;
    }

    @SuppressWarnings("unused")
    public SubCommand(@NotNull IAPI api, List<String> aliases, boolean onlyForPlayers, boolean onlyForConsole, @NotNull String usage) {
        this.api = api;
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        this.description = "";
        this.permission = api.getProjectID() + "." + aliases.get(0);
        this.onlyForPlayers = onlyForPlayers;
        this.onlyForConsole = onlyForConsole;
        this.usage = "/" + api.getProjectID() + " " + aliases.get(0) + " " + usage;
    }

    public void execute(CommandSender sender, List<String> args) {
        if (sender instanceof Player) {
            System.out.println(api.getDatabaseManager());
            execute(api.getDatabaseManager().getUser(sender), args);
        } else {
            execute(api.getConsoleUser(), args);
        }
    }

    public abstract void execute(User user, List<String> args);

    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        if (sender instanceof Player) {
            return onTabComplete(api.getDatabaseManager().getUser(sender), args);
        } else {
            return onTabComplete(api.getConsoleUser(), args);
        }
    }

    public abstract List<String> onTabComplete(User sender, List<String> args);


    @SuppressWarnings("unused")
    public void sendUsage(CommandSender sender) {
        api.getMessageManager().sendMessage(sender, new MessageBuilder(usage));
    }

    public void sendUsage(User user) {
        user.sendMessage(api, usage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCommand that = (SubCommand) o;
        for (String alias : aliases) {
            if (that.aliases.contains(alias)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aliases);
    }
}
