package dev.lightdream.api.gui;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.files.dto.GUIConfig;
import dev.lightdream.api.files.dto.GUIItem;
import dev.lightdream.api.utils.ItemBuilder;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class GUI implements InventoryProvider {
    public final LightDreamPlugin plugin;
    public final GUIConfig config;

    public GUI(LightDreamPlugin plugin) {
        this.plugin = plugin;
        this.config = setConfig();
    }

    public SmartInventory getInventory() {
        return SmartInventory.builder()
                .id(config.id)
                .provider(getProvider())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .manager(plugin.inventoryManager)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));

        List<GUIItem> items = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        for (String key : config.items.keySet()) {
            items.add(config.items.get(key));
            keys.add(key);
        }
        for (int i = items.size() - 1; i >= 0; i--) {
            GUIItem item = items.get(i).clone();

            while (canAddItem(item, keys.get(i))) {
                item.item.displayName = parse(item.item.displayName, player);
                item.item.lore = parse(item.item.lore, player);
                item.item.headOwner = parse(item.item.displayName, player);

                item.args = parse(item.args, player);

                contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                    List<String> functions = item.getFunctions();
                    functions.forEach(function -> functionCall(player, function, item.getFunctionArgs(function)));
                }));
                if (!item.repeatedItem) {
                    break;
                }
            }

        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public abstract String parse(String raw, Player player);

    @SuppressWarnings("unchecked")
    public MessageBuilder parse(MessageBuilder raw, Player player) {
        if (raw.isList()) {
            return raw.setBase(parse((List<String>) raw.getBase(), player));
        } else {
            return raw.setBase(parse((String) raw.getBase(), player));
        }
    }


    @SuppressWarnings("UnusedAssignment")
    public GUIItem.GUIItemArgs parse(GUIItem.GUIItemArgs args, Player player) {
        return args.parse((function, arg) -> {
            function = parse(function, player);
            arg = parse(arg, player);
        });

    }

    public List<String> parse(List<String> raw, Player player) {
        List<String> output = new ArrayList<>();

        raw.forEach(line -> output.add(parse(line, player)));

        return output;
    }

    public abstract GUIConfig setConfig();

    public abstract InventoryProvider getProvider();

    public abstract void functionCall(Player player, String function, Object args);

    public abstract boolean canAddItem(GUIItem item, String key);

    public void open(Player player) {
        getInventory().open(player);
    }

    public void open(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        getInventory().open((Player) sender);
    }

}
