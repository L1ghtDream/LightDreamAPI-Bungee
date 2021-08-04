package dev.lightdream.plugin.gui;

import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.files.dto.GUIConfig;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

@SuppressWarnings("unused")
public class SampleGUI implements InventoryProvider {

    //https://minuskube.gitbook.io/smartinvs/guide/content_provider

    public static SmartInventory getInventory() {
        GUIConfig config = Main.instance.getGUIs().sampleGUIConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new SampleGUI())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}
