package dev.lightdream.plugin.files.config;

import dev.lightdream.plugin.files.dto.GUIConfig;
import dev.lightdream.plugin.files.dto.Item;
import dev.lightdream.plugin.files.dto.XMaterial;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@NoArgsConstructor
public class GUIs {

    public GUIConfig sampleGUIConfig = new GUIConfig("id", "CHEST", "title", 6, 9, new Item(XMaterial.GLASS_PANE, 1, "", new ArrayList<>(), new HashMap<>()), new HashMap<>());

}
