package dev.lightdream.api.databases;

import dev.lightdream.api.IAPI;

@SuppressWarnings("unused")
public abstract class EditableDatabaseEntry implements DatabaseEntry {

    private IAPI api;

    public EditableDatabaseEntry(IAPI api) {
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
