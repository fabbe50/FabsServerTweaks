package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.neoforge.event.ClientEvents;
import com.fabbe50.fabsservertweaks.neoforge.plugins.ClothConfigPlugin;
import dev.architectury.platform.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Fabsservertweaks.MOD_ID, dist = Dist.CLIENT)
public final class FabsservertweaksClientNeoForge {
    public FabsservertweaksClientNeoForge(ModContainer container) {
        Fabsservertweaks.initClient();

        if (Platform.isModLoaded("cloth_config")) {
            ClothConfigPlugin.registerConfigScreen(container);
        }

        NeoForge.EVENT_BUS.register(new ClientEvents());
    }
}
