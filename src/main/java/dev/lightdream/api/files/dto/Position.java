package dev.lightdream.api.files.dto;

import com.sk89q.worldedit.Vector;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class Position {

    public double x;
    public double y;
    public double z;

    public PluginLocation getPluginLocation(String world) {
        return new PluginLocation(world, x, y, z);
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

    public Position clone() {
        return new Position(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public void flip() {
        double tmp = this.x;
        this.x = this.z;
        this.z = tmp;
    }

    public Position newFlip() {
        return new Position(this.z, y, this.x);
    }
}
