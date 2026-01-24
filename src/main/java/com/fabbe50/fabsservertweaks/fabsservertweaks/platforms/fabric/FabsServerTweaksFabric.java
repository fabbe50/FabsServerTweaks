//? if fabric {
/*package com.fabbe50.fabsservertweaks.fabsservertweaks.platforms.fabric;

import com.fabbe50.fabsservertweaks.fabsservertweaks.ModPlatform;
import net.fabricmc.api.ModInitializer;
import com.fabbe50.fabsservertweaks.fabsservertweaks.FabsServerTweaksInit;
import net.fabricmc.loader.api.FabricLoader;

public class FabsServerTweaksFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		FabsServerTweaksInit.entrypoint(new FabricPlatform());
	}
	public static class FabricPlatform implements ModPlatform{

		@Override
		public String getModloader() {
			return "Fabric";
		}

		@Override
		public boolean isModLoaded(String modloader) {
			return FabricLoader.getInstance().isModLoaded(modloader);
		}
		
		@Override
        public String getConfigPath() {
            return FabricLoader.getInstance().getConfigDir().toString();
        }
	}
}
*///?}