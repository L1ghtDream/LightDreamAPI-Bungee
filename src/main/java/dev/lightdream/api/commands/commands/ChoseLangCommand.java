package dev.lightdream.api.commands.commands;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChoseLangCommand extends Command {
    public ChoseLangCommand(@NotNull LightDreamPlugin plugin) {
        super(plugin, Collections.singletonList("choseLang"), "", "", true, false, "[lang]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }

        User user = pluginInstance.databaseManager.getUser((Player) sender);

        String lang = args.get(0);
        if (!pluginInstance.baseConfig.langs.contains(lang)) {
            pluginInstance.messageManager.sendMessage(user, new MessageBuilder(pluginInstance.baseLang.invalidLang));
            return;
        }

        user.setLang(lang, pluginInstance);
        pluginInstance.messageManager.sendMessage(user, new MessageBuilder(pluginInstance.baseLang.langChanged));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return pluginInstance.baseConfig.langs;
    }
}
