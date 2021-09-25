package dev.lightdream.api.gui;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.dto.GUIConfig;
import dev.lightdream.api.dto.GUIItem;
import dev.lightdream.api.utils.ItemBuilder;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public abstract class GUI implements InventoryProvider {
    public final IAPI api;
    public final GUIConfig config;

    public GUI(IAPI api) {
        this.api = api;
        this.config = setConfig();
    }

    public SmartInventory getInventory() {
        return SmartInventory.builder()
                .id(config.id)
                .provider(getProvider())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .manager(api.getInventoryManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));

        List<GUIItem> items = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        for (String key : config.items.keySet()) {
            items.add(config.items.get(key));
            keys.add(key);
        }

        int iter = 0;

        for (int i = items.size() - 1; i >= 0; i--) {

            while (canAddItem(items.get(0), keys.get(i))) {
                GUIItem item = items.get(i).clone();

                if(item.repeated){
                    if(item.nextSlots.size()<=iter){
                        break;
                    }
                    item.item.slot=item.nextSlots.get(iter);
                    iter++;
                }

                item.item.displayName = parse(item.item.displayName, player);
                item.item.lore = parse(item.item.lore, player);
                item.item.headOwner = parse(item.item.displayName, player);

                item.args = parse(item.args, player);

                contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                    List<String> functions = item.getFunctions();
                    functions.forEach(function -> functionCall(player, function, item.getFunctionArgs(function)));
                }));
                if (!item.repeated) {
                    break;
                }
            }

        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public abstract String parse(String raw, Player player);

    @SuppressWarnings("unchecked")
    public MessageBuilder parse(MessageBuilder raw, Player player) {
        if (raw.isList()) {
            return raw.changeBase(parse((List<String>) raw.getBase(), player));
        } else {
            return raw.changeBase(parse((String) raw.getBase(), player));
        }
    }


    @SuppressWarnings("UnusedAssignment")
    public GUIItem.GUIItemArgs parse(GUIItem.GUIItemArgs args, Player player) {
        return args.parse((function, arg) -> {
            function = parse(function, player);
            arg = parse(arg, player);
        });

    }

    public List<String> parse(List<String> raw, Player player) {
        List<String> output = new ArrayList<>();

        raw.forEach(line -> output.add(parse(line, player)));

        return output;
    }

    public abstract GUIConfig setConfig();

    public abstract InventoryProvider getProvider();

    public abstract void functionCall(Player player, String function, MessageBuilder args);

    public abstract boolean canAddItem(GUIItem item, String key);

    public void open(Player player) {
        getInventory().open(player);
    }

    public void open(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        getInventory().open((Player) sender);
    }

    public void open(User user) {
        if (!user.isOnline()) {
            return;
        }
        getInventory().open(user.getPlayer());
    }

    public abstract HashMap<Class<?>, Object> getArgs();

}
