package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Fabsservertweaks.MOD_ID, dist = Dist.DEDICATED_SERVER)
public final class FabsservertweaksServerNeoForge {
    public FabsservertweaksServerNeoForge() {
        Fabsservertweaks.initServer();
    }
}
