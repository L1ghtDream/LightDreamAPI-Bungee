package dev.lightdream.api.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@NoArgsConstructor
@AllArgsConstructor
public class PluginLocation {

    public String world;
    public double x;
    public double y;
    public double z;

    public PluginLocation(Location location){
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public boolean smaller(PluginLocation pos) {
        if (pos.x >= x &&
                pos.y >= y &&
                pos.z >= z) {
            return false;
        }
        return true;
    }

    public boolean bigger(PluginLocation pos) {
        if (pos.x <= x &&
                pos.y <= y &&
                pos.z <= z) {
            return false;
        }
        return true;    }
}
