package dev.lightdream.api;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.files.config.Lang;
import dev.lightdream.api.files.config.SQLConfig;
import dev.lightdream.api.managers.DatabaseManager;
import dev.lightdream.api.managers.KeyDeserializerManager;
import dev.lightdream.api.managers.MessageManager;
import fr.minuskube.inv.InventoryManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public interface IAPI {

    JavaPlugin getPlugin();

    Economy getEconomy();

    Lang getLang();

    Config getSettings();

    SQLConfig getSQLConfig();

    MessageManager getMessageManager();

    DatabaseManager getDatabaseManager();

    File getDataFolder();

    Logger getLogger();

    String getProjectName();

    String getProjectID();

    String getProjectVersion();

    void setLang(Player player, String lang);

    void loadConfigs();

    InventoryManager getInventoryManager();

    List<SimpleModule> getSimpleModules();

    API getAPI();

    KeyDeserializerManager getKeyDeserializerManager();

}
