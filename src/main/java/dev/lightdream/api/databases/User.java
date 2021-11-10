package dev.lightdream.api.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.api.IAPI;
import dev.lightdream.api.utils.MessageBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@DatabaseTable(tableName = "users")
@dev.lightdream.api.annotations.DatabaseTable(table = "users")
public class User extends DatabaseEntry {

    @SuppressWarnings("unused")
    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @dev.lightdream.api.annotations.DatabaseField(columnName = "id", autoGenerate = true)
    public int id;
    @DatabaseField(columnName = "uuid", unique = true)
    @dev.lightdream.api.annotations.DatabaseField(columnName = "uuid", unique = true)
    public UUID uuid;
    @DatabaseField(columnName = "name", unique = true)
    @dev.lightdream.api.annotations.DatabaseField(columnName = "name", unique = true)
    public String name;
    @DatabaseField(columnName = "lang")
    @dev.lightdream.api.annotations.DatabaseField(columnName = "lang")
    public String lang;

    public User() {
        super(null);
    }

    public User(IAPI api, UUID uuid, String name, String lang) {
        super(api);
        this.uuid = uuid;
        this.name = name;
        this.lang = lang;
    }

    public @Nullable ProxiedPlayer getPlayer() {
        return api.getPlugin().getProxy().getPlayer(uuid);
    }

    @SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
    public boolean isOnline() {
        if (getPlayer() == null) {
            return false;
        }
        return getPlayer().isConnected();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @SuppressWarnings("unused")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @SuppressWarnings("unused")
    public void sendMessage(IAPI api, String msg) {
        api.getMessageManager().sendMessage(this, msg);
    }

    @SuppressWarnings("unused")
    public void sendMessage(IAPI api, MessageBuilder msg) {
        api.getMessageManager().sendMessage(this, msg);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean hasPermission(String permission) {
        if (!isOnline()) {
            return false;
        }
        return getPlayer().hasPermission(permission);
    }


}
