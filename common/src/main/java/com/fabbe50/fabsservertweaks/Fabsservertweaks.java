package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.registries.EventRegistry;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import net.minecraft.resources.ResourceLocation;

public final class Fabsservertweaks {
    public static final String MOD_ID = "fabsservertweaks";

    public static void init() {
        ModRegistry.init();
        ModGameRules.init();
        EventRegistry.init();
    }

    public static void initServer() {
    }

    public static void initClient() {
    }

    public static ResourceLocation location(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static ResourceLocation location(String namespace, String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace, name);
    }
}
