package dev.lightdream.api.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.api.API;
import dev.lightdream.api.files.dto.PluginLocation;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@DatabaseTable(tableName = "users")
public class User {

    @SuppressWarnings("unused")
    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public int id;
    @DatabaseField(columnName = "uuid", unique = true)
    public UUID uuid;
    @DatabaseField(columnName = "name", unique = true)
    public String name;
    @DatabaseField(columnName = "lang")
    public String lang;

    public User(UUID uuid, String name, String lang) {
        this.uuid = uuid;
        this.name = name;
        this.lang = lang;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public @NotNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @SuppressWarnings("unused")
    public @Nullable PluginLocation getLocation() {
        Player player = getPlayer();
        if (player == null) {
            return null;
        }
        return new PluginLocation(player.getLocation());
    }

    @SuppressWarnings("unused")
    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
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

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean hasMoney(double amount){
        return API.instance.getEconomy().has(getOfflinePlayer(), amount);
    }

    public void addMoney(double amount){
        API.instance.getEconomy().depositPlayer(getOfflinePlayer(), amount);
    }

    public void removeMoney(double amount){
        API.instance.getEconomy().withdrawPlayer(getOfflinePlayer(), amount);
    }
}
