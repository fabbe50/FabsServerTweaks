package com.fabbe50.fabsservertweaks;

import com.fabbe50.fabsservertweaks.client.ClientData;
import com.fabbe50.fabsservertweaks.data.nickname.NicknameRegistry;
import com.fabbe50.fabsservertweaks.data.soulbound.SoulBoundRegistry;
import com.fabbe50.fabsservertweaks.data.stats.StatsRegistry;
import com.fabbe50.fabsservertweaks.network.NetworkHandler;
import com.fabbe50.fabsservertweaks.registries.EventRegistry;
import com.fabbe50.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.registries.ModRegistry;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public final class Fabsservertweaks {
    public static final String MOD_ID = "fabsservertweaks";
    public static final String MOD_NAME = "Fab's Server Tweaks";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    public static ModConfig CONFIG;
    public static boolean registryLoaded = false;

    public static void init() {
        initRegistries();
        initRuntime();
    }

    public static void initRegistries() {
        ModRegistry.init();
        registryLoaded = true;
    }

    public static void initRuntime() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        ModGameRules.init();
        EventRegistry.init();
    }

    public static void initServer() {
        NetworkHandler.registerServerHandlers();
    }

    public static void initClient() {
        NetworkHandler.registerClientHandlers();
        ClientData.init();
    }

    public static Identifier location(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static Identifier location(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, name);
    }
}
