package dev.lightdream.api.gui;

import com.google.gson.JsonElement;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.dto.GUIConfig;
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
    private final LightDreamPlugin plugin;
    private GUIConfig config;

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
            contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                List<String> functions = item.getFunctions();
                functions.forEach(function -> {
                    functionCall(player, function, item.getFunctionArgs(function));
                });
            }));
        });
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public abstract String parse(String raw, User user, String... values);

    public List<String> parse(List<String> raw, User user, String... values) {
        List<String> output = new ArrayList<>();

        raw.forEach(line -> output.add(parse(line, user, values)));

        return output;
    }

    public abstract void setConfig();

    public abstract InventoryProvider getProvider();

    public abstract void functionCall(Player player, String function, JsonElement args);

}
