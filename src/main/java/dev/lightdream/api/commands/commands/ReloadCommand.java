package dev.lightdream.api.commands.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command {
    public ReloadCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("reload"), "", "", false, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        pluginInstance.loadConfigs();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }
}
