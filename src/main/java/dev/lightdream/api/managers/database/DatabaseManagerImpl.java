package dev.lightdream.api.managers.database;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class DatabaseManagerImpl extends OmrLiteDatabaseManager implements IDatabaseManagerImpl {
    public DatabaseManagerImpl(IAPI api) {
        super(api);
    }

    //Users
    @Override
    public @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(api, uuid, api.getPlugin().getProxy().getPlayer(uuid).getName(), api.getSettings().baseLang);
        save(user);
        return user;
    }

    @SuppressWarnings("unused")
    @Override
    public @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
    }

    @Override
    public @NotNull User getUser(@NotNull ProxiedPlayer player) {
        return getUser(player.getUniqueId());
    }

    @SuppressWarnings("unused")
    @Override
    public @Nullable User getUser(int id) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.id == id).findFirst();

        return optionalUser.orElse(null);
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return getUser((ProxiedPlayer) sender);
        }
        return api.getConsoleUser();
    }
}
