package dev.lightdream.plugin.gui.executors;

@SuppressWarnings("unused")
public enum GUIExecutors {

    EXAMPLE(null);

    public GUIExecutor executor;

    GUIExecutors(GUIExecutor executor){
        this.executor = executor;
    }

}
