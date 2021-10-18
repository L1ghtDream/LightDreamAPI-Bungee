package dev.lightdream.api.dto;

import dev.lightdream.api.databases.User;
import dev.lightdream.api.enums.RewardType;
import dev.lightdream.api.utils.ItemBuilder;
import org.bukkit.Bukkit;

@SuppressWarnings("unused")
public class Reward {

    public RewardType type;
    public Item item;
    public String command;
    public int money;

    public Reward(Item item) {
        this.item = item;
        this.type = RewardType.ITEM;
    }

    public Reward(String command) {
        this.command = command;
        this.type = RewardType.COMMAND;
    }

    public Reward(int money) {
        this.money = money;
        this.type = RewardType.MONEY;
    }

    @SuppressWarnings("ConstantConditions")
    public void win(User user) {
        switch (this.type) {
            case ITEM:
                if(!user.isOnline()){
                    return;
                }
                user.getPlayer().getInventory().addItem(ItemBuilder.makeItem(item));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                break;
            case MONEY:
                user.addMoney(money);
                break;
        }
    }

}
