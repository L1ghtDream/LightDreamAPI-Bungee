package dev.lightdream.api;

import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.managers.BalanceChangeEventRunnable;
import dev.lightdream.api.managers.PAPI;
import dev.lightdream.api.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class API extends LightDreamPlugin {

    //Settings
    public final static String PROJECT_NAME = "LightDreamAPI";
    public final static String PROJECT_ID = "ld-api";
    public final static String VERSION = "1.31";
    public static API instance;
    public Config config;

    //Plugins
    public List<LightDreamPlugin> plugins = new ArrayList<>();

    public Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;

        //Managers
        MessageUtils.init(this);
        new BalanceChangeEventRunnable(this);
        new PAPI(this).register();
        economy = setupEconomy();

        //Register
        init(PROJECT_NAME, PROJECT_ID, VERSION);
    }

    @Override
    public void onDisable() {
        databaseManager.save();
    }


    private Economy setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp.getProvider();
    }

    @Override
    public @NotNull String parsePapi(OfflinePlayer player, String identifier) {
        switch (identifier) {
            case "api_version":
                return VERSION;
        }
        return "";
    }

    @Override
    public void loadConfigs() {
    }

    @Override
    public void loadBaseCommands() {
    }
}
