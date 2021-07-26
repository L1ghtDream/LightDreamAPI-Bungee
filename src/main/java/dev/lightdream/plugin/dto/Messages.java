package dev.lightdream.plugin.dto;

import dev.lightdream.plugin.Main;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@NoArgsConstructor
public class Messages {

    public String prefix = "[" + Main.PROJECT_NAME + "] ";

    public String mustBeAPlayer = "You must be a player to use this command.";
    public String mustBeConsole = "You must be console to use this command.";
    public String noPermission = "You do not have the permission to use this command.";
    public String unknownCommand = "This is not a valid command.";

    //Leave empty for auto-generated
    public List<String> helpCommand = new ArrayList<>();

}
