package dev.lightdream.api.utils;

import dev.lightdream.api.files.dto.Item;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static @NotNull List<String> color(@NotNull List<String> list) {
        List<String> output = new ArrayList<>();
        list.forEach(line -> output.add(color(line)));
        return output;
    }

    public static @NotNull String color(@NotNull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static boolean checkExecute(double chance) {
        if (chance >= 100) {
            return true;
        }
        double result = Math.random() * 101 + 0;
        return result < chance;
    }

    public static int generateRandom(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static double generateRandom(double a, double b) {
        if (b < a) {
            return Math.random() * (a - b + 1) + b;
        }
        return Math.random() * (b - a + 1) + a;
    }

    public static void spawnFireworks(@NotNull Location location, int amount, @NotNull Color color, boolean flicker) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(flicker).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public static boolean compareItemToItemStack(ItemStack itemStack, Item item) {
        if (itemStack == null) {
            return false;
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (!itemStack.getType().equals(item.material.parseMaterial())) {
                return false;
            } else if (!meta.getDisplayName().equals(item.displayName)) {
                return false;
            } else if (!meta.getLore().equals(item.lore)) {
                return false;
            } else {
                for (String attribute : item.nbtTags.keySet()) {
                    Object value = item.nbtTags.get(attribute);
                    Object itemStackValue = NbtUtils.getNBT(itemStack, attribute);
                    if (!value.equals(itemStackValue)) {
                        return false;
                    }
                }
                return false;
            }
        }
    }

    public static SlotPos getSlotPosition(int slot) {
        slot++;
        int row = slot / 9; //6
        int column = slot % 9;
        if (column == 0) {
            column = 9;
            row--;
        }
        return new SlotPos(row, column - 1);
    }


}
