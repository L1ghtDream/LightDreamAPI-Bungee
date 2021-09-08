package dev.lightdream.api;

import dev.lightdream.api.commands.commands.ChoseLangCommand;
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

public final class API extends LightDreamPlugin {

    //Settings
    public static API instance;

    //Plugins
    public List<LightDreamPlugin> plugins = new ArrayList<>();

    public Economy economy = null;
    public Permission permission = null;

    //Managers
    public LangManager langManager;
    public MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;

        //Events
        new BalanceChangeEventRunnable(this);

        //Placeholders
        new PAPI(this).register();

        //Setups
        economy = setupEconomy();
        permission = setupPermissions();

        //Pre-init Managers
        messageManager = new MessageManager(this);

        //Register
        init("LightDreamAPI", "ld-api", "2.0");

        //Post-init managers
        langManager = new LangManager(this);
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

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public @NotNull String parsePapi(OfflinePlayer player, String identifier) {
        switch (identifier) {
            case "api_version":
                return version;
        }
        return "";
    }

    @Override
    public void loadBaseCommands() {
        baseCommands.add(new ChoseLangCommand(this));
    }

    @Override
    public MessageManager instantiateMessageManager() {
        return messageManager;
    }
}


/*
ro_ro=Lang{mustBeAPlayer='You must be a player to use this command.', mustBeConsole='You must be console to use this command.', noPermission='You do not have the permission to use this command.', unknownCommand='This is not a valid command.', invalidUser='This is not a valid user.', invalidNumber='This is not a valid number', version='%project_name% versiune %version%', invalidLang='This is not a valid lang', langChanged='Limba schimbata', helpCommand=''},
en_us=Lang{mustBeAPlayer='You must be a player to use this command.', mustBeConsole='You must be console to use this command.', noPermission='You do not have the permission to use this command.', unknownCommand='This is not a valid command.', invalidUser='This is not a valid user.', invalidNumber='This is not a valid number', version='%project_name% version %version%', invalidLang='This is not a valid lang', langChanged='Language changed', helpCommand=''}}
 */