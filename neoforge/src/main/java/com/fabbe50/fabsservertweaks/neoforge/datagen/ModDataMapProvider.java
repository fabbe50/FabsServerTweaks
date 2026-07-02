package com.fabbe50.fabsservertweaks.neoforge.datagen;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    private static final Map<ResourceKey<Item>, Compostable> COMPOSTABLES = new HashMap<>();

    public static void addCompostable(ResourceKey<Item> item, Compostable compostable) {
        COMPOSTABLES.put(item, compostable);
    }

    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(@NonNull Provider provider) {
        registerCompostables();
    }

    private void registerCompostables() {
        Builder<Compostable, Item> builder = builder(NeoForgeDataMaps.COMPOSTABLES);
        for (Map.Entry<ResourceKey<Item>, Compostable> entry : COMPOSTABLES.entrySet()) {
            builder.add(entry.getKey(), entry.getValue(), false);
        }
    }
}
