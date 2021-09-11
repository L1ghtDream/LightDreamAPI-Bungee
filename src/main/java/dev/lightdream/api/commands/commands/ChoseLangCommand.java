package dev.lightdream.api.commands.commands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.utils.MessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ChoseLangCommand extends Command {
    public ChoseLangCommand(@NotNull IAPI api) {
        super(api, Collections.singletonList("choseLang"), "", "", true, false, "[lang]");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(sender);
            return;
        }

        Player player = (Player) sender;

        String lang = args.get(0);
        if (!api.getSettings().langs.contains(lang)) {
            api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().invalidLang));
            return;
        }

        api.getAPI().plugins.forEach(plugin -> plugin.setLang(player, lang));
        api.getMessageManager().sendMessage(sender, new MessageBuilder(api.getLang().langChanged));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, List<String> args) {
        return api.getSettings().langs;
    }
}
