package dev.lightdream.api.commands.commands.ldapi;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.utils.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PluginsCommand extends SubCommand {
    public PluginsCommand(@NotNull IAPI api) {
        super(api, Collections.singletonList("plugins"), "", "", false, false, "");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        StringBuilder s = new StringBuilder();
        api.getAPI().plugins.forEach(plugin -> s.append(new MessageBuilder(api.getLang().pluginFormat).addPlaceholders(new HashMap<String, String>() {{
            put("project-name", plugin.getProjectName());
            put("project-id", plugin.getProjectID());
            put("project-version", plugin.getProjectVersion());
        }}).parse()));

        api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().pluginList).addPlaceholders(new HashMap<String, String>() {{
            put("plugins", s.toString());
        }}));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
