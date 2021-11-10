package dev.lightdream.api.databases;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.utils.MessageBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ConsoleUser extends User {
    public ConsoleUser() {
        super(null, null, "CONSOLE", "");
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return null;
    }


    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void setLang(String lang) {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void sendMessage(IAPI api, String msg) {
        api.getPlugin().getProxy().getConsole().sendMessage(msg);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void sendMessage(IAPI api, MessageBuilder msg) {
        api.getPlugin().getProxy().getConsole().sendMessage(msg.parseString());
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }


}
