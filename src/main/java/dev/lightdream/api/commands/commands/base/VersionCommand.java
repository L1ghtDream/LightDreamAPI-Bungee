package dev.lightdream.api.commands.commands.base;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.utils.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class VersionCommand extends SubCommand {

    public VersionCommand(@NotNull IAPI api) {
        super(api, Collections.singletonList("version"), "", "", false, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().version).addPlaceholders(new HashMap<String, String>() {{
            put("project_name", api.getProjectName());
            put("version", api.getProjectVersion());
        }}));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }
}
