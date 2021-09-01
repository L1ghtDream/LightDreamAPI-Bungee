package dev.lightdream.api.gui;

import com.google.gson.JsonElement;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.files.dto.GUIConfig;
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

        config.items.forEach((key, item) -> {
            Item builder = item.item.clone();

            if (canAddItem(builder)) {
                builder.displayName = parse(builder.displayName, player);
                builder.lore = parse(builder.lore, player);
                builder.headOwner = parse(builder.displayName, player);

                contents.set(Utils.getSlotPosition(builder.slot), ClickableItem.of(ItemBuilder.makeItem(builder), e -> {
                    List<String> functions = item.getFunctions();
                    functions.forEach(function -> {
                        functionCall(player, function, item.getFunctionArgs(function));
                    });
                }));
            }

        });
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

    public abstract boolean canAddItem(Item item);

}
