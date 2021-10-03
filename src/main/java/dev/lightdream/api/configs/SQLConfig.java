package dev.lightdream.api.configs;

import lombok.NoArgsConstructor;

@SuppressWarnings("CanBeFinal")
@NoArgsConstructor
public class SQLConfig {

    public Driver driver = Driver.SQLITE;
    public String host = "localhost";
    public String database = "LightDreamAPI";
    public String username = "";
    public String password = "";
    public int port = 3306;
    public boolean useSSL = false;

    @Override
    public String toString() {
        return "SQLConfig{" +
                "driver=" + driver +
                ", host='" + host + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", useSSL=" + useSSL +
                '}';
    }

    public enum Driver {
        MYSQL,
        MARIADB,
        SQLSERVER,
        POSTGRESQL,
        H2,
        SQLITE
    }
}
