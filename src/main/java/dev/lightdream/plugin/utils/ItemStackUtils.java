package dev.lightdream.plugin.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import dev.lightdream.plugin.dto.Item;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"ConstantConditions", "unused"})
public class ItemStackUtils {

    private static final boolean supports = XMaterial.supports(14);

    public static ItemStack makeItem(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem();
        if (item == null) return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.setLore(Utils.color(lore));
        m.setDisplayName(Utils.color(name));
        item.setItemMeta(m);
        return item;
    }

    public static @NotNull ItemStack makeItem(@NotNull Item item) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, item.displayName, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headData != null) {
                NBTItem nbtItem = new NBTItem(itemstack);
                NBTCompound skull = nbtItem.addCompound("SkullOwner");
                if (supports) {
                    skull.setUUID("Id", UUID.randomUUID());
                } else {
                    skull.setString("Id", UUID.randomUUID().toString());
                }
                NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
                texture.setString("Value", item.headData);
                return nbtItem.getItem();
            } else if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            // Create a fallback item
            return makeItem(XMaterial.STONE, item.amount, item.displayName, item.lore);
        }
    }

    public static @NotNull String serialize(@NotNull ItemStack itemStack) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            bukkitObjectOutputStream.writeObject(itemStack);
            bukkitObjectOutputStream.flush();

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static @NotNull ItemStack deserialize(@NotNull String string) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(string));
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
            return (ItemStack) bukkitObjectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return XMaterial.AIR.parseItem();
        }
    }

    public static @NotNull String[] playerInventoryToBase64(@NotNull PlayerInventory playerInventory) throws IllegalStateException {
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(Arrays.asList(playerInventory.getArmorContents()));

        return new String[]{content, armor};
    }

    public static @NotNull String itemStackArrayToBase64(@NotNull List<ItemStack> items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.size());

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static @NotNull String toBase64(@NotNull Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static @NotNull Inventory fromBase64(@NotNull String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static @NotNull List<ItemStack> itemStackArrayFromBase64(@NotNull String data) throws IOException {
        if (data.equals("")) {
            return new ArrayList<>();
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            List<ItemStack> items = new ArrayList<>();
            int size = dataInput.readInt();

            for (int i = 0; i < size; i++) {
                items.add((ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
