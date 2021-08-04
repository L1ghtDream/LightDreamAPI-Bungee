package dev.lightdream.plugin.files.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GUIConfig {

    public String id;
    public String type = "CHEST";
    public String title;
    public int rows = 6;
    public int columns = 9;
}