package com.fabbe50.fabsservertweaks.util;

import com.fabbe50.fabsservertweaks.Fabsservertweaks;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class RegistryHelper {
    public static Set<Entry<ResourceKey<Item>, Item>> getItemEntries() {
        if (Fabsservertweaks.registryLoaded) {
            return ModRegistry.ITEMS.entrySet();
        }
        return new HashSet<>();
    }
}
