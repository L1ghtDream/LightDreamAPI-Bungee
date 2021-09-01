package dev.lightdream.api.gui;

import com.google.gson.JsonElement;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.files.dto.GUIConfig;
import dev.lightdream.api.files.dto.GUIItem;
import dev.lightdream.api.files.dto.Item;
import dev.lightdream.api.utils.ItemBuilder;
import dev.lightdream.api.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public abstract class GUI implements InventoryProvider {
    public final LightDreamPlugin plugin;
    public GUIConfig config;

    public GUI(LightDreamPlugin plugin) {
        this.plugin = plugin;
        setConfig();
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
            GUIItem item = items.get(i).deepClone();

            if (canAddItem(item.item, keys.get(i))) {
                item.item.displayName = parse(item.item.displayName, player);
                item.item.lore = parse(item.item.lore, player);
                item.item.headOwner = parse(item.item.displayName, player);

                item.args = parse(item.args, player);

                contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                    List<String> functions = item.getFunctions();
                    functions.forEach(function -> {
                        functionCall(player, function, item.getFunctionArgs(function));
                    });
                }));
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public abstract String parse(String raw, Player player);

    public List<String> parse(List<String> raw, Player player) {
        List<String> output = new ArrayList<>();

        raw.forEach(line -> output.add(parse(line, player)));

        return output;
    }

    public abstract void setConfig();

    public abstract InventoryProvider getProvider();

    public abstract void functionCall(Player player, String function, JsonElement args);

    public abstract boolean canAddItem(Item item, String key);

}
