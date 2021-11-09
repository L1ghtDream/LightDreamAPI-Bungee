package dev.lightdream.api.managers.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.annotations.DatabaseTable;
import dev.lightdream.api.databases.DatabaseEntry;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class HikariDatabaseManager extends DatabaseManager {

    private HikariDataSource ds;
    @SuppressWarnings("FieldMayBeFinal")
    //private HashMap<Class<?>, String> tables = new HashMap<>();

    public HikariDatabaseManager(IAPI api) {
        this.api = api;
        this.sqlConfig = api.getSQLConfig();
    }

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getDatabaseURL());
        config.setUsername(sqlConfig.username);
        config.setPassword(sqlConfig.password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    @SneakyThrows
    public Connection getConnection() {
        return ds.getConnection();
    }

    @SuppressWarnings("SqlNoDataSourceInspection")
    @SneakyThrows
    @Override
    public <T> List<T> getAll(Class<T> clazz) {

        if(!clazz.isAnnotationPresent(DatabaseTable.class)){
            //todo logger
            return new ArrayList<>();
        }

        List<T> output = new ArrayList<>();
        PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM ? WHERE 1");
        statement.setString(1, clazz.getAnnotation(DatabaseTable.class).table());
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            T obj = clazz.newInstance();
            Field[] fields = obj.getClass().getFields();
            for (Field field : fields) {
                field.set(obj, rs.getString(field.getName()));
            }
            output.add(obj);
        }
        return output;
    }

    @SneakyThrows
    @Override
    public void createTable(Class<?> clazz) {

        if(!clazz.isAnnotationPresent(DatabaseTable.class)){
            //todo logger
return;
        }

        Object obj = clazz.newInstance();
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ?.?(");

        Field[] fields = obj.getClass().getFields();
        //todo get annotations
        for (Field field : fields) {
            query.append(field.getName()).append(" ").append(getDataType(field)).append(",");
        }

        query.append(",");
        query = new StringBuilder(query.toString().replace(",,", ""));
        query.append(")");

        PreparedStatement statement = getConnection().prepareStatement(query.toString());
        statement.setString(1, sqlConfig.database);
        statement.setString(2, clazz.getAnnotation(DatabaseTable.class).table());
    }

    @Override
    public void setup() {

    }

    @Override
    public void setup(Class<?> clazz) {
        createTable(clazz);
        //todo implement cache
    }

    @Override
    public void save() {
        //todo
    }

    @Override
    public void save(DatabaseEntry object, boolean cache) {
        //todo
    }

    @Override
    public void delete(DatabaseEntry entry) {
        //todo
    }
}
