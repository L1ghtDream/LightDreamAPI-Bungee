package dev.lightdream.api.databases;

import dev.lightdream.api.IAPI;

@SuppressWarnings("unused")
public abstract class EditableDatabaseEntry implements DatabaseEntry {

    private IAPI api;

    public EditableDatabaseEntry(IAPI api) {
        this.api = api;
    }

    public void save() {
        api.getDatabaseManager().save(this);
    }

    public void delete() {
        api.getDatabaseManager().delete(this);
    }

    public void setAPI(IAPI api) {
        this.api = api;
    }

}
