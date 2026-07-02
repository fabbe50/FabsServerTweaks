package com.fabbe50.fabsservertweaks.neoforge;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.PotionBrewingRecipes;
import com.fabbe50.fabsservertweaks.neoforge.event.PackEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@Mod(value = Fabsservertweaks.MOD_ID)
public final class FabsservertweaksNeoForge {
    public FabsservertweaksNeoForge(IEventBus bus) {
        Fabsservertweaks.initRegistries();
        if (!DatagenModLoader.isRunningDataGen()) {
            Fabsservertweaks.initRuntime();
        }
        bus.register(PackEvents.class);
    }

    @EventBusSubscriber(modid = Fabsservertweaks.MOD_ID)
    public static class Events {
        @SubscribeEvent
        public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
            PotionBrewingRecipes.register(event.getBuilder());
        }
    }
}
