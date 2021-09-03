package dev.lightdream.api.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
public class PluginLocation extends Position {

    public String world;
    public double x;
    public double y;
    public double z;
    public float rotationX;
    public float rotationY;

    public PluginLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.rotationX = location.getYaw();
        this.rotationY = location.getPitch();
    }

    public PluginLocation(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, rotationX, rotationY);
    }

    public Block getBlock() {
        return Bukkit.getWorld(world).getBlockAt(toLocation());
    }

    public void setBlock(Material material) {
        getBlock().setType(material);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginLocation that = (PluginLocation) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0 && Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    public void offset(Position position) {
        this.x += position.x;
        this.y += position.y;
        this.z += position.z;
    }

    public PluginLocation newOffset(Position position) {
        return new PluginLocation(world, x + position.x, y + position.y, z + position.z);
    }
}
