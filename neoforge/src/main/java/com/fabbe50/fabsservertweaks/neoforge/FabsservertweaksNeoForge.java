package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.neoforge.datagen.DataGenerators;
import com.fabbe50.fabsservertweaks.neoforge.event.PackEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

@Mod(value = Fabsservertweaks.MOD_ID)
public final class FabsservertweaksNeoForge {
    public FabsservertweaksNeoForge(IEventBus bus) {
        Fabsservertweaks.initRegistries();
        if (!DatagenModLoader.isRunningDataGen()) {
            Fabsservertweaks.initRuntime();
        }
        bus.register(PackEvents.class);
    }
}
