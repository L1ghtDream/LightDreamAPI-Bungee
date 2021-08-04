package dev.lightdream.plugin.commands;

import dev.lightdream.plugin.Main;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command {
    public ReloadCommand(@NotNull Main plugin) {
        super(plugin, Collections.singletonList("reload"), "Reloads the configs", "reload", false, false, "reload");
    }

    @Override
    public void execute(Object sender, List<String> args) {
        plugin.loadConfigs();
    }

    @Override
    public List<String> onTabComplete(Object commandSender, List<String> args) {
        return new ArrayList<>();
    }
}
