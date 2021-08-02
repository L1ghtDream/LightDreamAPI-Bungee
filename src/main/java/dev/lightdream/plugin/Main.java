package dev.lightdream.plugin;

import dev.lightdream.plugin.commands.Command;
import dev.lightdream.plugin.commands.ReloadCommand;
import dev.lightdream.plugin.dto.Config;
import dev.lightdream.plugin.dto.Messages;
import dev.lightdream.plugin.dto.SQL;
import dev.lightdream.plugin.managers.*;
import dev.lightdream.plugin.managers.FileManager;
import dev.lightdream.plugin.utils.WorldEditUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {
    //Settings
    public final static String PROJECT_NAME = "SpigotTemplate";
    public final static String PROJECT_ID = "st";
    private final List<Command> commands = new ArrayList<>();

    //Managers
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private EventManager eventManager;
    private InventoryManager inventoryManager;
    private MessageManager messageManager;
    private SchedulerManager schedulerManager;

    //Utils
    private FileManager fileManager;

    //DTO
    private Config settings;
    private Messages messages;
    private SQL sql;

    @Override
    public void onEnable() {

        //Utils
        fileManager = new FileManager(this, FileManager.PersistType.YAML);

        //Config
        loadConfigs();

        //Commands
        commands.add(new ReloadCommand(this));

        //Managers
        commandManager = new CommandManager(this, PROJECT_ID.toLowerCase());
        try {
            databaseManager = new DatabaseManager(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        eventManager = new EventManager(this);
        inventoryManager = new InventoryManager(this);
        messageManager = new MessageManager(this);
        schedulerManager = new SchedulerManager(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI(this).register();
        } else {
            System.out.println("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        //Save files

        //Save to db
        databaseManager.saveUsers();
    }

    public void loadConfigs(){
        settings = fileManager.load(Config.class);
        messages = fileManager.load(Messages.class);
        sql = fileManager.load(SQL.class);
    }
}
