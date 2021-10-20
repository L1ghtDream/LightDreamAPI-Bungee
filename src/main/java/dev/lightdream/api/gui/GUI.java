package dev.lightdream.api.gui;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.dto.GUIConfig;
import dev.lightdream.api.dto.GUIItem;
import dev.lightdream.api.dto.XMaterial;
import dev.lightdream.api.utils.ItemBuilder;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.api.utils.Utils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public abstract class GUI implements InventoryProvider {
    public final IAPI api;
    public final GUIConfig config;

    @SneakyThrows
    public GUI(IAPI api) {
        this.api = api;
        this.config = setConfig();
        if (this.config == null) {
            throw new Exception("The gui config with this id does not exist in the config");
        }
        api.getEventManager().registeredGUIs.add(this);
    }

    public SmartInventory getInventory() {
        return SmartInventory.builder()
                .id(config.id)
                .provider(getProvider())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .manager(api.getInventoryManager())
                .listener(getInventoryClickListener())
                .listener(getInventoryCloseListener())
                .closeable(!preventClose())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        if (!config.fillItem.material.equals(XMaterial.AIR)) {
            contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));
        }

        List<GUIItem> items = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        for (String key : config.items.keySet()) {
            items.add(config.items.get(key));
            keys.add(key);
        }

        int iter = 0;

        for (int i = items.size() - 1; i >= 0; i--) {
            while (canAddItem(items.get(i), keys.get(i))) {
                GUIItem item = items.get(i).clone();

                if (item.repeated) {
                    if (item.nextSlots.size() <= iter) {
                        break;
                    }
                    item.item.slot = item.nextSlots.get(iter);
                    iter++;
                }

                if (item.item.material.equals(XMaterial.PLACEHOLDER)) {
                    item.item.material = XMaterial.matchXMaterial(parse("%material%", player)).orElse(XMaterial.AIR);
                }
                if (item.item.amount == null) {
                    item.item.amount = Integer.parseInt(parse("%amount%", player));
                }
                item.item.displayName = parse(item.item.displayName, player);
                item.item.lore = parse(item.item.lore, player);
                item.item.headOwner = parse(item.item.displayName, player);

                item.args = parse(item.args, player);

                if (item.item.material.equals(XMaterial.AIR)) {
                    contents.set(Utils.getSlotPosition(item.item.slot), null);
                } else {
                    contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.of(ItemBuilder.makeItem(item.item), e -> {
                        List<String> functions = item.functions();
                        functions.forEach(function -> functionCall(player, function, item.functionArgs(function)));
                    }));
                }
                if (!item.repeated) {
                    break;
                }
            }
        }

        setItems(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        if (config.update) {
            beforeUpdate(player, contents);
            init(player, contents);
        }
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

    public abstract void functionCall(Player player, String function, List<String> args);

    public abstract boolean canAddItem(GUIItem item, String key);

    public void open(Player player) {
        getInventory().open(player);
    }

    public void open(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        open((Player) sender);
    }

    public void open(User user) {
        if (!user.isOnline()) {
            return;
        }
        open(user.getPlayer());
    }

    public abstract HashMap<Class<?>, Object> getArgs();

    public abstract void setItems(Player player, InventoryContents contents);

    public abstract void beforeUpdate(Player player, InventoryContents contents);

    public GUIConfig getConfig() {
        return config;
    }

    public String getID() {
        if (config == null) {
            return "";
        }
        return config.id;
    }

    @SuppressWarnings("unchecked")
    public <T> T getArg(Class<T> clazz) {
        return (T) getArgs().getOrDefault(clazz, null);
    }


    public InventoryListener<InventoryCloseEvent> getInventoryCloseListener() {
        return new InventoryListener<>(InventoryCloseEvent.class, this::a);
    }

    public InventoryListener<InventoryClickEvent> getInventoryClickListener() {
        return new InventoryListener<>(InventoryClickEvent.class, this::b);
    }

    public final void a(InventoryCloseEvent event) {
        api.getEventManager().registeredGUIs.remove(this);
        onInventoryClose(event);
    }

    public final void b(InventoryClickEvent event) {
        onInventoryClick(event);
    }

    public final void c(InventoryClickEvent event) {

        if (event.getRawSlot() < 9 * config.rows) {
            System.out.println("Not in player inventory");
            return;
        }

        if (!event.getView().getTopInventory().getTitle().equals(Utils.color(config.title))) {
            System.out.println("Not the title");
            return;
        }

        onPlayerInventoryClick(event);
    }

    public abstract void onInventoryClose(InventoryCloseEvent event);

    public abstract void onInventoryClick(InventoryClickEvent event);

    public abstract void onPlayerInventoryClick(InventoryClickEvent event);

    public abstract boolean preventClose();

    @SuppressWarnings("unchecked")
    public <T> T getInventoryHandler(User user, Class<T> clazz) {
        SmartInventory smartInventory = api.getInventoryManager().getInventory(user.getPlayer()).orElse(null);
        if (smartInventory == null) {
            return null;
        }
        InventoryProvider provider = smartInventory.getProvider();

        if (provider == null) {
            return null;
        }

        return (T) provider;
    }

}
