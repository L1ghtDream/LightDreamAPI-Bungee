package dev.lightdream.api;

import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public final class APIPlugin extends Plugin {

    public API api;

    @Override
    public void onEnable() {
        this.api = new API(this);
    }

    @Override
    public void onDisable() {
       api.disable();
    }

}
