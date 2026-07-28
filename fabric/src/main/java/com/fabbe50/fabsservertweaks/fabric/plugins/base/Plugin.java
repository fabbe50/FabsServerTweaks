package com.fabbe50.fabsservertweaks.fabric.plugins.base;

public abstract class Plugin {
    protected boolean initialized;
    protected boolean bootstrapped;

    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        init();
    }

    abstract public void init();
}
