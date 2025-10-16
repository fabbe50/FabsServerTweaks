package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Fabsservertweaks.MOD_ID)
public final class FabsservertweaksNeoForge {
    public FabsservertweaksNeoForge(IEventBus bus) {
        // Run our common setup.
        Fabsservertweaks.init();
    }
}
