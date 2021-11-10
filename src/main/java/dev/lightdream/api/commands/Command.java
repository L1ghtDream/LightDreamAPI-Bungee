package dev.lightdream.api.commands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Command extends net.md_5.bungee.api.plugin.Command {

    //todo add minimum required arguments

    private final IAPI api;
    private List<SubCommand> subCommands;

    public Command(IAPI api, String command, List<SubCommand> subCommands) {
        super("command");
        this.api = api;
        try {
            api.getPlugin().getProxy().getPluginManager().registerCommand(api.getPlugin(), this);
        } catch (NullPointerException e) {
            api.getLogger().severe("The command '" + command + "' does not exist in bungee.yml");
            return;
        }

        List<SubCommand> sc = new ArrayList<>();

        subCommands.forEach(subCommand -> {
            if (!sc.contains(subCommand)) {
                sc.add(subCommand);
            }
        });

        this.subCommands = sc;
        this.subCommands.sort(Comparator.comparing(com -> com.aliases.get(0)));
    }

    public void sendUsage(CommandSender sender) {
        sendUsage(api.getDatabaseManager().getUser(sender));
    }

    public void sendUsage(User user) {
        StringBuilder helpCommandOutput = new StringBuilder();
        helpCommandOutput.append("\n");

        if (api.getLang().helpCommand.equals("")) {
            for (SubCommand subCommand : subCommands) {
                if (user.hasPermission(subCommand.permission)) {
                    helpCommandOutput.append(subCommand.usage);
                    helpCommandOutput.append("\n");
                }
            }
        } else {
            helpCommandOutput.append(api.getLang().helpCommand);
        }

        user.sendMessage(api, helpCommandOutput.toString());
    }

/*
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String bukkitAlias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                for (String alias : subCommand.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && hasPermission(sender, subCommand.permission)) {
                        result.add(alias);
                    }
                }
            }
            return result;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.aliases.contains(args[0]) && hasPermission(sender, subCommand.permission)) {
                return subCommand.onTabComplete(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            }
        }

        return Collections.emptyList();
    }

 */

    private boolean hasPermission(CommandSender sender, String permission) {
        return ((sender.hasPermission(permission) || permission.equalsIgnoreCase("")));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.aliases.get(0).equals("")) {
                    subCommand.execute(sender, Arrays.asList(args));
                    return;
                }
            }
            sendUsage(sender);
            return;
        }

        for (SubCommand subCommand : subCommands) {
            if (!(subCommand.aliases.contains(args[0].toLowerCase()))) {
                continue;
            }

            if (subCommand.onlyForPlayers && !(sender instanceof ProxiedPlayer)) {
                api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().mustBeAPlayer));
                return;
            }

            if (!hasPermission(sender, subCommand.permission)) {
                api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().noPermission));
                return;
            }

            subCommand.execute(sender, new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            return;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.aliases.get(0).equals("")) {
                subCommand.execute(sender, Arrays.asList(args));
                return;
            }
        }

        api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().unknownCommand));
    }
}
