package dev.lightdream.plugin.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("unused")
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public int id;
    @DatabaseField(columnName = "uuid", unique = true)
    public UUID uuid;
    @DatabaseField(columnName = "name", unique = true)
    public String name;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

}
