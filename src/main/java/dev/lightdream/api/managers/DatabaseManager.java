package dev.lightdream.api.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.config.SQLConfig;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unchecked")
public class DatabaseManager {

    public final IAPI api;
    private final SQLConfig sqlSettings;
    private final ConnectionSource connectionSource;
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Class<?>, Dao<?, ?>> daoMap = new HashMap<>();
    private final HashMap<Class<?>, List<?>> cacheMap = new HashMap<>();

    @SneakyThrows
    @SuppressWarnings("unused")
    public DatabaseManager(IAPI api) {
        this.api = api;
        this.sqlSettings = api.getSQLConfig();
        String databaseURL = getDatabaseURL();

        this.connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlSettings.username,
                sqlSettings.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        setup(User.class);
    }


    private @NotNull String getDatabaseURL() {
        switch (sqlSettings.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + sqlSettings.driver.toString().toLowerCase() + "://" + sqlSettings.host + ":" + sqlSettings.port + "/" + sqlSettings.database + "?useSSL=" + sqlSettings.useSSL + "&autoReconnect=true";
            case SQLSERVER:
                return "jdbc:sqlserver://" + sqlSettings.host + ":" + sqlSettings.port + ";databaseName=" + sqlSettings.database;
            case H2:
                return "jdbc:h2:file:" + sqlSettings.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(api.getDataFolder(), sqlSettings.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlSettings.driver.name());
        }
    }

    @SneakyThrows
    public DatabaseConnection getDatabaseConnection() {
        return connectionSource.getReadWriteConnection(null);
    }

    @SuppressWarnings("unused")
    @SneakyThrows
    public void createTable(Class<?> clazz) {
        TableUtils.createTableIfNotExists(connectionSource, clazz);
    }

    @SneakyThrows
    public Dao<?, ?> createDao(Class<?> clazz) {
        daoMap.put(clazz, DaoManager.createDao(connectionSource, clazz));
        return daoMap.get(clazz);
    }

    public Dao<?, ?> getDao(Class<?> clazz) {
        return daoMap.get(clazz);
    }

    @SuppressWarnings("unused")
    @SneakyThrows
    public void save() {
        cacheMap.forEach((clazz, list) -> list.forEach(obj -> {
            try {
                ((Dao<Object, Integer>) getDao(clazz)).createOrUpdate(obj);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }));

        for (Class<?> clazz : daoMap.keySet()) {
            daoMap.get(clazz).commit(getDatabaseConnection());
        }
    }

    @SneakyThrows
    public void save(Object object, boolean cache) {
        if (cache) {
            List<Object> list = (List<Object>) cacheMap.getOrDefault(object.getClass(), new ArrayList<>());
            list.add(object);
            cacheMap.put(object.getClass(), list);
        } else {
            ((Dao<Object, Integer>) daoMap.get(object.getClass())).createOrUpdate(object);
        }
    }

    public void save(Object object) {
        save(object, true);
    }

    @SneakyThrows
    public <T> List<T> getAll(Class<T> clazz, boolean cache) {
        if (cache) {
            return (List<T>) cacheMap.get(clazz);
        } else {
            return (List<T>) getDao(clazz).queryForAll();
        }
    }

    public <T> List<T> getAll(Class<T> clazz) {
        return getAll(clazz, true);
    }

    @SuppressWarnings("unused")
    @SneakyThrows
    public void delete(Object object) {
        List<Object> list = (List<Object>) cacheMap.get(object.getClass());
        list.remove(object);
        cacheMap.put(object.getClass(), list);
        ((Dao<Object, Integer>) daoMap.get(object.getClass())).delete(object);
    }


    @SneakyThrows
    public void setup(Class<?> clazz){
        createTable(clazz);
        createDao(clazz).setAutoCommit(getDatabaseConnection(), false);
        cacheMap.put(clazz, getAll(clazz, false));
    }

    //Users

    public @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName(), api.getSettings().baseLang);
        save(user);
        return user;
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
    }

    @SuppressWarnings("unused")
    public @NotNull User getUser(@NotNull OfflinePlayer player) {
        return getUser(player.getUniqueId());
    }

    public @NotNull User getUser(@NotNull Player player) {
        return getUser(player.getUniqueId());
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(int id) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.id == id).findFirst();

        return optionalUser.orElse(null);
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            return null;
        }
        return getUser((Player) sender);
    }


}