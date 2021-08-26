package dev.lightdream.api.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.files.config.SQLConfig;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public abstract class DatabaseManager {

    private final LightDreamPlugin plugin;
    private final SQLConfig sqlSettings;
    private final ConnectionSource connectionSource;
    HashMap<Class<?>, Dao<?, ?>> daoMap = new HashMap<>();

    @SneakyThrows
    public DatabaseManager(LightDreamPlugin plugin) {
        this.plugin = plugin;
        this.sqlSettings = plugin.sqlConfig;
        String databaseURL = getDatabaseURL();

        connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlSettings.username,
                sqlSettings.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );
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
                return "jdbc:sqlite:" + new File(plugin.getDataFolder(), sqlSettings.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlSettings.driver.name());
        }
    }

    @SneakyThrows
    public DatabaseConnection getDatabaseConnection() {
        return connectionSource.getReadWriteConnection(null);
    }

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

    @SneakyThrows
    public void save() {
        for (Class<?> clazz : daoMap.keySet()) {
            daoMap.get(clazz).commit(getDatabaseConnection());
        }
    }

    @SneakyThrows
    public void save(Object object) {
        ((Dao<Object, Integer>) daoMap.get(object.getClass())).createOrUpdate(object);
    }


}