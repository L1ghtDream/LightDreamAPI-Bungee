package dev.lightdream.api;

import dev.lightdream.api.commands.commands.ChoseLangCommand;
import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.files.config.Lang;
import dev.lightdream.api.files.config.SQLConfig;
import dev.lightdream.api.managers.BalanceChangeEventRunnable;
import dev.lightdream.api.managers.LangManager;
import dev.lightdream.api.managers.MessageManager;
import dev.lightdream.api.managers.PAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class API {

    //Settings
    public static API instance;
    public final LightDreamPlugin plugin;
    //Plugins
    public List<LightDreamPlugin> plugins = new ArrayList<>();

    public Economy economy = null;
    public Permission permission = null;

    //Managers
    public LangManager langManager;
    public MessageManager messageManager;

    public API(LightDreamPlugin plugin) {
        this.plugin = plugin;
        init();
    }

    public void init() {
        instance = this;

        //Events
        new BalanceChangeEventRunnable(plugin);

        //Placeholders
        new PAPI(this).register();

        //Setups
        economy = setupEconomy();
        permission = setupPermissions();

        //Pre-init Managers
        messageManager = new MessageManager(plugin);

        //Register
        plugin.init("LightDreamAPI", "ld-api", "2.26", this);
    }

    public void onDisable() {
        plugin.databaseManager.save();
    }

    private Economy setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        return rsp.getProvider();
    }

    private Permission setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        return rsp.getProvider();
    }

    @SuppressWarnings({"SwitchStatementWithTooFewBranches", "unused"})
    public @NotNull String parsePapi(OfflinePlayer player, String identifier) {
        switch (identifier) {
            case "api_version":
                return plugin.version;
        }
        return "";
    }

    public void loadBaseCommands() {
        plugin.baseCommands.add(new ChoseLangCommand(plugin));
    }

    public MessageManager instantiateMessageManager() {
        return messageManager;
    }

    public void registerLangManager() {
        langManager = new LangManager(plugin, plugin.getLangs());
    }

    public void loadConfigs() {
        plugin.sqlConfig = plugin.fileManager.load(SQLConfig.class, plugin.fileManager.getFile("LightDreamAPI", SQLConfig.class.getName()));
        plugin.baseConfig = plugin.fileManager.load(Config.class, plugin.fileManager.getFile("LightDreamAPI", Config.class.getName()));
        //plugin.baseJdaConfig = plugin.fileManager.load(JdaConfig.class, plugin.fileManager.getFile("LightDreamAPI", JdaConfig.class.getName()));
        plugin.baseLang = plugin.fileManager.load(Lang.class, plugin.fileManager.getFile("LightDreamAPI", plugin.baseConfig.baseLang));
    }
}
