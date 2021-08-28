package dev.lightdream.api.files.dto;

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

    public boolean smaller(PluginLocation pos) {
        if (pos.x > x ||
                pos.y > y ||
                pos.z > z) {
            return false;
        }
        return true;
    }

    public boolean bigger(PluginLocation pos) {
        return !smaller(pos);
    }
}
