package com.fabbe50.fabsservertweaks.fabric.plugins.base;

public abstract class Plugin implements IPlugin {
    protected boolean initialized;
    protected boolean bootstrapped;

    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        init();
        bootstrap();
    }

    abstract public void init();

    public void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        registerBlocks();
        registerBlockOverlays();
        registerItems();
        registerItemOverlays();
        registerElementHolderOverlays();
        registerBlockEntities();
        registerEntities();
    }
}
