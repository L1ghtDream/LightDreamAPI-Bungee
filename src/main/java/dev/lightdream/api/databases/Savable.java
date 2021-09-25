package dev.lightdream.api.databases;

import dev.lightdream.api.IAPI;

public abstract class Savable {

    private final IAPI api;

    public Savable(IAPI api){
        this.api = api;
    }

    public void save(){
        api.getDatabaseManager().save(this);
    }

}
