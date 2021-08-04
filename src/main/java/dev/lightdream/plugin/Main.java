package dev.lightdream.plugin;

import dev.lightdream.plugin.commands.Command;
import dev.lightdream.plugin.commands.ReloadCommand;
import dev.lightdream.plugin.files.config.Config;
import dev.lightdream.plugin.files.config.GUIs;
import dev.lightdream.plugin.files.config.Messages;
import dev.lightdream.plugin.files.config.SQL;
import dev.lightdream.plugin.managers.*;
import dev.lightdream.plugin.utils.init.DatabaseUtils;
import dev.lightdream.plugin.utils.init.MessageUtils;
import dev.lightdream.plugin.utils.init.PlaceholderUtils;
import dev.lightdream.plugin.utils.init.WorldEditUtils;
import fr.minuskube.inv.SmartInvsPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {

    public static Main instance;

    //Settings
    public final static String PROJECT_NAME = "SpigotTemplate";
    public final static String PROJECT_ID = "st";
    private final List<Command> commands = new ArrayList<>();

    //Managers
    private CommandManager commandManager;
    private EventManager eventManager;
    private SchedulerManager schedulerManager;

    //Utils
    private FileManager fileManager;

    //DTO
    private Config settings;
    private Messages messages;
    private GUIs GUIs;
    private SQL sql;

    @Override
    public void onEnable() {
        instance = this;

        fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();

        //Utils
        MessageUtils.init(this);
        PlaceholderUtils.init(this);
        WorldEditUtils.init(this);
        try {
            DatabaseUtils.init(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SmartInvsPlugin.manager().registerOpeners(new InventoryManager());

        //Commands
        commands.add(new ReloadCommand(this));

        //Managers
        commandManager = new CommandManager(this, PROJECT_ID.toLowerCase());
        eventManager = new EventManager(this);
        schedulerManager = new SchedulerManager(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI(this).register();
        } else {
            this.getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        //Save files

        //Save to db
        DatabaseUtils.saveUsers();
    }

    public void loadConfigs() {
        settings = fileManager.load(Config.class);
        messages = fileManager.load(Messages.class);
        sql = fileManager.load(SQL.class);
        GUIs = fileManager.load(GUIs.class);
    }
}
