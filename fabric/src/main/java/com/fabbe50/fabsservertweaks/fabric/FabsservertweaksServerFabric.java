package com.fabbe50.fabsservertweaks.fabric;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FabsservertweaksServerFabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Fabsservertweaks.initServer();
    }
}
