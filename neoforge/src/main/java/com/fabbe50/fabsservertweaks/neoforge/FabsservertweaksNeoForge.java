package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.neoforged.fml.common.Mod;

@Mod(value = Fabsservertweaks.MOD_ID)
public final class FabsservertweaksNeoForge {
    public FabsservertweaksNeoForge() {
        // Run our common setup.
        Fabsservertweaks.init();
    }
}
