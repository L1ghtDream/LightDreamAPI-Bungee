package dev.lightdream.api;

import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.managers.BalanceChangeEventRunnable;
import dev.lightdream.api.managers.PAPI;
import dev.lightdream.api.utils.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class API extends LightDreamPlugin {

    //Settings
    public static API instance;
    public Config config;

    //Plugins
    public List<LightDreamPlugin> plugins = new ArrayList<>();

    public Economy economy = null;
    public Permission permission = null;

    @Override
    public void onEnable() {
        instance = this;

        //Managers
        MessageUtils.init(this);
        new BalanceChangeEventRunnable(this);
        new PAPI(this).register();
        economy = setupEconomy();
        permission = setupPermissions();

        //Register
        init("LightDreamAPI", "ld-api", "1.56");
    }

    @Override
    public void onDisable() {
        databaseManager.save();
    }


    private Economy setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp.getProvider();
    }

    private Permission setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        return rsp.getProvider();
    }

    @Override
    public @NotNull String parsePapi(OfflinePlayer player, String identifier) {
        switch (identifier) {
            case "api_version":
                return version;
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
