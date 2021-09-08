package dev.lightdream.api.managers.local;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.managers.DatabaseManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class LocalDatabaseManager extends DatabaseManager {

    @SneakyThrows
    public LocalDatabaseManager(LightDreamPlugin plugin) {
        super(plugin);
        createTable(User.class);
        createDao(User.class).setAutoCommit(getDatabaseConnection(), false);
    }

    @SneakyThrows
    public @NotNull List<User> getUsers() {
        return (List<User>) getDao(User.class).queryForAll();
    }

    public @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getUsers().stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName(), plugin.baseConfig.baseLang);
        save(user);
        return user;
    }

    public @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = getUsers().stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
    }

    public @NotNull User getUser(@NotNull OfflinePlayer player) {
        return getUser(player.getUniqueId());
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(int id) {
        Optional<User> optionalUser = getUsers().stream().filter(user -> user.id == id).findFirst();

        return optionalUser.orElse(null);
    }

}
