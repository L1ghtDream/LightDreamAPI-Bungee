package dev.lightdream.api.commands.commands;

import dev.lightdream.api.API;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VersionCommand extends Command {


    public VersionCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("version"), "", "", false, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        MessageUtils.sendMessage(sender, plugin.baseLang.version.replace("%project_name%", plugin.projectName).replace("%version%", plugin.version));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }
}
