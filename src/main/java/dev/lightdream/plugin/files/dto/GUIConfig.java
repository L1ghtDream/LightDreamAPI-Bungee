package dev.lightdream.plugin.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
public class GUIConfig {

    public String id;
    public String type = "CHEST";
    public String title;
    public int rows = 6;
    public int columns = 9;
    public Item fillItem = new Item(XMaterial.GLASS_PANE, 1, "", new ArrayList<>());
    public HashMap<String, GUIItem> items;

}