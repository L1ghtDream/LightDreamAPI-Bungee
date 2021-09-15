package dev.lightdream.api.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.api.API;
import dev.lightdream.api.files.dto.PluginLocation;
import dev.lightdream.api.utils.XPUtils;
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

    @SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
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

    @SuppressWarnings("unused")
    public void setLang(String lang) {
        this.lang = lang;
    }

    @SuppressWarnings("unused")
    public boolean hasMoney(double amount){
        return API.instance.getEconomy().has(getOfflinePlayer(), amount);
    }

    @SuppressWarnings("unused")
    public void addMoney(double amount){
        API.instance.getEconomy().depositPlayer(getOfflinePlayer(), amount);
    }

    @SuppressWarnings("unused")
    public void removeMoney(double amount){
        API.instance.getEconomy().withdrawPlayer(getOfflinePlayer(), amount);
    }

    @SuppressWarnings("unused")
    public double getMoney(){
        return API.instance.getEconomy().getBalance(getOfflinePlayer());
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    public boolean hasXP(int xp){
        if(!isOnline()){
            return false;
        }
        return XPUtils.getTotalExperience(getPlayer())>=xp;
    }

    @SuppressWarnings("unused")
    public void addXP(int xp){
        if(!isOnline()) {
            return;
        }
        XPUtils.setTotalExperience(getPlayer(), getXP()+xp);
    }

    @SuppressWarnings("unused")
    public void removeXP(int xp){
        if(!isOnline()){
            return;
        }
        XPUtils.setTotalExperience(getPlayer(),getXP()-xp);
    }

    @SuppressWarnings({"unused", "ConstantConditions"})
    public int getXP(){
        if(!isOnline()){
            return 0;
        }
        return XPUtils.getTotalExperience(getPlayer());
    }


}
