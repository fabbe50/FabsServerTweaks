package com.fabbe50.fabsservertweaks.fabsservertweaks;

import com.fabbe50.fabsservertweaks.fabsservertweaks.client.ClientData;
import com.fabbe50.fabsservertweaks.fabsservertweaks.network.NetworkHandler;
import com.fabbe50.fabsservertweaks.fabsservertweaks.registries.EventRegistry;
import com.fabbe50.fabsservertweaks.fabsservertweaks.registries.ModGameRules;
import com.fabbe50.fabsservertweaks.fabsservertweaks.registries.ModRegistry;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class FabsServerTweaks
{
	public static final String MOD_ID = "fabsservertweaks";
	public static final String MOD_NAME = "Fab's Server Tweaks";
	public static final Logger LOGGER = LoggerFactory.getLogger("Fab's Server Tweaks");
	public static ModPlatform PLATFORM = null;

	public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

	public static void entrypoint(ModPlatform platform) {
		FabsServerTweaks.PLATFORM = platform;
		LOGGER.info("Started mod in %s loader".formatted(FabsServerTweaks.PLATFORM.getModloader()));

		ModConfig.register();

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