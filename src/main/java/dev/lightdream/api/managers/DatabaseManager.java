package dev.lightdream.api.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.configs.SQLConfig;
import dev.lightdream.api.databases.DatabaseEntry;
import dev.lightdream.api.databases.EditableDatabaseEntry;
import dev.lightdream.api.databases.User;
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

public class DatabaseManager {

    public final IAPI api;
    private final SQLConfig sqlSettings;
    private final ConnectionSource connectionSource;
    private final DatabaseConnection databaseConnection;
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Class<?>, List<DatabaseEntry>> cacheMap = new HashMap<>();
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Class<?>, Dao<?, ?>> daoMap = new HashMap<>();

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

        this.databaseConnection = connectionSource.getReadWriteConnection(null);
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

    @SneakyThrows
    public Dao<?, ?> getDao(Class<?> clazz) {
        if (!daoMap.containsKey(clazz)) {
            throw new Exception("The class '" + clazz.getSimpleName() + "' has not been setup. Use setup(" + clazz.getSimpleName() + ".class); in your database manager");
        }
        return daoMap.get(clazz);
    }

    @SuppressWarnings({"unused", "unchecked"})
    @SneakyThrows
    public void save(boolean cache) {
        api.getLogger().info("Saving database tables to " + api.getDataFolder());
        if (cache) {
            cacheMap.forEach((clazz, list) -> {
                list.forEach(obj -> {
                    try {
                        ((Dao<DatabaseEntry, Integer>) getDao(clazz)).createOrUpdate(obj);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                });
                api.getLogger().info("Saving table " + getDao(clazz).getTableName());
                try {
                    getDao(clazz).commit(databaseConnection);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });
        } else {
            for (Dao<?, ?> dao : daoMap.values()) {
                api.getLogger().info("Saving table " + dao.getTableName());
                dao.commit(databaseConnection);
            }
        }

    }

    public void save() {
        save(true);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void save(DatabaseEntry entry, boolean cache) {
        if (cache) {
            List<DatabaseEntry> list = cacheMap.getOrDefault(entry.getClass(), new ArrayList<>());
            if (list.contains(entry)) {
                list.remove(entry);
            } else {
                ((Dao<DatabaseEntry, Integer>) daoMap.get(entry.getClass())).createOrUpdate(entry);
            }
            list.add(entry);
            cacheMap.put(entry.getClass(), list);
        } else {
            ((Dao<DatabaseEntry, Integer>) daoMap.get(entry.getClass())).createOrUpdate(entry);
            daoMap.get(entry.getClass()).commit(databaseConnection);
        }
    }

    public void save(DatabaseEntry entry) {
        save(entry, true);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T> List<T> getAll(Class<T> clazz, boolean cache) {
        if (cache) {
            if (!cacheMap.containsKey(clazz)) {
                throw new Exception("The class '" + clazz.getSimpleName() + "' has not been setup. Use setup(" + clazz.getSimpleName() + ".class); in your database manager");
            }
            return (List<T>) cacheMap.get(clazz);
        } else {
            List<T> list = (List<T>) queryAll(clazz);
            for (T t : list) {
                if (t instanceof EditableDatabaseEntry) {
                    ((EditableDatabaseEntry) t).setAPI(api);
                }
            }
            return list;
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public List<Integer> getAllIDs(Class<?> clazz, boolean cache) {
        List<DatabaseEntry> entries;
        if (cache) {
            if (!cacheMap.containsKey(clazz)) {
                throw new Exception("The class '" + clazz.getSimpleName() + "' has not been setup. Use setup(" + clazz.getSimpleName() + ".class); in your database manager");
            }
            entries = cacheMap.get(clazz);
        } else {
            entries = (List<DatabaseEntry>) queryAll(clazz);
        }
        List<Integer> output = new ArrayList<>();
        for (DatabaseEntry databaseEntry : entries) {
            output.add(databaseEntry.getID());
        }
        return output;
    }

    public List<?> queryAll(Class<?> clazz){
        try{
            return getDao(clazz).queryForAll();
        }catch (Throwable t){
            t.printStackTrace();
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public List<Integer> getAllIDs(Class<?> clazz) {
        return getAllIDs(clazz, true);
    }

    public <T> List<T> getAll(Class<T> clazz) {
        return getAll(clazz, true);
    }

    @SuppressWarnings({"unused", "unchecked"})
    @SneakyThrows
    public void delete(DatabaseEntry entry) {
        List<DatabaseEntry> list = cacheMap.get(entry.getClass());
        list.remove(entry);
        cacheMap.put(entry.getClass(), list);
        ((Dao<DatabaseEntry, Integer>) daoMap.get(entry.getClass())).delete(entry);
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void setup(Class<?> clazz) {
        api.getLogger().info("Setting up " + clazz.getSimpleName() + " database table");
        createTable(clazz);
        createDao(clazz).setAutoCommit(databaseConnection, false);
        cacheMap.put(clazz, (List<DatabaseEntry>) getAll(clazz, false));
    }

    //Users
    public @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(api, uuid, Bukkit.getOfflinePlayer(uuid).getName(), api.getSettings().baseLang);
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
        if (sender instanceof Player) {
            return getUser((Player) sender);
        }
        return api.getConsoleUser();
    }


}