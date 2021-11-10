package dev.lightdream.api.managers.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.annotations.DatabaseField;
import dev.lightdream.api.annotations.DatabaseTable;
import dev.lightdream.api.databases.DatabaseEntry;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unused", "SqlNoDataSourceInspection"})
public class HikariDatabaseManager extends DatabaseManager {

    private HikariDataSource ds;

    @SuppressWarnings("FieldMayBeFinal")

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
        if (!clazz.isAnnotationPresent(DatabaseTable.class)) {
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
    public <T> List<T> get(Class<T> clazz, HashMap<String, Object> queries) {
        if (queries.size() == 0) {
            return getAll(clazz);
        }

        if (!clazz.isAnnotationPresent(DatabaseTable.class)) {
            //todo logger
            return new ArrayList<>();
        }
        StringBuilder query = new StringBuilder("SELECT * FROM ? WHERE ");

        for (String key : queries.keySet()) {
            Object value = queries.get(key);
            query.append(key)
                    .append("=")
                    .append(formatQueryArgument(value))
                    .append(" AND ");
        }
        query.append(" ");
        query = new StringBuilder(query.toString().replace(" AND  ", ""));


        List<T> output = new ArrayList<>();
        PreparedStatement statement = getConnection().prepareStatement(query.toString());
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

    @SuppressWarnings("StringConcatenationInLoop")
    @SneakyThrows
    @Override
    public void createTable(Class<?> clazz) {

        if (!clazz.isAnnotationPresent(DatabaseTable.class)) {
            //todo logger
            return;
        }

        Object obj = clazz.newInstance();
        String query = "CREATE TABLE IF NOT EXISTS ?.?(";

        Field[] fields = obj.getClass().getFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(DatabaseField.class)) {
                return;
            }
            DatabaseField dbField = field.getAnnotation(DatabaseField.class);
            query += dbField.columnName() + " " +
                    getDataType(field) + " " +
                    (dbField.unique() ? "UNIQUE " : "") +
                    (dbField.autoGenerate() ? "AUTO_INCREMENT " : "") +
                    ",";
        }

        query += ",";
        query = query.replace(",,", "");
        query += ")";

        PreparedStatement statement = getConnection().prepareStatement(query);
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
        //todo implement cache
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
