package com.fabbe50.fabsservertweaks.fabric;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.data.PotionBrewingRecipes;
import com.fabbe50.fabsservertweaks.fabric.plugins.Plugins;
import com.fabbe50.fabsservertweaks.util.BuiltinDatapack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public final class FabsservertweaksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Fabsservertweaks.init();
        Plugins.init();
        for (BuiltinDatapack builtinDatapack : BuiltinDatapack.values()) {
            ResourceLoader.registerBuiltinPack(
                    builtinDatapack.id(),
                    FabricLoader.getInstance().getModContainer(Fabsservertweaks.MOD_ID).orElseThrow(),
                    (builtinDatapack.enabledByDefault() ? PackActivationType.DEFAULT_ENABLED : PackActivationType.NORMAL)
            );
        }
        FabricPotionBrewingBuilder.BUILD.register(PotionBrewingRecipes::register);
    }
}
