package dev.lightdream.api;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.lightdream.api.commands.Command;
import dev.lightdream.api.commands.commands.ReloadCommand;
import dev.lightdream.api.commands.commands.VersionCommand;
import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.files.config.JdaConfig;
import dev.lightdream.api.files.config.Lang;
import dev.lightdream.api.files.config.SQLConfig;
import dev.lightdream.api.managers.CommandManager;
import dev.lightdream.api.managers.DatabaseManager;
import dev.lightdream.api.managers.FileManager;
import dev.lightdream.api.managers.MessageManager;
import dev.lightdream.api.managers.local.LocalDatabaseManager;
import fr.minuskube.inv.InventoryManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class LightDreamPlugin extends JavaPlugin implements IAPI {

    //Settings
    public String projectName = "Undefined";
    public String projectID = "Undefined";
    public String projectVersion = "Undefined";

    //Config
    public SQLConfig sqlConfig;
    public JdaConfig baseJdaConfig;
    public Config baseConfig;
    public Lang baseLang;

    //Managers
    public Economy economy;
    public Permission permission;
    public FileManager fileManager;
    public LocalDatabaseManager databaseManager;
    public InventoryManager inventoryManager;
    public MessageManager messageManager;

    //Bot
    public JDA bot;

    //API
    public API api;
    
    //Commands
    public List<Command> baseCommands = new ArrayList<>();

    @SuppressWarnings("unused")
    @SneakyThrows
    public void init(String projectName, String projectID, String version) {
        if (API.instance == null) {
            api = new API(this);
        } else {
            api = API.instance;
        }

        this.projectName = projectName;
        this.projectID = projectID;
        this.projectVersion = version;

        //Files
        fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();

        //Managers
        registerLangManager();
        this.economy = api.economy;
        this.permission = api.permission;
        this.databaseManager = new LocalDatabaseManager(this);
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();
        this.messageManager = instantiateMessageManager();

        //Commands
        baseCommands.add(new ReloadCommand(this));
        baseCommands.add(new VersionCommand(this));
        loadBaseCommands();
        new CommandManager(this, projectID, baseCommands);

        //Bot
        if (baseJdaConfig != null) {
            if (baseJdaConfig.useJDA) {
                bot = JDABuilder.createDefault(baseJdaConfig.botToken).build();
            }
        }


        //Register
        API.instance.plugins.add(this);
        getLogger().info(ChatColor.GREEN + projectName + "(by github.com/L1ghtDream) has been enabled");
    }

    @Override
    public void onDisable() {
        api.onDisable();
    }

    public abstract @NotNull String parsePapi(OfflinePlayer player, String identifier);

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        baseConfig = fileManager.load(Config.class);
        baseJdaConfig = fileManager.load(JdaConfig.class);
        baseLang = fileManager.load(Lang.class, fileManager.getFile(baseConfig.baseLang));
    }

    public abstract void loadBaseCommands();

    public abstract MessageManager instantiateMessageManager();

    public abstract void registerLangManager();

    @SuppressWarnings("unused")
    public HashMap<String, Object> getLangs() {
        HashMap<String, Object> langs = new HashMap<>();

        baseConfig.langs.forEach(lang -> {
            Lang l = fileManager.load(Lang.class, fileManager.getFile(lang));
            langs.put(lang, l);
        });

        return langs;
    }

    @Override
    public JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public Economy getEconomy() {
        return economy;
    }

    @Override
    public Lang getLang() {
        return baseLang;
    }

    @Override
    public SQLConfig getSQLConfig() {
        return sqlConfig;
    }

    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Config getSettings() {
        return baseConfig;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public String getProjectID() {
        return projectID;
    }

    @Override
    public String getProjectVersion() {
        return projectVersion;
    }

    @Override
    public abstract void setLang(Player player, String lang);

    @Override
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    @Override
    public List<SimpleModule> getSimpleModules() {
        return new ArrayList<>();
    }

    @Override
    public API getAPI() {
        return api;
    }
}
