package dev.lightdream.api.commands.commands.base;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(@NotNull IAPI api) {
        super(api, Collections.singletonList("reload"), "", "", false, false, "");
    }

    @Override
    public void execute(User user, List<String> args) {
        api.loadConfigs();
    }

    @Override
    public List<String> onTabComplete(User user, List<String> args) {
        return new ArrayList<>();
    }
}
