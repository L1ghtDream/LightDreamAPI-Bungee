package dev.lightdream.api.utils;

import dev.lightdream.api.dto.Randomizable;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
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

    @SuppressWarnings("unused")
    public static boolean checkExecute(double chance) {
        if (chance >= 100) {
            return true;
        }
        double result = Math.random() * 101 + 0;
        return result < chance;
    }

    @SuppressWarnings("unused")
    public static int generateRandom(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    @SuppressWarnings("unused")
    public static double generateRandom(double a, double b) {
        if (b < a) {
            return Math.random() * (a - b + 1) + b;
        }
        return Math.random() * (b - a + 1) + a;
    }


    public static int getTotalExperience(int level) {
        int xp = 0;

        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

    @SuppressWarnings("unused")
    public static int getTotalChances(List<Randomizable> objects) {
        int output = 0;
        for (Randomizable object : objects) {
            output += object.getChance();
        }
        return output;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public static <T> T getRandom(List<T> objects) {
        if (objects.size() == 0) {
            return null;
        }

        if (!(objects.get(0) instanceof Randomizable)) {
            return null;
        }
        int chances = getTotalChances((List<Randomizable>) objects); //200
        int index = -1;
        int rnd = generateRandom(0, chances); //90

        do {
            if (index >= objects.size()) { // 0>=2(false)
                break;
            }
            index++;
            rnd -= ((Randomizable) objects.get(index)).getChance(); // rnd=-10
        } while (rnd > 0);

        if (index >= objects.size()) {
            index = objects.size() - 1;
        }

        return objects.get(index);
    }

    public static double getRam() {
        return ((double) (Runtime.getRuntime().totalMemory() / 1024) / 1024) - ((double) (Runtime.getRuntime().freeMemory() / 1024) / 1024);
    }

    public static double getCpuLoad() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

            if (list.isEmpty())
                return Double.NaN;

            Attribute att = (Attribute) list.get(0);
            Double value = (Double) att.getValue();

            if (value == -1.0)
                return Double.NaN;
            return ((int) (value * 1000) / 10.0);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public static String getJava() {
        return String.format("Java Version: %s %s. \n", System.getProperty("java.vendor"), System.getProperty("java.version"));
    }


}
