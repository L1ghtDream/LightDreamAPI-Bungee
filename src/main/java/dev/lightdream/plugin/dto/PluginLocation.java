package dev.lightdream.plugin.dto;

import com.sk89q.worldedit.math.BlockVector3;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@NoArgsConstructor
@AllArgsConstructor
public class PluginLocation {

    public String world;
    public float x;
    public float y;
    public float z;

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public BlockVector3 toBlockVector3() {
        return BlockVector3.at(x, y, z);
    }

    @Override
    public String toString() {
        return "PluginLocation{" +
                "world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
