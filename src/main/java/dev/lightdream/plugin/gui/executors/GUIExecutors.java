package dev.lightdream.plugin.gui.executors;

public enum GUIExecutors {

    BACK(null);

    public GUIExecutor executor;

    GUIExecutors(GUIExecutor executor){
        this.executor = executor;
    }

}
