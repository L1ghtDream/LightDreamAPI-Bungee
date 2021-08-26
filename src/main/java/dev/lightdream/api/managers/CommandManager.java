package dev.lightdream.api.managers;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.MessageUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings({"MismatchedQueryAndUpdateOfStringBuilder"})
public class CommandManager implements CommandExecutor, TabCompleter {

    private final LightDreamPlugin plugin;
    private final List<Command> commands;

    public CommandManager(LightDreamPlugin plugin, String command, List<Command> commands) {
        this.plugin = plugin;
        plugin.getCommand(command).setExecutor(this);
        plugin.getCommand(command).setTabCompleter(this);
        this.commands = commands;
        this.commands.sort(Comparator.comparing(com -> com.aliases.get(0)));
    }

    public void sendUsage(CommandSender sender) {
        StringBuilder helpCommandOutput = new StringBuilder();
        helpCommandOutput.append("\n");

        if (plugin.baseLang.helpCommand.size() == 0) {
            for (Command command : commands) {
                if (sender.hasPermission(command.permission)) {
                    helpCommandOutput.append(command.usage);
                    helpCommandOutput.append("\n");
                }
            }
        } else {
            for (String line : plugin.baseLang.helpCommand) {
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
                MessageUtils.sendMessage(sender, plugin.baseLang.mustBeAPlayer);
                return true;
            }

            if (command.onlyForConsole && !(sender instanceof ConsoleCommandSender)) {
                MessageUtils.sendMessage(sender, plugin.baseLang.mustBeConsole);
                return true;
            }

            if (!hasPermission(sender, command.permission)) {
                MessageUtils.sendMessage(sender, plugin.baseLang.noPermission);
                return true;
            }

            command.execute(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            return true;
        }

        MessageUtils.sendMessage(sender, plugin.baseLang.unknownCommand);
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
