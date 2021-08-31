package dev.lightdream.api.files.dto;

import com.sk89q.worldedit.Vector;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;


@NoArgsConstructor
@AllArgsConstructor
public class PluginLocation {

    public String world;
    public double x;
    public double y;
    public double z;
    public double rotationX;
    public double rotationY;

    public PluginLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.rotationX = location.getYaw();
        this.rotationY = location.getPitch();
    }

    public PluginLocation(String world, double x,double y, double z){
        this.world = world;
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public boolean smaller(PluginLocation pos) {
        return pos.x >= x &&
                pos.y >= y &&
                pos.z >= z;
    }

    public boolean bigger(PluginLocation pos) {
        return pos.x <= x &&
                pos.y <= y &&
                pos.z <= z;
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public Block getBlock() {
        return Bukkit.getWorld(world).getBlockAt(toLocation());
    }

    public PluginLocation clone() {
        return new PluginLocation(world, x, y, z, rotationX, rotationY);
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
