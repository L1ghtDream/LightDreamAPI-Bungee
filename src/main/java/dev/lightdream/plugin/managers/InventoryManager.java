package dev.lightdream.plugin.managers;

import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.SmartInvsPlugin;
import fr.minuskube.inv.opener.InventoryOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class InventoryManager implements InventoryOpener {

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        fr.minuskube.inv.InventoryManager manager = SmartInvsPlugin.manager();
        Inventory handle = Bukkit.createInventory(player, inv.getRows() * inv.getColumns(), inv.getTitle());

        fill(handle, manager.getContents(player).get());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST;
    }

}
