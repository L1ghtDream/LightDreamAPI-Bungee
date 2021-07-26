package dev.lightdream.plugin;

import dev.lightdream.plugin.commands.Command;
import dev.lightdream.plugin.commands.ReloadCommand;
import dev.lightdream.plugin.dto.Config;
import dev.lightdream.plugin.dto.Messages;
import dev.lightdream.plugin.dto.SQL;
import dev.lightdream.plugin.managers.*;
import dev.lightdream.plugin.managers.FileManager;
import lombok.Getter;
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
    private PAPI papi;

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
        commandManager = new CommandManager(this, PROJECT_NAME.toLowerCase());
        try {
            databaseManager = new DatabaseManager(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        eventManager = new EventManager(this);
        inventoryManager = new InventoryManager(this);
        messageManager = new MessageManager(this);
        papi = new PAPI(this);
    }

    @Override
    public void onDisable() {
        //Save files

        //Save to db
    }

    public void loadConfigs(){
        settings = fileManager.load(Config.class);
        messages = fileManager.load(Messages.class);
        sql = fileManager.load(SQL.class);
    }
}
