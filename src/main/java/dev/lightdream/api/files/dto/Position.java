package dev.lightdream.api.files.dto;

import com.sk89q.worldedit.Vector;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class Position implements Serializable, java.io.Serializable {

    public double x;
    public double y;
    public double z;

    @SuppressWarnings("unused")
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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
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

    @SuppressWarnings("unused")
    public void flip() {
        double tmp = this.x;
        this.x = this.z;
        this.z = tmp;
    }

    @SuppressWarnings("unused")
    public Position newFlip() {
        return new Position(this.z, y, this.x);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public Position deserialize(String serialized) {
        serialized = serialized.replace(getClass().getSimpleName(), "");
        serialized = serialized.replace("{", "");
        serialized = serialized.replace("}", "");
        List<String> coords = Arrays.asList(serialized.split(", "));
        if (coords.size() != 3) {
            throw new InvalidParameterException("The string does not match the constructor");
        }
        return new Position(Double.parseDouble(coords.get(0).replace("x=", "")), Double.parseDouble(coords.get(1).replace("y=", "")), Double.parseDouble(coords.get(2).replace("z=", "")));
    }

}
