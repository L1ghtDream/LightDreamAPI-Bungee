package dev.lightdream.api.databases;

import dev.lightdream.api.IAPI;

@SuppressWarnings("unused")
public abstract class EditableDatabaseEntry implements DatabaseEntry {

    private final IAPI api;

    public EditableDatabaseEntry(IAPI api){
        this.api = api;
        save();
    }

    public void save(){
        api.getDatabaseManager().save(this);
    }

    public void delete(){
        api.getDatabaseManager().delete(this);
    }

}
