package dev.lightdream.api.files.config;

import dev.lightdream.api.API;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SQLConfig {

    public Driver driver = Driver.SQLITE;
    public String host = "localhost";
    public String database = API.instance.projectName;
    public String username = "";
    public String password = "";
    public int port = 3306;
    public boolean useSSL = false;

    public enum Driver {
        MYSQL,
        MARIADB,
        SQLSERVER,
        POSTGRESQL,
        H2,
        SQLITE
    }

}
