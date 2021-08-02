package dev.lightdream.plugin.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InventoryUtils {

    public static void fillInventory(@NotNull Inventory inventory, @NotNull ItemStack fillItem, @NotNull List<Integer> positions) {
        positions.forEach(pos -> inventory.setItem(pos, NbtUtils.setNBT(fillItem, "gui_protect", true)));
    }

    public static void fillInventory(@NotNull Inventory inventory, @NotNull ItemStack fillItem) {
        for (int pos = 0; pos < inventory.getSize(); pos++) {
            inventory.setItem(pos, NbtUtils.setNBT(fillItem, "gui_protect", true));
        }
    }

}
