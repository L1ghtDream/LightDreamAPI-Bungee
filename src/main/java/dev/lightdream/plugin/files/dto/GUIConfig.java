package dev.lightdream.plugin.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
public class GUIConfig {

    public String id;
    public String type;
    public String title;
    public int rows;
    public int columns;
    public Item fillItem;
    public HashMap<String, GUIItem> items;

}