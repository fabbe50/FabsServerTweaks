package com.fabbe50.fabsservertweaks.fabric;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.fabricmc.api.ModInitializer;

public final class FabsservertweaksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Fabsservertweaks.init();
    }
}
