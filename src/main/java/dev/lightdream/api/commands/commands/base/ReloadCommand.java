package dev.lightdream.api.commands.commands.base;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(@NotNull IAPI api) {
        super(api, Collections.singletonList("reload"), "", "", false, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        api.loadConfigs();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }
}
