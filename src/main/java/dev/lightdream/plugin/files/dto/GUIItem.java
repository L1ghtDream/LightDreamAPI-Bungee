package dev.lightdream.plugin.files.dto;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class GUIItem {

    public Item item;
    public List<String> args = new ArrayList<>();


    public GUIItem(Item item, String... args) {
        this.item = item;
        this.args.addAll(Arrays.asList(args));
    }
}
