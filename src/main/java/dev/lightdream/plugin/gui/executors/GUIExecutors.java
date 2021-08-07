package dev.lightdream.plugin.gui.executors;

@SuppressWarnings("unused")
public enum GUIExecutors {

    BACK(null);

    public GUIExecutor executor;

    GUIExecutors(GUIExecutor executor){
        this.executor = executor;
    }

}
