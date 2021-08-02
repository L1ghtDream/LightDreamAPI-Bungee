package dev.lightdream.plugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
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

}
