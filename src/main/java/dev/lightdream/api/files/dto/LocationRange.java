package dev.lightdream.api.files.dto;

import dev.lightdream.api.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LocationRange {

    public PluginLocation pos1;
    public PluginLocation pos2;

    public boolean check(PluginLocation pos) {
        PluginLocation min = Utils.minPluginLocation(pos1, pos2);
        PluginLocation max = Utils.maxPluginLocation(pos1, pos2);
        System.out.println("min.bigger(pos) = " + min.bigger(pos));
        System.out.println("min.smaller(pos) = " + min.smaller(pos));
        System.out.println("max.bigger(pos) = " + max.bigger(pos));
        System.out.println("max.smaller(pos) = " + max.smaller(pos));

        return !min.bigger(pos) ||
                !max.smaller(pos);
    }

}
