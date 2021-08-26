package dev.lightdream.api.files.config;

import java.util.ArrayList;
import java.util.List;

public abstract class Lang {

    public String mustBeAPlayer = "You must be a player to use this command.";
    public String mustBeConsole = "You must be console to use this command.";
    public String noPermission = "You do not have the permission to use this command.";
    public String unknownCommand = "This is not a valid command.";
    public String invalidUser = "This is not a valid user.";
    public String invalidNumber = "This is not a valid number";
    public String version = "%project_name% version %version%";

    public List<String> helpCommand = new ArrayList<>();

}
