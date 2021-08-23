package dev.lightdream.plugin.managers;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.commands.Command;
import dev.lightdream.plugin.utils.init.MessageUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings({"MismatchedQueryAndUpdateOfStringBuilder"})
public class CommandManager implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private final List<Command> commands;

    public CommandManager(Main plugin, String command, List<Command> commands) {
        this.plugin = plugin;
        plugin.getCommand(command).setExecutor(this);
        plugin.getCommand(command).setTabCompleter(this);
        this.commands = commands;
        this.commands.sort(Comparator.comparing(com -> com.aliases.get(0)));
    }

    public void sendUsage(CommandSender sender) {
        StringBuilder helpCommandOutput = new StringBuilder();
        helpCommandOutput.append("\n");

        if (plugin.messages.helpCommand.size() == 0) {
            for (Command command : commands) {
                if (sender.hasPermission(command.permission)) {
                    helpCommandOutput.append(command.usage);
                    helpCommandOutput.append("\n");
                }
            }
        } else {
            for (String line : plugin.messages.helpCommand) {
                helpCommandOutput.append(line);
                helpCommandOutput.append("\n");
            }
        }

        MessageUtils.sendMessage(sender, helpCommandOutput.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] args) {
        if (args.length == 0) {
            for (Command command : commands) {
                if (command.aliases.get(0).equals("")) {
                    command.execute(sender, Arrays.asList(args));
                    return true;
                }
            }
            sendUsage(sender);
            return true;
        }

        for (Command command : commands) {
            if (!(command.aliases.contains(args[0]))) {
                continue;
            }

            if (command.onlyForPlayers && !(sender instanceof Player)) {
                MessageUtils.sendMessage(sender, plugin.messages.mustBeAPlayer);
                return true;
            }

            if (command.onlyForConsole && !(sender instanceof ConsoleCommandSender)) {
                MessageUtils.sendMessage(sender, plugin.messages.mustBeConsole);
                return true;
            }

            if (!hasPermission(sender, command.permission)) {
                MessageUtils.sendMessage(sender, plugin.messages.noPermission);
                return true;
            }

            command.execute(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            return true;
        }

        MessageUtils.sendMessage(sender, plugin.messages.unknownCommand);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String bukkitAlias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && hasPermission(sender, command.permission)) {
                        result.add(alias);
                    }
                }
            }
            return result;
        }

        for (Command command : commands) {
            if (command.aliases.contains(args[0]) && hasPermission(sender, command.permission)) {
                return command.onTabComplete(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            }
        }

        return Collections.emptyList();
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        return ((sender.hasPermission(permission) || permission.equalsIgnoreCase("")));
    }
}
