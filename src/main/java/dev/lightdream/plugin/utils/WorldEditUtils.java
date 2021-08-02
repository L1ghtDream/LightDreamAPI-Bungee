package dev.lightdream.plugin.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.lightdream.plugin.Main;
import dev.lightdream.plugin.dto.PluginLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorldEditUtils {

    public static BlockArrayClipboard copy(PluginLocation pos1, PluginLocation pos2) {
        World world = Bukkit.getWorld(pos1.world);
        CuboidRegion region = new CuboidRegion(BukkitAdapter.adapt(world), pos1.toBlockVector3(), pos2.toBlockVector3());
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, new PluginLocation(pos1.world, Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), Math.min(pos1.z, pos2.z)).toBlockVector3()
            );
            try {
                Operations.complete(forwardExtentCopy);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }

        return clipboard;
    }

    public static void save(String subFolder, String name, BlockArrayClipboard clipboard, Main plugin) {
        new File(plugin.getDataFolder().getPath() + "/" + subFolder).mkdirs();
        File file = new File(plugin.getDataFolder().getPath() + "/" + subFolder + "/" + name + ".schem");
        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void paste(PluginLocation pos1, PluginLocation pos2, Clipboard clipboard) {
        World world = Bukkit.getWorld(pos1.world);
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(world))) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(new PluginLocation(pos1.world, Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y), Math.min(pos1.z, pos2.z)).toBlockVector3())
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static Clipboard load(String subFolder, String name, Main plugin) {
        Clipboard clipboard = null;
        File file = new File(plugin.getDataFolder().getPath() + "/" + subFolder + "/" + name + ".schem");

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return clipboard;
    }

}
