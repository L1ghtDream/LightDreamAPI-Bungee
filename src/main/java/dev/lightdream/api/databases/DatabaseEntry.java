package dev.lightdream.api.databases;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.annotations.DatabaseField;

public abstract class DatabaseEntry {

    @DatabaseField(columnName = "id", autoGenerate = true, unique = true)
    public int id;
    public IAPI api;

    public DatabaseEntry(IAPI api) {
        this.api = api;
    }

    public void save() {
        save(true);
    }

    public void save(boolean cache){
        api.getDatabaseManager().save(this, cache);
    }

    public void delete() {
        api.getDatabaseManager().delete(this);
    }

    public void setAPI(IAPI api) {
        this.api = api;
    }

}
