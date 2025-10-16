package com.fabbe50.fabsservertweaks.fabric;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.fabricmc.api.ClientModInitializer;

public class FabsservertweaksClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Fabsservertweaks.initClient();
    }
}
