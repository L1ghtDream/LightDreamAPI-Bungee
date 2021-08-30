package dev.lightdream.api.files.dto;

import dev.lightdream.api.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class LocationRange {

    public PluginLocation pos1;
    public PluginLocation pos2;

    public boolean check(PluginLocation pos) {
        PluginLocation min = Utils.minPluginLocation(pos1, pos2);
        PluginLocation max = Utils.maxPluginLocation(pos1, pos2);

        if (!min.world.equals(max.world)) {
            return false;
        }

        return min.smaller(pos) && max.bigger(pos);
    }

    public List<Block> getBlocks() {
        List<Block> output = new ArrayList<>();
        PluginLocation min = Utils.minPluginLocation(pos1, pos2);
        PluginLocation max = Utils.maxPluginLocation(pos1, pos2);

        if (!min.world.equals(max.world)) {
            return output;
        }

        World world = Bukkit.getWorld(min.world);

        for (int x = (int) min.x; x <= max.x; x++) {
            for (int y = (int) min.y; y <= max.y; y++) {
                for (int z = (int) min.z; z <= max.z; z++) {
                    output.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return output;
    }

}
