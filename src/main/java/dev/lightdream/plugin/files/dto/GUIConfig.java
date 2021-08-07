package dev.lightdream.plugin.files.dto;

import dev.lightdream.plugin.files.dto.Item;
import dev.lightdream.plugin.files.dto.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public abstract class GUIConfig {

    public String id;
    public String type = "CHEST";
    public String title;
    public int rows = 6;
    public int columns = 9;
    public Item fillItem = new Item(XMaterial.GLASS_PANE, 1, "", new ArrayList<>());
    public List<GUIItem> items;


}