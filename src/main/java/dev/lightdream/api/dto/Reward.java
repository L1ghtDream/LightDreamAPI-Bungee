package dev.lightdream.api.dto;

import dev.lightdream.api.API;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.enums.RewardType;
import dev.lightdream.api.utils.MessageBuilder;

import java.util.HashMap;

@SuppressWarnings("unused")
public class Reward {

    public RewardType type;
    public String command;

    public Reward(String command) {
        this.command = command;
        this.type = RewardType.COMMAND;
    }

    @SuppressWarnings({"ConstantConditions", "SwitchStatementWithTooFewBranches"})
    public void win(User user) {
        switch (this.type) {
            case COMMAND:
                API.instance.getPlugin().getProxy().getPluginManager().dispatchCommand(API.instance.getPlugin().getProxy().getConsole(), new MessageBuilder(command).addPlaceholders(new HashMap<String, String>() {{
                    put("player_name", user.name);
                }}).parseString());
                break;
        }
    }

}
