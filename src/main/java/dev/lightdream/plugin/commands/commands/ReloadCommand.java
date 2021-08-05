package dev.lightdream.plugin.commands.commands;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command {
    public ReloadCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("reload"), "Reloads the configs", "reload", false, false, "reload");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        plugin.loadConfigs();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }
}
