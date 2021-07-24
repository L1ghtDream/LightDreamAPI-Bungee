package dev.lightdream.plugin;

import dev.lightdream.plugin.commands.Command;
import dev.lightdream.plugin.dto.Config;
import dev.lightdream.plugin.dto.Messages;
import dev.lightdream.plugin.dto.SQL;
import dev.lightdream.plugin.managers.*;
import dev.lightdream.plugin.utils.Persist;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {

    //Settings
    public final static String PROJECT_NAME = "SpigotTemplate";
    private List<Command> commands;

    //Managers
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private EventManager eventManager;
    private InventoryManager inventoryManager;
    private MessageManager messageManager;
    private PAPI papi;

    //Utils
    private Persist persist;

    //DTO
    private Config config;
    private Messages messages;
    private SQL sql;

    @Override
    public void onEnable() {

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

        //Utils
        persist = new Persist(this, Persist.PersistType.YAML);

        //Config
        config = persist.load(Config.class);
        messages = persist.load(Messages.class);
        sql = persist.load(SQL.class);

        //Commands
    }

    @Override
    public void onDisable() {
        //Save files

        //Save to db
    }
}
