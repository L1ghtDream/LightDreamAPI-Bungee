package dev.lightdream.api;

import dev.lightdream.api.commands.Command;
import dev.lightdream.api.commands.commands.ReloadCommand;
import dev.lightdream.api.commands.commands.VersionCommand;
import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.files.config.JdaConfig;
import dev.lightdream.api.files.config.Lang;
import dev.lightdream.api.files.config.SQLConfig;
import dev.lightdream.api.managers.CommandManager;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class LightDreamPlugin extends JavaPlugin {

    //Settings
    public String projectName = "Undefined";
    public String projectID = "Undefined";
    public String version = "Undefined";

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

    //Commands
    public List<Command> baseCommands = new ArrayList<>();

    //Bot
    public JDA bot;

    @SneakyThrows
    public void init(String projectName, String projectID, String version) {
        this.projectName = projectName;
        this.projectID = projectID;
        this.version = version;

        //Files
        fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();

        //Managers
        registerLangManager();
        this.economy = API.instance.economy;
        this.permission = API.instance.permission;
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

    public abstract @NotNull String parsePapi(OfflinePlayer player, String identifier);

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        baseConfig = fileManager.load(Config.class);
        baseJdaConfig = fileManager.load(JdaConfig.class);
        baseLang = fileManager.load(Lang.class, fileManager.getFile(baseConfig));
    }

    public abstract void loadBaseCommands();

    public abstract MessageManager instantiateMessageManager();

    public abstract void registerLangManager();

    public HashMap<String, Object> getLangs() {
        HashMap<String, Object> langs = new HashMap<>();

        baseConfig.langs.forEach(lang -> {
            Lang l = fileManager.load(Lang.class, fileManager.getFile(lang));
            langs.put(lang, l);
        });

        return langs;
    }

}
