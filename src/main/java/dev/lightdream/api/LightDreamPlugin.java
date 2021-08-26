package dev.lightdream.api;

import dev.lightdream.api.commands.Command;
import dev.lightdream.api.commands.commands.VersionCommand;
import dev.lightdream.api.files.config.Config;
import dev.lightdream.api.files.config.JdaConfig;
import dev.lightdream.api.files.config.Lang;
import dev.lightdream.api.files.config.SQLConfig;
import dev.lightdream.api.files.config.base.BaseConfig;
import dev.lightdream.api.files.config.base.BaseJdaConfig;
import dev.lightdream.api.managers.CommandManager;
import dev.lightdream.api.managers.FileManager;
import dev.lightdream.api.managers.local.LocalDatabaseManager;
import dev.lightdream.api.utils.LangUtils;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    public FileManager fileManager;
    public LocalDatabaseManager databaseManager;

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
        sqlConfig = fileManager.load(SQLConfig.class);
        baseConfig = fileManager.load(BaseConfig.class);
        baseJdaConfig = fileManager.load(BaseJdaConfig.class);
        baseLang = (Lang) fileManager.load(LangUtils.getLang("dev.lightdream.api.files.config.base.lang.base_" + baseConfig.lang));
        loadConfigs();

        //Managers
        this.economy = API.instance.economy;
        databaseManager = new LocalDatabaseManager(this);

        //Commands
        baseCommands.add(new VersionCommand(this));
        new CommandManager(this, projectID, baseCommands);

        //Bot
        if (baseJdaConfig.useJDA) {
            bot = JDABuilder.createDefault(baseJdaConfig.botToken).build();
        }

        //Register
        API.instance.plugins.add(this);
        getLogger().info(ChatColor.GREEN + projectName + "(by github.com/L1ghtDream) has been enabled");
    }

    public abstract @NotNull String parsePapi(OfflinePlayer player, String identifier);

    public abstract void loadConfigs();

}
