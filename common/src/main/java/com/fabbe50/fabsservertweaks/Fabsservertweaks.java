package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.client.ClientData;
import com.fabbe50.fabsservertweaks.network.NetworkHandler;
import com.fabbe50.fabsservertweaks.registries.EventRegistry;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class Fabsservertweaks {
    public static final String MOD_ID = "fabsservertweaks";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));


    public static void init() {
        ModRegistry.init();
        ModGameRules.init();
        EventRegistry.init();
    }

    public static void initServer() {
        NetworkHandler.registerServerHandlers();
    }

    public static void initClient() {
        NetworkHandler.registerClientHandlers();
        ClientData.createDebugInfo();
    }

    public static ResourceLocation location(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static ResourceLocation location(String namespace, String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace, name);
    }
}
